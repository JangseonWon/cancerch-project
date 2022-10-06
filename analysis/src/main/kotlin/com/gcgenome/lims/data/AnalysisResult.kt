package com.gcgenome.lims.data

data class AnalysisResult(
    val sample: Long,
    val service: String
    ) {
    var comment: String? = ""
}