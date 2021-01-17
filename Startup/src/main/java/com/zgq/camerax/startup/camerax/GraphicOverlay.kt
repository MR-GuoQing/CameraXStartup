package com.zgq.camerax.startup.camerax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class GraphicOverlay constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val obj = Any()
    private val graphics: MutableList<Graphic> = ArrayList()

    fun clear() {
        synchronized(obj) {
            graphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic: Graphic) {
        synchronized(obj) {
            graphics.add(graphic)
        }

    }

    fun remove(graphic: Graphic) {
        synchronized(obj) {
            graphics.remove(graphic)
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(obj) {
            for (graphic in graphics) {
                graphic.draw(canvas)
            }
        }
    }

    abstract class Graphic(private val overlay: GraphicOverlay) {

        abstract fun draw(canvas: Canvas?)

        fun transformRect(width: Int, height: Int, cropRect: Rect): Rect {
            fun calculateScale(): Float {
                val scaleX = overlay.width.toFloat() / width
                val scaleY = overlay.height.toFloat() / height
                return scaleX.coerceAtLeast(scaleY)

            }

            fun transformX(x: Int): Int {
                val offsetX = overlay.width.minus(width.times(calculateScale())).div(2)
                return x.times(calculateScale()).plus(offsetX).toInt()
            }

            fun transformY(y: Int): Int {
                val offsetY = overlay.height.minus(height.times(calculateScale())).div(2)
                return y.times(calculateScale()).plus(offsetY).toInt()
            }

            return Rect().apply {
                top = transformY(cropRect.top)
                left = transformX(cropRect.left)
                bottom = transformY(cropRect.bottom)
                right = transformX(cropRect.right)
            }
        }

        fun postInvalidate() {
            overlay.postInvalidate()
        }
    }
}