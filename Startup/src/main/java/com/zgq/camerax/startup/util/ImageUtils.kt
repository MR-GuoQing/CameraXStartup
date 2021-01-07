package com.sap.cloud.mobile.fiori.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.RectF
import android.media.Image
import android.renderscript.Element.YUV
import androidx.renderscript.*
import android.util.Log
import com.zgq.camerax.startup.camerax.GraphicOverlay
import com.zgq.camerax.startup.util.ScriptC_rgb_crop
import com.zgq.camerax.startup.util.ScriptC_rgb_rotate
import java.nio.ByteBuffer

/**
 * Utility class for images processing.
 */
internal class ImageUtils constructor(val context: Context) {

    private var pixelCount: Int = -1
    private lateinit var byteBuffer: ByteBuffer
    private lateinit var inputAllocation: Allocation
    private lateinit var rotateAllocation: Allocation
    private lateinit var croppedAllocation: Allocation
    private lateinit var rgbAllocation: Allocation
    private val rs: RenderScript = RenderScript.create(context)
    private val scriptYuvToRgb: ScriptIntrinsicYuvToRGB
    private var elemType: Type.Builder
    private lateinit var rotatedType: Type.Builder
    private lateinit var croppedType: Type.Builder
    private lateinit var rgbType: Type.Builder
    private lateinit var bitmapBuffer: Bitmap
    private lateinit var croppedBitmapBuffer: Bitmap
    private var mRotateScript: ScriptC_rgb_rotate
    private var mCropScriptC: ScriptC_rgb_crop

