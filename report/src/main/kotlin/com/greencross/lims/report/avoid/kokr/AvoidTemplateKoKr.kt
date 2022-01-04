package com.greencross.lims.report.avoid.kokr

import java.time.format.DateTimeFormatter

abstract class AvoidTemplateKoKr {
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


}