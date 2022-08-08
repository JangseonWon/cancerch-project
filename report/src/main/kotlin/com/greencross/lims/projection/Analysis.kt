package com.greencross.lims.projection

import com.greencross.lims.entity.readonly.Patient
import io.r2dbc.postgresql.codec.Json
import java.time.LocalDate
import java.time.LocalDateTime

data class Analysis (
    val sample: Long,
    val service: String,
    val value: String?,
    val dateRequest: LocalDateTime,
    val dateSampling: LocalDateTime,
    val dateDue: LocalDateTime,
    val sampleType: String,
    val patient: Patient,
    val barcode: Long,
    val result: String,
    val cancer: String
) {
    companion object {
        data class AnalysisBuilder(
            val sample: Long,
            val service: String,
            val value: Json?,
            val dateRequest: LocalDateTime,
            val dateSampling: LocalDateTime,
            val dateDue: LocalDateTime,
            val sampleType: String,
            val barcode: Long,
            val patient: String,
            val patientName: String,
            val sex: String,
            val birth: LocalDate,
            val customerName: String,
            val mrn: String?,
            val result: String,
            val cancer: String
        ){
            fun build() = Analysis(sample, service, value?.asString(), dateRequest, dateSampling, dateDue, sampleType, Patient(patient, patientName, sex, birth, customerName, mrn), barcode, result, cancer)
        }
    }
}