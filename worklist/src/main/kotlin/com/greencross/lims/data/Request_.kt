package com.greencross.lims.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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
    //patient
    val customerName: String? = "",
    val mrn: String? = "",
    val name: String? = "",
    val code: String? = "",
    val sex: String? = "",
    //request
    val serviceName: String? = ""
)