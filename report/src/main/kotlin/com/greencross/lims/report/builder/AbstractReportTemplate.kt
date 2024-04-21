package com.greencross.lims.report.builder

import com.greencross.lims.report.Resource
import com.greencross.lims.report.Template
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import java.time.LocalDate
import java.time.LocalDateTime

interface AbstractReportTemplate<R: Resource>: Template<R> {
}
