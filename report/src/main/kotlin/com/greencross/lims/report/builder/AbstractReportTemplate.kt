package com.greencross.lims.report.builder

import com.greencross.lims.report.Resource
import com.greencross.lims.report.Template
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import java.time.LocalDate
import java.time.LocalDateTime

interface AbstractReportTemplate<R: Resource>: Template<R> {
    fun lblMedicalInstitution(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblMedicalRecordNumber(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblRequestNumber(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblPatientName(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblAgeSex(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblSpecimenType(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblSpecimenDate(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblReceiptReportDate(stream: PDPageContentStreamPageAccessible, y: Float)
}
