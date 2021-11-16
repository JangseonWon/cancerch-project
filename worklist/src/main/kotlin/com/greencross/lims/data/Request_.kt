package com.greencross.lims.data

import java.time.LocalDateTime

data class Request_ (
    val sample: Double,
    val info: String? = null,
    val service: String? = null,
    val dateRequest: LocalDateTime? = null,
    val dateEnd: LocalDateTime? = null,
    //sample
    val type: String? = null,
    val remark: String? = null,
    val patient: String? = null,
    //patient
    val customerName: String? = null,
    val mrn: String? = null,
    val name: String? = null,
    val code: String? = null,
    val sex: String? = null,
    //request
    val serviceName: String? = null
)