package com.zgq.camerax.startup.util

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.media.Image
import android.util.Size
import androidx.core.graphics.toRect
import androidx.renderscript.*
import com.zgq.camerax.startup.util.rs.ScriptC_rgb_rotate
import com.zgq.camerax.startup.util.rs.ScriptC_yuv420_888_to_rgb
import com.zgq.camerax.startup.util.rs.ScriptC_rgb_crop
import kotlin.math.min

class ImageProcessor private constructor(
    private val rs: RenderScript,
    private val size: Size,
    private val rect: RectF?,
    private val image: Image,
    private val rotateDegree: Int
){
    private lateinit var yAllocation: Allocation
    private lateinit var uAllocation: Allocation
    private lateinit var vAllocation: Allocation
    private lateinit var rgbAllocation: Allocation
    private lateinit var cropAllocation: Allocation
    private lateinit var rotateAllocation: Allocation
    private lateinit var bitmapBuffer: Bitmap
    private lateinit var cropBitmap: Bitmap
    private lateinit var scriptcRgbCrop: ScriptC_rgb_crop
    private lateinit var scriptcRgbRotate: ScriptC_rgb_rotate
    private lateinit var scriptcYuv420888ToRgb: ScriptC_yuv420_888_to_rgb
    private lateinit var yType: Type.Builder
    private lateinit var uvType: Type.Builder
    private lateinit var cropType: Type.Builder
    private lateinit var rgbType: Type.Builder
    private lateinit var rotateType: Type.Builder

    class Builder{
        private lateinit var rs: RenderScript
        private lateinit var size: Size
        private var rect: RectF? = null
        private var rotateDegree = 0
        private lateinit var image: Image

        fun setRS(rs: RenderScript) = apply { this.rs = rs }
        fun setImage(image: Image) = apply { this.image = image }
        fun setSize(size: Size) = apply { this.size = size }
        fun setRect(rect: RectF) = apply { this.rect = rect }
        fun setRotateDegree(rotateDegree: Int) = apply { this.rotateDegree = rotateDegree }
        fun build() = ImageProcessor(rs, size, rect, image, rotateDegree)
    }

    fun process(): Bitmap{
        val planes = image.planes
        var buffer = planes[0].buffer
        val y = ByteArray(buffer.remaining())
        buffer.get(y)
        buffer = planes[1].buffer
        val u = ByteArray(buffer.remaining())
        buffer.get(u)
        buffer = planes[2].buffer
        val v = ByteArray(buffer.remaining())
        buffer.get(v)
        val uvRowStride = planes[1].rowStride
        val uvPixelStride = planes[1].pixelStride
        val yRowStride = planes[0].rowStride
        var rotatedWidth: Int = image.width
        var rotatedHeight: Int = image.height
        val degree = (rotateDegree + 360) % 360
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
        createRSResource(yRowStride, u.size, image, rotatedWidth, rotatedHeight)
        yAllocation.copyFrom(y)
        uAllocation.copyFrom(u)
        vAllocation.copyFrom(v)
        scriptcYuv420888ToRgb._uvPixelStride = uvPixelStride.toLong()
        scriptcYuv420888ToRgb._uvRowStride = uvRowStride.toLong()
        scriptcYuv420888ToRgb._yAllocation = yAllocation
        scriptcYuv420888ToRgb._uAllocation = uAllocation
        scriptcYuv420888ToRgb._vAllocation = vAllocation
        val lo = Script.LaunchOptions()
        lo.setX(0, image.width)
        lo.setY(0, image.height)
        scriptcYuv420888ToRgb.forEach_yuvToRGB(rgbAllocation,lo)
        scriptcRgbRotate._inImage = rgbAllocation
        scriptcRgbRotate._inHeight = image.height
        scriptcRgbRotate._inWidth = image.width
        when(degree){
            90 -> scriptcRgbRotate.forEach_rotate_90_clockwise(rotateAllocation)
            180 -> scriptcRgbRotate.forEach_rotate_180(rotateAllocation)
            270 -> scriptcRgbRotate.forEach_rotate_270_clockwise(rotateAllocation)
        }
        rect?.let {
            val test = transformRect(it.toRect(), rotatedWidth, rotatedHeight)
            scriptcRgbCrop._input = rotateAllocation
            scriptcRgbCrop._xStart = test.left.toLong()
            scriptcRgbCrop._yStart = test.top.toLong()
            scriptcRgbCrop.forEach_crop(cropAllocation)
            cropAllocation.copyTo(cropBitmap)
            return@process cropBitmap
        }
        bitmapBuffer = Bitmap.createBitmap(rotatedWidth, rotatedHeight, Bitmap.Config.ARGB_8888)
        rotateAllocation.copyTo(bitmapBuffer)

        return bitmapBuffer

    }

    private fun createRSResource(yRowStride: Int, uSize: Int, image: Image, rotatedWidth: Int, rotatedHeight: Int) {
        this.scriptcRgbCrop = ScriptC_rgb_crop(rs)
        scriptcRgbRotate = ScriptC_rgb_rotate(rs)
        scriptcYuv420888ToRgb = ScriptC_yuv420_888_to_rgb(rs)
        yType = Type.Builder(rs, Element.U8(rs)).setX(yRowStride).setY(image.height)
        uvType = Type.Builder(rs, Element.U8(rs)).setX(uSize)
        yAllocation = Allocation.createTyped(rs, yType.create())
        uAllocation = Allocation.createTyped(rs, uvType.create())
        vAllocation = Allocation.createTyped(rs, uvType.create())
        rgbType = Type.Builder(rs, Element.RGBA_8888(rs))
        rgbType.setX(image.width)
        rgbType.setY(image.height)
        rotateType = Type.Builder(rs, Element.RGBA_8888(rs))
        rotateType.setX(rotatedWidth)
        rotateType.setY(rotatedHeight)
        rgbAllocation = Allocation.createTyped(rs, rgbType.create())
        rotateAllocation = Allocation.createTyped(rs, rotateType.create())
        rect?.let {
            val test = transformRect(it.toRect(), rotatedWidth, rotatedHeight)
            cropType = Type.Builder(rs, Element.RGBA_8888(rs)).setX(test.width()).setY(test.height())
            cropAllocation = Allocation.createTyped(rs, cropType.create())
            cropBitmap = Bitmap.createBitmap(test.width(), test.height(), Bitmap.Config.ARGB_8888)
        }
        bitmapBuffer = Bitmap.createBitmap(rotatedWidth, rotatedHeight, Bitmap.Config.ARGB_8888)

    }
    private fun transformRect(rect: Rect, width: Int, height: Int): Rect{
        fun transformX(x: Int): Int{
            return x.times(width).div(size.width)
        }
        fun transformY(y: Int): Int{
            return y.times(height).div(size.height)
        }
        return Rect().apply {
            left = transformX(rect.left)
            top = transformY(rect.top)
            right = transformX(rect.right)
            bottom = transformY(rect.bottom)
        }
    }

}