package com.zgq.camerax.startup.camerax

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.renderscript.RenderScript
import com.google.android.gms.tasks.Task

abstract class BaseImageAnalyzer<T> constructor(context: Context) : ImageAnalysis.Analyzer {
    abstract val graphicOverlay: GraphicOverlay
    val rs: RenderScript = RenderScript.create(context)
    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val image = imageProxy.image?: return
            processImage(image, imageProxy.imageInfo.rotationDegrees).addOnSuccessListener {result ->
                onSuccess(result)
            }.addOnFailureListener{e ->
                onFailure(e)
            }.addOnCompleteListener {
                imageProxy.close()
            }

    }

    protected abstract fun processImage(image: Image, rotateDegree: Int): Task<T>
    protected abstract fun onSuccess(results: T)
    protected abstract fun onFailure(e: Exception)
}