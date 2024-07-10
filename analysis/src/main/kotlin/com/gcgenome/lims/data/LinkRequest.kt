package com.gcgenome.lims.data

data class LinkRequest(
    val originSample: Long,
    val originService: String,
    val batch: String,
    val row: Int
) {
    val linkSample : Long? = null
    val linkService: String? = null
}
