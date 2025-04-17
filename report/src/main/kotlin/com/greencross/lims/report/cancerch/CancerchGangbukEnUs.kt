package com.greencross.lims.report.cancerch

import com.greencross.lims.report.SectionBarcode
import com.greencross.lims.report.cancerch.GangbukComponent.*
import com.gcgenome.lims.report.func.Painter

class CancerchGangbukEnUs(
    val template: CancerchTemplateON256<CancerchResource>,
    val dto: CancerchDto,
    val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto>,
    val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto>,
    val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>
) : CancerchPageBuilder<CancerchTemplateON256<CancerchResource>>(template, dto)  {
    val barcode: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionBarcode()
    private val simpleTitle: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionSimpleTitleEnUs()
    private val title: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionTitleEnUs()
    private val intro: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionIntroEnUs()
    private val totalResult: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionTotalResultEnUs()
    private val predictCancer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionPredictCancerEnUs()
    private val analysis: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionAnalysisEnUs()
    private val cancerTypeDanger: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionCancerTypeDangerEnUs()
    private val analysisComment: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionAnalysisCommentEnUs()
    private val guideLine: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionGuideLineEnUs()
    private val limitation: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = GangbukSectionLimitationEnUs()

    override fun template(): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
        return title.and { stream, template, dto ->
            stream.setFont(template.resource().fontDefault(), 11f)
            return@and stream
        }.and(sign).and(footer)
    }
    private fun templateWithSimpleTitle(): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
        return simpleTitle.and { stream, template, dto ->
            stream.setFont(template.resource().fontDefault(), 11f)
            return@and stream
        }.and(sign).and(footer)
    }

    override fun pages(): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
        return template().and(intro).and(totalResult).and(predictCancer).and(analysis)
            .and{ s, t, d -> newPage(s) }.and(templateWithSimpleTitle()).and(cancerTypeDanger).and(analysisComment).and(guideLine)
            .and{ s, t, d -> newPage(s) }.and(templateWithSimpleTitle()).and(limitation).and(page)
    }
}
