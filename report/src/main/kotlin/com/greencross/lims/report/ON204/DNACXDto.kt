package com.greencross.lims.report.ON204

import com.greencross.lims.report.HasServiceCode
import com.greencross.lims.report.builder.AbstractReportDto

class DNACXDto(
    override var barcode: String?,
    val risk: Risk,
    val result: SummaryOfResult,
    val language: String,
) : AbstractReportDto(), HasServiceCode {
    var comment: String = ""
    override fun code(): String? {
        return barcode
    }

    enum class Risk {
        HIGH, MODERATE, MILD, LOW
    }

    data class SummaryOfResult(
        val cancer: String,
        val signalScore: Double,
        val covScore: Double,
        val femsScore: Double,
        val cfDNAConcentration: Double,
        val genomicInstability: Double
    ) {
        var signalScore95CutOff: Double = 0.0
        var signalScore99CutOff: Double = 0.0
        var covScoreCutOff: Double = 0.423
        var femsScoreCutOff: Double = 0.531
        var cfDNAConcentrationCutOff: Double = 9.625
        var genomicInstabilityCutOff: Double = 4.0

    }
}
