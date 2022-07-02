package com.greencross.lims.report.builder

import com.greencross.lims.report.Resource
import com.greencross.lims.report.Template
import java.time.LocalDate
import java.time.LocalDateTime

interface AbstractReportTemplate<R: Resource>: Template<R> {
    fun date(date: LocalDate?): String?
    fun date(date: LocalDateTime?): String?
    fun age(birth: LocalDate?, sampling: LocalDate?): String
    fun sex(sex: Sex?): String
    fun lblMedicalInstitution(): String
    fun lblMedicalRecordNumber(): String
    fun lblRequestNumber(): String
    fun lblPatientName(): String
    fun lblAgeSex(): String
    fun lblSpecimenTypeDate(): String
    fun lblSpecimenType(): String
    fun lblSpecimenDate(): String
    fun lblPatientInfo(): String
    fun lblReceiptReportDate(): String
    fun lblInspector(): String
    fun lblChecker(): String
}
