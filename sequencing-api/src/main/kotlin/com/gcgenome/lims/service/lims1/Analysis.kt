package com.gcgenome.lims.service.lims1

import java.util.*

data class Analysis (
    val row: Int,
    val patientId: Long?,
    val code: String?,
    val serial: String,
    val sort: String
) {
    val value: MutableMap<UUID, String> = mutableMapOf()
}