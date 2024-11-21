package com.greencross.lims.report.cancerch

import com.greencross.lims.report.SectionBarcode
import com.gcgenome.lims.report.func.Painter

class CancerchON203(
    val template: CancerchTemplateON203<CancerchResource>,
    val dto: CancerchDto,
    val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto>,
    val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto>,
    val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>
) : CancerchPageBuilder<CancerchTemplateON203<CancerchResource>>(template, dto)  {
    val barcode: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionBarcode()
    private val title: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionTitle2()
    private val intro: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionIntro2()
    private val totalResult: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionTotalResult2()
    private val predictCancer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionPredictCancer2()
    private val analysis: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionAnalysis2()
    private val cancerTypeDanger: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionCancerTypeDanger2()
    private val analysisComment: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionAnalysisComment2()
    private val guideLine: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionGuideLine2()
    private val limitation: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionLimitation2()

    override fun template(): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
        return title.and { stream, template, dto ->
            stream.setFont(template.resource().fontDefault(), 11f)
            return@and stream
        }.and(sign).and(footer)
    }
    private fun templateWithoutTitle(): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
        return sign.and { stream, template, dto ->
            stream.setFont(template.resource().fontDefault(), 11f)
            return@and stream
        }.and(footer)
    }

    override fun pages(): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
        return template().and(intro).and(totalResult).and(predictCancer).and(analysis)
            .and{ s, t, d -> newPage(s) }.and(templateWithoutTitle()).and(cancerTypeDanger).and(analysisComment).and(guideLine)
            .and{ s, t, d -> newPage(s) }.and(templateWithoutTitle()).and(limitation)
    }
}
