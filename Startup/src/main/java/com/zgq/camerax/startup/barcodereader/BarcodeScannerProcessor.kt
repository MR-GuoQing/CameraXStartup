package com.zgq.camerax.startup.barcodereader

import android.content.Context
import android.media.Image
import android.util.Size
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.zgq.camerax.startup.camerax.AnalysisType
import com.zgq.camerax.startup.camerax.BaseImageAnalyzer
import com.zgq.camerax.startup.camerax.GraphicOverlay
import com.zgq.camerax.startup.util.ImageProcessor

class BarcodeScannerProcessor constructor(context: Context, private val view: GraphicOverlay, private val barcodeType: AnalysisType.BARCODE): BaseImageAnalyzer<List<Barcode>>(context){

    override val graphicOverlay: GraphicOverlay
        get() = view
    private val barcodeScannerOptions: BarcodeScannerOptions = BarcodeScannerOptions.Builder().build()
    private val barcodeScanner: BarcodeScanner =  BarcodeScanning.getClient(barcodeScannerOptions)
    private val barcodeFrameOverlay: BarcodeFrameOverlay by lazy {
        BarcodeFrameOverlay(graphicOverlay)
    }
    init {
        if (!barcodeType.isMultiMode){
            graphicOverlay.add(barcodeFrameOverlay)
        }
        graphicOverlay.postInvalidate()
    }

    override fun processImage(image: Image, rotateDegree: Int): Task<List<Barcode>> {

        val bitmap = ImageProcessor.Builder()
                .setRS(rs)
                .setImage(image)
                .setRect(barcodeFrameOverlay.frameRectF)
                .setSize(Size(view.width, view.height))
                .setRotateDegree(rotateDegree).build()
                .process()
        return if(barcodeType.isMultiMode) {
            barcodeScanner.process(InputImage.fromMediaImage(image, rotateDegree))
        }else{
            barcodeScanner.process(InputImage.fromBitmap(bitmap, 0))
        }
    }

    override fun onSuccess(results: List<Barcode>) {
       val listBarcode = results.map {
            it.rawValue!!
        }
        barcodeType.callback.onSuccess(listBarcode)

    }

    override fun onFailure(e: Exception) {
        barcodeType.callback.onFailed(e)
    }
}