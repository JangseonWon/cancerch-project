package com.greencross.lims.report.ON206

import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.enus.component.*
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import com.greencross.lims.report.ON206.template.DNACTTemplateON206

class DNACTON206EnUs(
    val template: DNACTTemplateON206<DNACTResource>,
    val dto: DNACTDto,
    val sign: Painter<DNACTTemplate<DNACTResource>, DNACTDto>,
    val footer: Painter<DNACTTemplate<DNACTResource>, DNACTDto>,
    val page: Painter<DNACTTemplate<DNACTResource>, DNACTDto>
) : DNACTPageBuilder<DNACTTemplateON206<DNACTResource>>(template, dto) {
    private val title: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionTitle()
    private val intro: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionIntro()
    private val clinicalInformation: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionClinicalInformation()
    private val abnormalPatterns: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionAbnormalPatterns()
    private val summaryOfResults: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionSummaryOfResults()
    private val interpretation: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionInterpretation()
    private val abnormalPatternsSmall: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionAbnormalPatternsSmall()
    private val plot: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionPlot()
    private val trackingResults: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionTrackingResults()
    private val trackingGraph: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionTrackingGraph()
    private val disclaimers: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionDisclaimers()
    override fun template(): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
        return title.and { stream, template, dto ->
            stream.setFont(template.resource().fontDefault(), 11f)
            return@and stream
        }.and(sign).and(footer)
    }

    override fun pages(): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
        return template().and(intro).and(clinicalInformation).and(abnormalPatterns).and(summaryOfResults).and(interpretation)
            .and { s, t, d -> newPage(s) }.and(template()).and(abnormalPatternsSmall).and(plot)
            .and { s, t, d -> newPage(s) }.and(template()).and(abnormalPatternsSmall).and(trackingResults).and(trackingGraph)
            .and { s, t, d -> newPage(s) }.and(template()).and(disclaimers).and(page)
    }
}