    init {
        scriptYuvToRgb = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))
        elemType = Type.Builder(rs, Element.U8(rs)).setYuvFormat(ImageFormat.NV21)
        mRotateScript = ScriptC_rgb_rotate(rs)
        mCropScriptC = ScriptC_rgb_crop(rs)
    }

    fun processingYUVImage2Bitmap(image: Image, rotation: Int, cropRectF: RectF?): Bitmap{

        val nv21 = imageToByteBuffer(image)
        if (!::inputAllocation.isInitialized){
            elemType.setX(image.width)
            elemType.setY(image.height)
            inputAllocation = Allocation.createTyped(rs, elemType.create(), Allocation.USAGE_SCRIPT)
        }

        inputAllocation.copyFrom(nv21)
        var rotatedWidth: Int = image.width
        var rotatedHeight: Int = image.height
        val degree = (rotation + 360) % 360

        when(degree){
            90, 270 -> {
                rotatedWidth = image.height
                rotatedHeight = image.width
            }
            0, 180 -> {
                rotatedWidth = image.width
                rotatedHeight = image.height
            }
        }

        rgbType = Type.Builder(rs, Element.RGBA_8888(rs))
        rgbType.setX(image.width)
        rgbType.setY(image.height)
        rgbAllocation = Allocation.createTyped(rs, rgbType.create(), Allocation.USAGE_SCRIPT)


        scriptYuvToRgb.setInput(inputAllocation)
        scriptYuvToRgb.forEach(rgbAllocation)
        rotatedType = Type.Builder(rs, Element.RGBA_8888(rs))
        rotatedType.setX(rotatedWidth)
        rotatedType.setY(rotatedHeight)
        rotateAllocation = Allocation.createTyped(rs, rotatedType.create(), Allocation.USAGE_SCRIPT)
        mRotateScript._inImage = rgbAllocation
        mRotateScript._inWidth = image.width
        mRotateScript._inHeight = image.height

        when (degree) {
            90 -> mRotateScript.forEach_rotate_90_clockwise(rotateAllocation)
            180 -> mRotateScript.forEach_rotate_180(rotateAllocation)
            279 -> mRotateScript.forEach_rotate_270_clockwise(rotateAllocation)
        }
        cropRectF?.let {
            croppedType = Type.Builder(rs, Element.RGBA_8888(rs))
            croppedType.setX(it.width().toInt())
            croppedType.setY(it.height().toInt())
            croppedAllocation = Allocation.createTyped(rs, croppedType.create(), Allocation.USAGE_SCRIPT)
            mCropScriptC._input = rotateAllocation
            mCropScriptC._xStart = it.left.toLong()
            mCropScriptC._yStart = it.top.toLong()
            mCropScriptC.forEach_crop(croppedAllocation)
            rotatedWidth = it.width().toInt()
            rotatedHeight = it.height().toInt()
            croppedBitmapBuffer = Bitmap.createBitmap(rotatedWidth, rotatedHeight, Bitmap.Config.ARGB_8888)
            croppedAllocation.copyTo(croppedBitmapBuffer)
            return@processingYUVImage2Bitmap croppedBitmapBuffer
        }
        bitmapBuffer = Bitmap.createBitmap(rotatedWidth, rotatedHeight, Bitmap.Config.ARGB_8888)
        rotateAllocation.copyTo(bitmapBuffer)

        return bitmapBuffer
    }

    private fun imageToByteBuffer(image: Image): ByteArray {
        assert(image.format == ImageFormat.YUV_420_888)
        if (!::byteBuffer.isInitialized) {
            pixelCount = image.cropRect.width() * image.cropRect.height()
            // Bits per pixel is an average for the whole image, so it's useful to compute the size
            // of the full buffer but should not be used to determine pixel offsets
            val pixelSizeBits = ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888)
            byteBuffer = ByteBuffer.allocateDirect(pixelCount * pixelSizeBits / 8)
        }

        // Rewind the buffer; no need to clear it since it will be filled
        byteBuffer.rewind()
        val byteArray = byteBuffer.array()

        val imageCrop = image.cropRect
        val imagePlanes = image.planes

        imagePlanes.forEachIndexed { planeIndex, plane ->
            val outputStride: Int
            var outputOffset: Int

            when (planeIndex) {
                0 -> {
                    outputStride = 1
                    outputOffset = 0
                }
                1 -> {
                    outputStride = 2
                    // For NV21 format, U is in odd-numbered indices
                    outputOffset = pixelCount + 1
                }
                2 -> {
                    outputStride = 2
                    // For NV21 format, V is in even-numbered indices
                    outputOffset = pixelCount
                }
                else -> {
                    // Image contains more than 3 planes, something strange is going on
                    return@forEachIndexed
                }
            }

            val planeBuffer = plane.buffer
            val rowStride = plane.rowStride
            val pixelStride = plane.pixelStride
            Log.e("Test","$planeIndex--size-->${planeBuffer.remaining()}rowStride-->$rowStride--pixelStride-->$pixelStride")
            // We have to divide the width and height by two if it's not the Y plane
            val planeCrop = if (planeIndex == 0) {
                imageCrop
            } else {
                Rect(
                        imageCrop.left / 2,
                        imageCrop.top / 2,
                        imageCrop.right / 2,
                        imageCrop.bottom / 2
                )
            }

            val planeWidth = planeCrop.width()
            val planeHeight = planeCrop.height()

            // Intermediate buffer used to store the bytes of each row
            val rowBuffer = ByteArray(plane.rowStride)

            // Size of each row in bytes
            val rowLength = if (pixelStride == 1 && outputStride == 1) {
                planeWidth
            } else {
                // We need to get (N-1) * (pixel stride bytes) per row + 1 byte for the last pixel
                (planeWidth - 1) * pixelStride + 1
            }
            Log.e("test", "$planeIndex-->$rowLength")
            for (row in 0 until planeHeight) {
                // Move buffer position to the beginning of this row
                planeBuffer.position(
                        (row + planeCrop.top) * rowStride + planeCrop.left * pixelStride)

                if (pixelStride == 1 && outputStride == 1) {
                    // When there is a single stride value for pixel and output, we can just copy
                    // the entire row in a single step
                    planeBuffer.get(byteArray, outputOffset, rowLength)
                    outputOffset += rowLength
                } else {
                    // When either pixel or output have a stride > 1 we must copy pixel by pixel
                    planeBuffer.get(rowBuffer, 0, rowLength)
                    for (col in 0 until planeWidth) {
                        byteArray[outputOffset] = rowBuffer[col * pixelStride]
                        outputOffset += outputStride
                    }
                }
            }
        }
        return byteArray
    }

//    fun transformPreviewToImage(rectF: RectF, sizeData: GraphicOverlay): RectF{
//        val tmpRect = RectF()
//        tmpRect.left = (rectF.left / sizeData.previewSize.width) * sizeData.imageSize.width
//        tmpRect.top = (rectF.top / sizeData.previewSize.height) * sizeData.imageSize.height
//        tmpRect.right = (rectF.right / sizeData.previewSize.width) * sizeData.imageSize.width
//        tmpRect.bottom = (rectF.bottom / sizeData.previewSize.height) * sizeData.imageSize.height
//        return tmpRect
//    }

}

