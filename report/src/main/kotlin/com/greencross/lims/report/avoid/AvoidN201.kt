package com.greencross.lims.report.avoid

import com.greencross.lims.report.SectionBarcode
import com.greencross.lims.report.func.Painter

class AvoidN201(
    val template: AvoidTemplateN201<AvoidResource>,
    val dto: AvoidDto,
    val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto>,
    val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto>,
    val page: Painter<AvoidTemplate<AvoidResource>, AvoidDto>
) : AvoidPageBuilder<AvoidTemplateN201<AvoidResource>>(template, dto) {
    val barcode: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionBarcode()
    private val title: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionTitle()
    private val intro: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionIntro()
    private val totalResult: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionTotalResult()
    private val doubtCancer: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionDoubtCancer()
    private val cancerTypeDanger: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionCancerTypeDanger()
    private val detailResultAnalysis: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionDetailResultAnalysis()
    private val detailResultComment: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionDetailResultComment()
    private val guideLine: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionGuideLine()
    private val detailProcess: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionDetailProcess()
    private val limitation: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionLimitation()
    override fun template(): Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
        return title.and { stream, template, dto ->
            stream.setFont(template.resource().fontDefault(), 11f)
            return@and stream
        }.and(sign).and(footer)
    }

    override fun pages(): Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
        return template().and(intro).and(totalResult).and(doubtCancer).and(cancerTypeDanger).and(barcode)
            .and { s, t, d -> newPage(s) }.and(template()).and(detailResultAnalysis).and(detailResultComment)
            .and(guideLine)
            .and { s, t, d -> newPage(s) }.and(template()).and(detailProcess)
            .and { s, t, d -> newPage(s) }.and(template()).and(limitation)
            .and(page)
    }
}
