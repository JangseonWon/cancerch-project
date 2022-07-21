package com.gcgenome.lims.data

import com.gcgenome.lims.entity.Worklist

data class Worklist(val id: String?) {
    var title: String? = ""
    var createdAt: String? = ""
    var status: Worklist.Companion.Status? = null
    var remark: String? = ""
}