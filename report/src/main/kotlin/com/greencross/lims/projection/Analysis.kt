package com.greencross.lims.projection

import com.greencross.lims.entity.readonly.Patient
import java.time.LocalDate
import java.time.LocalDateTime

data class Analysis (
    val sample: Long,
    val service: String,
    val batch: String,
    val row: Long,
    val dateRequest: LocalDateTime,
    val dateSampling: LocalDateTime,
    val dateDue: LocalDateTime,
    val sampleType: String,
    val remark: String?,
    val ward: String?,
    val code: String?,
    val value: String?,
    val patient: Patient,
    val barcode: Long,
    val result: String,
    val too5Pred: String,
    val too6Pred: String,
    val comment: String?,
    val iscore: Double?,
    val femsCovBc: Double?,
    val femsBc: Double?,
    val covBc: Double?,
    val femsCovBernn: Double?,
    val femsPath: String?,
    val iscorePath: String?,
    val language: String?,
    val clinicalCancer: String?,
    val cfDnaConcentration: Double?
) {
    companion object {
        data class AnalysisBuilder(
            val sample: Long,
            val service: String,
            val batch: String,
            val row: Long,
            val dateRequest: LocalDateTime,
            val dateSampling: LocalDateTime,
            val dateDue: LocalDateTime,
            val sampleType: String,
            val barcode: Long,
            val remark: String?,
            val ward: String?,
            val code: String?,
            val value: String?,
            val patient: String,
            val patientName: String,
            val sex: String,
            val birth: LocalDate,
            val customerName: String,
            val customerName2: String?,
            val mrn: String?,
            val result: String,
            val too5Pred: String,
            val too6Pred: String,
            val comment: String?,
            val iscore: Double?,
            val femsCovBc: Double?,
            val femsBc: Double?,
            val covBc: Double?,
            val femsCovBernn: Double?,
            val femsPath: String?,
            val iscorePath: String?,
            val language: String?,
            val clinicalCancer: String?,
            val cfDnaConcentration: Double?
        ){
            fun build() = Analysis(sample, service, batch, row, dateRequest, dateSampling, dateDue, sampleType, remark, ward, code, value, Patient(patient, patientName, sex, birth, customerName, customerName2, mrn), barcode, result, too5Pred, too6Pred, comment, iscore, femsCovBc, femsBc, covBc, femsCovBernn, femsPath, iscorePath, language, clinicalCancer, cfDnaConcentration)
        }
    }
}
