package com.greencross.lims.report.avoid

import com.greencross.lims.report.HasServiceCode
import com.greencross.lims.report.builder.AbstractReportDto

data class AvoidDto(
    override var barcode: String?,
    val result: Results,
    val first: Cancer
): AbstractReportDto(), HasServiceCode {
    override fun code(): String? {
        return barcode
    }
    enum class Results{
        GENERAL, CONCERN, RISK
    }
    data class Cancer(
        val name: String = "",
        val ppv: Double = 0.0,
        val asr: Double = 0.0,
        val score: Double? = 0.0
    )
}