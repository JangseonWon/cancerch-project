package com.greencross.lims.projection

import com.greencross.lims.entity.readonly.Patient
import java.time.LocalDate
import java.time.LocalDateTime

data class Analysis (
    val sample: Long,
    val service: String,
    val dateRequest: LocalDateTime,
    val dateSampling: LocalDateTime,
    val dateDue: LocalDateTime,
    val sampleType: String,
    val remark: String?,
    val patient: Patient,
    val barcode: Long,
    val result: String,
    val too5Pred: String,
    val too6Pred: String
) {
    companion object {
        data class AnalysisBuilder(
            val sample: Long,
            val service: String,
            val dateRequest: LocalDateTime,
            val dateSampling: LocalDateTime,
            val dateDue: LocalDateTime,
            val sampleType: String,
            val barcode: Long,
            val remark: String?,
            val patient: String,
            val patientName: String,
            val sex: String,
            val birth: LocalDate,
            val customerName: String,
            val customerName2: String?,
            val mrn: String?,
            val result: String,
            val too5Pred: String,
            val too6Pred: String
        ){
            fun build() = Analysis(sample, service, dateRequest, dateSampling, dateDue, sampleType, remark, Patient(patient, patientName, sex, birth, customerName, customerName2, mrn), barcode, result, too5Pred, too6Pred)
        }
    }
}