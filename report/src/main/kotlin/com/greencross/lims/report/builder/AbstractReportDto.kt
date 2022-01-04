package com.greencross.lims.report.builder

import com.greencross.lims.report.Dto
import java.time.LocalDate

abstract class AbstractReportDto: Dto {
    var medicalInstitution: String? = null
    var medicalRecordNumber: String? = null
    var requestNumber: String? = null
    var patientNumber: String? = null
    var patientName: String? = null
    var birthDate: LocalDate? = null
    var sex: Sex? = null
}