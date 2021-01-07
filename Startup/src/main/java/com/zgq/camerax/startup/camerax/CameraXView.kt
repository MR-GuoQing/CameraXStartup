package com.zgq.camerax.startup.camerax

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.widget.FrameLayout
import androidx.annotation.RequiresPermission
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.zgq.camerax.startup.R
import com.zgq.camerax.startup.barcodereader.BarcodeScannerProcessor
import kotlinx.android.synthetic.main.camerax_preview.view.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraXView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraExecutor: ExecutorService
    private var cameraSelectorOption = CameraSelector.LENS_FACING_BACK
    private var cameraProvider: ProcessCameraProvider? = null
    var imageAnalyzer: ImageAnalysis? = null
    val graphicOverlay: GraphicOverlay

    init {
        LayoutInflater.from(context).inflate(R.layout.camerax_preview, this)
        cameraExecutor = Executors.newSingleThreadExecutor()
        graphicOverlay = GraphicOverlay(context, attrs)
        addView(graphicOverlay)
    }

    @SuppressLint("SupportAnnotationUsage")
    fun startCamera(lifecycleOwner: LifecycleOwner, type: AnalysisType) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                val ratio = aspectRatio(camerx_preview.width, camerx_preview.height)
                val size = sizeList[ratio]
                cameraProvider = cameraProviderFuture.get()
                preview = Preview.Builder()
                    .setTargetResolution(size!!)
                    .build()

                imageAnalyzer = ImageAnalysis.Builder()
                    .setTargetResolution(size!!)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, selectAnalyzer(type))
                    }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraSelectorOption)
                    .build()

                setupZoom()
                setCameraConfig(lifecycleOwner, cameraProvider, cameraSelector)

            }, ContextCompat.getMainExecutor(context)
        )

    }

    private fun setupZoom() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio: Float = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1F
                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }
        val scaleGestureDetector = ScaleGestureDetector(context, listener)
        camerx_preview.setOnTouchListener { _, event ->
            camerx_preview.post {
                scaleGestureDetector.onTouchEvent(event)
            }
            return@setOnTouchListener true
        }
    }

    private fun setCameraConfig(
        lifecycleOwner: LifecycleOwner,
        cameraProvider: ProcessCameraProvider?,
        cameraSelector: CameraSelector
    ) {
        try {
            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
            preview?.setSurfaceProvider(
                camerx_preview.surfaceProvider
            )
            post {
                graphicOverlay.size = Size(camerx_preview.width, camerx_preview.height)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    private fun selectAnalyzer(type: AnalysisType): ImageAnalysis.Analyzer {
        when (type) {
            is AnalysisType.BARCODE -> return BarcodeScannerProcessor(graphicOverlay, type.isMultiMode)
            else -> error("Not supported analysis type")
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    companion object {
        const val TAG = "CameraFrameView"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private val sizeList = mapOf(AspectRatio.RATIO_16_9 to Size(1080, 1920), AspectRatio.RATIO_4_3 to Size(1200, 1600))
    }
}