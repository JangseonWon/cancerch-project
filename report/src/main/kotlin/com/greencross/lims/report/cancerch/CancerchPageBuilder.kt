package com.greencross.lims.report.cancerch

import com.greencross.lims.report.func.Page
import com.greencross.lims.report.func.PageBuilder
import com.greencross.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.PDDocument

abstract class CancerchPageBuilder<T : CancerchTemplate<in CancerchResource>>(
    template: T,
    dto: CancerchDto
) :
    Page<T>(template) {
    val builder: PageBuilder<CancerchTemplate<CancerchResource>, CancerchDto>

    abstract fun template(): Painter<CancerchTemplate<CancerchResource>, CancerchDto>

    abstract fun pages(): Painter<CancerchTemplate<CancerchResource>, CancerchDto>

    fun build(): PDDocument {
        return builder.add(pages()).build()
    }

    init {
        builder = PageBuilder(template, dto)
    }
}
