package com.zgq.camerax.startup.camerax

sealed class AnalysisType {
    data class BARCODE(val isMultiMode: Boolean):AnalysisType()
    object OCR:AnalysisType()
    object FACE:AnalysisType()
    object OBJECT:AnalysisType()
}