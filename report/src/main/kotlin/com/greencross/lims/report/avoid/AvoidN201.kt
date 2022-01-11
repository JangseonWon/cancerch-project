package com.greencross.lims.report.avoid

import com.greencross.lims.report.SectionBarcode
import com.greencross.lims.report.avoid.kokr.AvoidResourceN201KoKr
import com.greencross.lims.report.avoid.kokr.AvoidTemplateN201KoKr
import com.greencross.lims.report.func.Painter

class AvoidN201(
    val template: AvoidTemplateN201<AvoidResource>,
    val dto: AvoidDto,
    val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto>,
    val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto>,
    val page: Painter<AvoidTemplate<AvoidResource>, AvoidDto>
) : AvoidPageBuilder<AvoidTemplateN201<AvoidResource>>(template, dto) {
    val barcode: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionBarcode()
    private val title: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionTitle(763f)
    override fun template(): Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
        return title.and { stream, template, dto ->
            stream.font(template.resource().fontDefault())
            return@and stream
        }
    }

    override fun pages(): Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
        return template().and(barcode)
    }
}