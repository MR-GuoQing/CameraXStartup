package com.zgq.camerax.startup.barcodereader

import android.graphics.*
import android.util.TypedValue
import com.zgq.camerax.startup.R
import com.zgq.camerax.startup.camerax.GraphicOverlay
import kotlin.math.min

class BarcodeFrameOverlay(overlay: GraphicOverlay) : GraphicOverlay.Graphic(overlay) {
    
    // Paint
    private lateinit var paint: Paint 
    private val maskColor = overlay.context.getColor(R.color.barcode_frame_mask)
    private val frameCornerLength: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FRAME_CORNER_LENGTH, overlay.resources.displayMetrics)
    private val frameCornerRadius: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FRAME_CORNER_RADIUS, overlay.resources.displayMetrics)
    private val frameCornerColor: Int = overlay.context.getColor(R.color.white)
    private val frameCornerLineWidth:Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FRAME_CORNER_LINE_WIDTH, overlay.resources.displayMetrics)
    private val frameRectF by lazy {
        val frameLength = min(overlay.size.width, overlay.size.height).times(FRAME_RATIO)
        val leftOffset = (overlay.size.width - frameLength).div(2)
        val topOffset = overlay.size.height.times(DEFAULT_FRAME_CONTAINER_TOP_RATIO) - frameLength.div(2)
        RectF(leftOffset, topOffset, leftOffset + frameLength, (topOffset + frameLength))
    }
    override fun draw(canvas: Canvas?) {
        drawMask(canvas)
        drawFrameRect(canvas)
        drawFrameContainer(canvas)
    }

    private fun drawFrameRect(canvas: Canvas?) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            style = Paint.Style.FILL
        }
        canvas?.drawRect(frameRectF, paint)
    }

    private fun drawMask(canvas: Canvas?) {
        paint = Paint().apply {
            color = maskColor
        }
        canvas?.drawPaint(paint)
    }

    private fun drawFrameContainer(canvas: Canvas?) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = frameCornerColor
            style = Paint.Style.STROKE
            strokeWidth = frameCornerLineWidth
            isAntiAlias = true
            pathEffect = CornerPathEffect(frameCornerRadius)
        }

        val centerOfCornerLine = frameCornerLineWidth.div(2)
        val x1 = frameRectF.left.minus(centerOfCornerLine)
        val x2 = frameRectF.right.plus(centerOfCornerLine)
        val y1 = frameRectF.top.minus(centerOfCornerLine)
        val y2 = frameRectF.bottom.plus(centerOfCornerLine)

        //top left corner
        val path = Path()
        path.moveTo(x1, y1.plus(frameCornerLength))
        path.lineTo(x1, y1)
        path.lineTo(x1.plus(frameCornerLength), y1)
        canvas?.drawPath(path, paint)
        //top right corner
        path.moveTo(x2, y1.plus(frameCornerLength))
        path.lineTo(x2, y1)
        path.lineTo(x2.minus(frameCornerLength), y1)
        canvas?.drawPath(path, paint)
        //bottom left corner
        path.moveTo(x1, y2.minus(frameCornerLength))
        path.lineTo(x1, y2)
        path.lineTo(x1.plus(frameCornerLength), y2)
        canvas?.drawPath(path, paint)
        //bottom right corner
        path.moveTo(x2, y2.minus(frameCornerLength))
        path.lineTo(x2, y2)
        path.lineTo(x2.minus(frameCornerLength), y2)
        canvas?.drawPath(path, paint)
    }


    companion object{
        private const val DEFAULT_FRAME_CONTAINER_TOP_RATIO = 0.45f
        private const val FRAME_RATIO = 0.55f
        private const val DEFAULT_FRAME_CORNER_LINE_WIDTH = 4f
        private const val DEFAULT_FRAME_CORNER_RADIUS = 5f
        private const val DEFAULT_FRAME_CORNER_LENGTH = 50f
    }
}