package com.greencross.lims.report.avoid

import com.gcgenome.lims.report.func.Page
import com.gcgenome.lims.report.func.PageBuilder
import com.gcgenome.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.PDDocument

abstract class AvoidPageBuilder<T : AvoidTemplate<in AvoidResource>>(
    template: T,
    dto: AvoidDto
) :
    Page<T>(template) {
    val builder: PageBuilder<AvoidTemplate<AvoidResource>, AvoidDto>

    abstract fun template(): Painter<AvoidTemplate<AvoidResource>, AvoidDto>
    abstract fun pages(): Painter<AvoidTemplate<AvoidResource>, AvoidDto>

    fun build(): PDDocument {
        return builder.add(pages()).build()
    }

    init{
        builder = PageBuilder(template, dto)
    }
}
