package com.zgq.camerax.startup.camerax

import com.google.mlkit.vision.barcode.Barcode
import kotlin.Exception

sealed class AnalysisType {

    class BARCODE(val isMultiMode: Boolean, val callback: BarcodeResultCallback) : AnalysisType()
    object OCR : AnalysisType()
    object FACE : AnalysisType()
    object OBJECT : AnalysisType()

    interface ResultCallback<in T> {
        fun onSuccess(result: T)
        fun onFailed(e: Exception)
    }

    interface BarcodeResultCallback:ResultCallback<List<String>>
}
