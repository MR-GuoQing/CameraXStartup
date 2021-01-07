package com.zgq.camerax.startup.camerax

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage

abstract class BaseImageAnalyzer<T> : ImageAnalysis.Analyzer {
    abstract val graphicOverlay: GraphicOverlay

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let {
            processImage(
                InputImage.fromMediaImage(
                    it,
                    imageProxy.imageInfo.rotationDegrees
                )
            ).addOnSuccessListener { result ->
                onSuccess(result, graphicOverlay, mediaImage.cropRect)
            }.addOnFailureListener { e ->
                onFailure(e)
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }

    protected abstract fun processImage(image: InputImage): Task<T>
    protected abstract fun onSuccess(
        results: T,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    )

    protected abstract fun onFailure(e: Exception)
}