package com.greencross.lims.report.ON206

import com.greencross.lims.report.HasServiceCode
import com.greencross.lims.report.builder.AbstractReportDto
import java.time.LocalDate

class DNACTDto(
    override var barcode: String?,
    var risk: Risk,
    var result: List<SummaryOfResult>,
    val language: String
): AbstractReportDto(), HasServiceCode {
    override fun code(): String? {
        return barcode
    }

    enum class Risk {
        STRONG, MODERATE, WEAK, NOT_DETECTED
    }

    data class SummaryOfResult(
        val date: LocalDate,
        val cancer: String,
        val cfDNAConcentration: Float,
        val genomicInstability: Float,
        val covScore: Float,
        val femsScore: Float
    ) {
        var FEMSPath: String = ""
        var GenomicPath: String = ""
        var cfDNAConcentrationCutOff: Double = 9.625
        var genomicInstabilityCutOff: Double = 4.0
        var covScoreCutOff: Double = 0.423
        var femsScoreCutOff: Double = 0.531
    }
}
