package com.greencross.lims.report.ON206

import com.gcgenome.lims.report.func.PageBuilder
import com.gcgenome.lims.report.func.Painter
import com.gcgenome.lims.report.func.Page
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import org.apache.pdfbox.pdmodel.PDDocument

abstract class DNACTPageBuilder<T : DNACTTemplate<in DNACTResource>>(
    template: T,
    dto: DNACTDto
): Page<T>(template) {
    val builder: PageBuilder<DNACTTemplate<DNACTResource>, DNACTDto>

    abstract fun template(): Painter<DNACTTemplate<DNACTResource>, DNACTDto>

    abstract fun pages(): Painter<DNACTTemplate<DNACTResource>, DNACTDto>
    fun build(): PDDocument {
        return builder.add(pages()).build()
    }

    init {
        builder = PageBuilder(template, dto)
    }
}
