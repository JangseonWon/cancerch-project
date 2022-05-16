package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.avoid.AvoidTemplate
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.test.avoid.TestInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters

abstract class AvoidTemplateKoKr<R : AvoidResourceKoKr>(
    testInfo: TestInfo
) : AvoidTemplate<R> {
    val DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val lblMedicalInstitution    = "의뢰기관"
    val lblMedicalRecordNumber   = "등록번호"
    val lblRequestNumber         = "접수번호"
    val lblPatientName           = "성명"
    val lblAgeSex                = "나이/성별"
    val lblSpecimenTypeDate      = "검체종류/채취일"
    val lblPatientInfo           = "임상정보/기타"
    val lblReceiptReportDate     = "접수일/보고일"
    val lblInspector             = "검사자 : "
    val lblChecker               = "확인자 : "

    val testInfo: TestInfo

    init{
        this.testInfo = testInfo
    }

    override fun date(date: LocalDate?): String? {
        if(date == null) return null
        return DTF.format(date)
    }

    override fun date(date: LocalDateTime?): String? {
        if(date == null) return null
        return DTF.format(date)
    }

    override fun age(birth: LocalDate?, sampling: LocalDate?): String {
        if(birth == null )      return "-"
        if(sampling == null )   return (Period.between(birth, LocalDate.now().with(TemporalAdjusters.firstDayOfYear())).years+1).toString()
        else                    return (Period.between(birth, sampling.with(TemporalAdjusters.firstDayOfYear())).years+1).toString()
    }

    override fun sex(sex: Sex?): String {
        if(sex == null) return "-"
        return when (sex){
            Sex.M -> "남"
            Sex.F -> "여"
        }
    }

    override fun lblMedicalInstitution(): String {
        return lblMedicalInstitution
    }

    override fun lblMedicalRecordNumber(): String {
        return lblMedicalRecordNumber
    }

    override fun lblRequestNumber(): String {
        return lblRequestNumber
    }

    override fun lblPatientName(): String {
        return lblPatientName
    }

    override fun lblAgeSex(): String {
        return lblAgeSex
    }

    override fun lblSpecimenTypeDate(): String {
        return lblSpecimenTypeDate
    }

    override fun lblPatientInfo(): String {
        return lblPatientInfo
    }

    override fun lblReceiptReportDate(): String {
        return lblReceiptReportDate
    }

    override fun lblInspector(): String {
        return lblInspector
    }

    override fun lblChecker(): String {
        return lblChecker
    }
}