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

    //수진자 특성 대역 유병률
    var lungPPV: Double? = 0.0          //폐암
    var clrtPPV: Double? = 0.0          //대장암
    var livrPPV: Double? = 0.0          //간암
    var pnctPPV: Double? = 0.0          //췌장암
    var thrtPPV: Double? = 0.0          //식도암
    var brstPPV: Double? = 0.0          //유방암
    var ovrnPPV: Double? = 0.0          //난소암
    var etcsPPV: Double? = 0.0          //기타암종

    //수진자 특성 대역 평균 유병률
    var avgLungPPV: Double? = 0.0
    var avgClrtPPV: Double? = 0.0
    var avgLivrPPV: Double? = 0.0
    var avgPnctPPV: Double? = 0.0
    var avgThrtPPV: Double? = 0.0
    var avgBrstPPV: Double? = 0.0
    var avgOvrnPPV: Double? = 0.0
    var avgEtcsPPV: Double? = 0.0

    override fun code(): String? {
        return barcode
    }
}