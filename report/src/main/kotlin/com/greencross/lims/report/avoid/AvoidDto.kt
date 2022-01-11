package com.greencross.lims.report.avoid

import com.greencross.lims.report.HasServiceCode
import com.greencross.lims.report.builder.AbstractReportDto

data class AvoidDto(
    override var barcode: String?
): AbstractReportDto(), HasServiceCode {
    var result:         String? = ""    //결과(저위험, 고위험, 기타암종)
    var firstCan:       String? = ""    //1순위암 명
    var firstCanPer:    Double? = 0.0   //1순위암 확률
    var firstCanNum:    Double? = 0.0   //수진자 대역 대 1순위암 유병량
    var secondCan:      String? = ""    //2순위암 명
    var secondCanPer:   Double? = 0.0   //2순위암 확률
    var secondCanNum:   Double? = 0.0   //수진자 대역 대 2순위암 유병량

    override fun code(): String? {
        return barcode
    }
    data class Cancer(
        val name: String,
        val morbidity: Double,
        val score: Double? = 0.0
    )
}