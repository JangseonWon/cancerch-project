package com.gcgenome.lims.projection

import java.time.LocalDate
import java.time.LocalDateTime

data class Request(
    val institutionName: String?,
    val institution2Name: String?,
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
    val sampleType: String?,
    val dateRequest: LocalDate?,
    val dateReception: LocalDateTime?,
    val dateSampling: LocalDate?,
    val dateDuePublish: LocalDate?,
    val age: Int?,
    val remark: String?,
    val service: String
) {
    companion object {
        data class RequestBuilder(
            val institutionName: String?,
            val institution2Name: String?,
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
            val sampleType: String?,
            val dateRequest: LocalDate?,
            val dateReception: LocalDateTime?,
            val dateSampling: LocalDate?,
            val dateDuePublish: LocalDate?,
            val age: Int?,
            val remark: String?,
            val service: String
        ) {
            fun build(): Request {
                return Request(
                    institutionName,
                    institution2Name,
                    departmentName,
                    wardName,
                    serviceName,
                    patientName,
                    sex,
                    birth,
                    mrn,
                    info,
                    institution,
                    institution2,
                    physician,
                    sample,
                    sampleType,
                    dateRequest,
                    dateReception,
                    dateSampling,
                    dateDuePublish,
                    age,
                    remark,
                    service)
            }
        }
    }
}
