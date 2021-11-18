package com.greencross.lims.data

import java.time.LocalDateTime

data class Request_ (
    val sample: Double,
    val info: String? = "",
    val service: String? = "",
    val dateRequest: LocalDateTime? = null,
    val dateEnd: LocalDateTime? = null,
    //sample
    val type: String? = "",
    val remark: String? = "",
    val patient: String? = "",
    val barcode: Double? = 0.0,
    //patient
    val customerName: String? = "",
    val customerCode: String? = "",
    val mrn: String? = "",
    val name: String? = "",
    val code: String? = "",
    val sex: String? = "",
    //request
    val serviceName: String? = ""
)