package com.greencross.lims.report.ON204

import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.enus.component.*
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import com.greencross.lims.report.ON204.template.DNACXTemplateON204

class DNACXON204EnUs(
    val template: DNACXTemplateON204<DNACXResource>,
    val dto: DNACXDto,
    val sign: Painter<DNACXTemplate<DNACXResource>, DNACXDto>,
    val footer: Painter<DNACXTemplate<DNACXResource>, DNACXDto>,
    val page: Painter<DNACXTemplate<DNACXResource>, DNACXDto>
) : DNACXPageBuilder<DNACXTemplateON204<DNACXResource>>(template, dto) {
    private val title: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionTitle()
    private val intro: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionIntro()
    private val testResult: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionTestResult()
    private val summaryOfResults: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionSummaryOfResults()
    private val tissueOfOrigin: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionTissueOfOrigin()
    private val testResultSmall: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionTestResultSmall()
    private val interpretation: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionInterpretation()
    private val signalScore: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionSignalScore()
    private val covScore: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionCovScore()
    private val femsScore: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionFemsScore()
    private val genomicInstabilityScore: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionGenomicInstabilityScore()
    private val disclaimers: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionDisclaimers()
    override fun template(): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
        return title.and { stream, template, dto ->
            stream.setFont(template.resource().fontDefault(), 11f)
            return@and stream
        }.and(sign).and(footer)
    }

    override fun pages(): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
        return template().and(intro).and(testResult).and(summaryOfResults).and(tissueOfOrigin)
            .and{ s, t, d -> newPage(s) }.and(template()).and(testResultSmall).and(interpretation).and(signalScore)
            .and{ s, t, d -> newPage(s) }.and(template()).and(testResultSmall).and(covScore).and(femsScore).and(genomicInstabilityScore)
            .and{ s, t, d -> newPage(s) }.and(template()).and(disclaimers).and(page)
    }
}
