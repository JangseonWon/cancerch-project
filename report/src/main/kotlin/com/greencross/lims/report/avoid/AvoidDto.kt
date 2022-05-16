package com.greencross.lims.report.avoid

import com.greencross.lims.report.HasServiceCode
import com.greencross.lims.report.builder.AbstractReportDto

data class AvoidDto(
    override var barcode: String?,
    val result: Results,
    val first: Cancer,
    val second: Cancer
): AbstractReportDto(), HasServiceCode {


    override fun code(): String? {
        return barcode
    }
    enum class Results{
        저위험, 고위험, 기타암종
    }
    data class Cancer(
        val name: String,
        val morbidity: Double,
        val score: Double? = 0.0
    )
}