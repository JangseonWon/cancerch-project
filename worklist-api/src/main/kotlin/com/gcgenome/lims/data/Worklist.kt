package com.gcgenome.lims.data

import com.gcgenome.lims.entity.Worklist

data class Worklist (
    val id: String = "",
    val title: String = "",
    val createdAt: String = "",
    val status: Worklist.Companion.Status? = null,
    val prefix: String? = null,
    val idx: Int? = null,
    val serial: String? = null
) {
    var remark: String? = null

    companion object{
        data class WorklistBuilder(
            val id: String,
            val title: String,
            val createdAt: String,
            val status: String,
            val prefix: String,
            val idx: Int,
            val serial: String,
            val remark: String? = null
        ) {
            fun build(): com.gcgenome.lims.data.Worklist = Worklist(
                id = id,
                title = title,
                createdAt = createdAt,
                status = Worklist.Companion.Status.valueOf(status),
                prefix = prefix,
                idx = idx,
                serial = serial
            )
        }
    }
}