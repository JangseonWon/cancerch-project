package com.gcgenome.lims.projection

import java.time.LocalDate

data class Request(
    val institutionName: String?,
    val departmentName: String?,
    val wardName: String?,
    val serviceName: String,
    val patientName: String,
    val sex: String,
    val birth: LocalDate?,
    val mrn: String?,
    val info: String?,
    val institution: String?,
    val institution2: String?,
    val physician: String?,
    val sample: Long,
    val service: String
) {
    companion object {
        data class RequestBuilder(
            val institutionName: String?,
            val departmentName: String?,
            val wardName: String?,
            val serviceName: String,
            val patientName: String,
            val sex: String,
            val birth: LocalDate?,
            val mrn: String?,
            val info: String?,
            val institution: String?,
            val institution2: String?,
            val physician: String?,
            val sample: Long,
            val service: String
        ) {
            fun build(): Request {
                return Request(institutionName, departmentName, wardName, serviceName, patientName, sex, birth, mrn, info, institution, institution2, physician, sample, service)
            }
        }
    }
}
