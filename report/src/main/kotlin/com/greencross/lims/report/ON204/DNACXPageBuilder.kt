package com.greencross.lims.report.ON204

import com.gcgenome.lims.report.func.Page
import com.gcgenome.lims.report.func.PageBuilder
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import org.apache.pdfbox.pdmodel.PDDocument

abstract class DNACXPageBuilder<T : DNACXTemplate<in DNACXResource>>(
    template: T,
    dto: DNACXDto
) : Page<T>(template) {
    val builder: PageBuilder<DNACXTemplate<DNACXResource>, DNACXDto>

    abstract fun template(): Painter<DNACXTemplate<DNACXResource>, DNACXDto>

    abstract fun pages(): Painter<DNACXTemplate<DNACXResource>, DNACXDto>

    fun build(): PDDocument {
        return builder.add(pages()).build()
    }

    init {
        builder = PageBuilder(template, dto)
    }
}
