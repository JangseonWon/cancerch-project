package com.greencross.lims.data

data class Report(
    val sample: Long,
    val service: String,
    val createdAt: String
) {
    var createBy:      String = ""
    var name:          String = ""
}