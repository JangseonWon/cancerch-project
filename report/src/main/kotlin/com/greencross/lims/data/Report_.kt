package com.greencross.lims.data

data class Report_(
    val sample: Long,
    val service: String,
    val createdAt: String
) {
    var createBy:      String = ""
    var name:          String = ""
}