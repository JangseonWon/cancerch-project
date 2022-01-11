package com.greencross.lims.report.avoid

import com.greencross.lims.report.SectionBarcode
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter

class AvoidN201(
    val template: AvoidTemplateN201<AvoidResource>,
    val dto: AvoidDto,
    header: Painter<out AvoidTemplate<out AvoidResource>, AvoidDto>,
    val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto>,
    val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto>,
    val page: Painter<AvoidTemplate<AvoidResource>, AvoidDto>
) : AvoidPageBuilder<AvoidTemplate<AvoidResource>>(template, dto) {
    val barcode: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionBarcode()
    private val title: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionTitle(763f)
    private val header: Painter<AvoidTemplate<AvoidResource>, AvoidDto>
    override fun template(): Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
        return title.and { stream, template, dto ->
            stream.font(template.resource().fontDefault())
            return@and stream
        }.and(header)
    }
    init{
        this.header = header as Painter<AvoidTemplate<AvoidResource>, AvoidDto>
    }
    override fun pages(): Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
        TODO("Not yet implemented")
    }
}