package com.greencross.lims.report.avoid

import com.greencross.lims.report.func.Page
import com.greencross.lims.report.func.PageBuilder
import com.greencross.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.PDDocument

abstract class AvoidPageBuilder<T : AvoidTemplate<AvoidResource>>(
    template: T,
    dto: AvoidDto
) :
    Page<T>(template) {
    val builder: PageBuilder<AvoidTemplate<AvoidResource>, AvoidDto> = PageBuilder(template, dto)

    abstract fun template(): Painter<AvoidTemplate<AvoidResource>, AvoidDto>
    abstract fun pages(): Painter<AvoidTemplate<AvoidResource>, AvoidDto>

    fun build(): PDDocument {
        return builder.add(pages()).build()
    }



}