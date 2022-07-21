package com.gcgenome.lims.data

import com.gcgenome.lims.entity.Worklist

data class Worklist (
    val id: String,
    val title: String,
    val createdAt: String,
    val status: Worklist.Companion.Status?,
    val prefix: String?,
    val idx: Int?,
    val serial: String?
) {
    var remark: String? = null
}