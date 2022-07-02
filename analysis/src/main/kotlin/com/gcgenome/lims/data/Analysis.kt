package com.gcgenome.lims.data

data class Analysis(
    val sample: Long,
    val service: String
) {
    var batch: String? = ""
    var row: Int? = 0
    var request: Request? = null
    var report: Report? = null
    var file: String? = ""
    var value: String? = ""
    var createdAt: String? = ""
    var createdBy: User? = null
    var lastModifyAt: String? = ""
    var lastModifyBy: User? = null
}