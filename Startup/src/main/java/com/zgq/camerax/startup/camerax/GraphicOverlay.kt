package com.zgq.camerax.startup.camerax

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Size
import android.view.View

class GraphicOverlay constructor(context: Context,
                                          attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val obj = Any()
    private val graphics: MutableList<Graphic> = ArrayList()
    var size: Size = Size(this.width, this.height)
    set(value) {
        field = value
        postInvalidate()
    }

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

        fun postInvalidate() {
            overlay.postInvalidate()
        }
    }
}