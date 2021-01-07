package com.zgq.camerax.startup.barcodereader

import android.graphics.Rect
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.zgq.camerax.startup.camerax.BaseImageAnalyzer
import com.zgq.camerax.startup.camerax.GraphicOverlay

class BarcodeScannerProcessor constructor(private val view: GraphicOverlay, isMultiMode: Boolean): BaseImageAnalyzer<List<Barcode>>(){

    override val graphicOverlay: GraphicOverlay
        get() = view
    private val barcodeScannerOptions: BarcodeScannerOptions = BarcodeScannerOptions.Builder().build()
    private val barcodeScanner: BarcodeScanner =  BarcodeScanning.getClient(barcodeScannerOptions)
    init {
        if (!isMultiMode){
            graphicOverlay.add(BarcodeFrameOverlay(graphicOverlay))
        }
        graphicOverlay.postInvalidate()
    }

    override fun processImage(image: InputImage): Task<List<Barcode>> {
        return barcodeScanner.process(image)
    }

    override fun onSuccess(results: List<Barcode>, graphicOverlay: GraphicOverlay, rect: Rect) {

    }

    override fun onFailure(e: Exception) {
    }
}