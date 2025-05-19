package com.greencross.lims.report.ON204.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import java.awt.Color

class SectionDisclaimers(private val y: Float = 708f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var style = template!!.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(12f)
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "Disclaimers"))
        stream.circle(36f, y-34f, 1.5f).fill()
        stream.paragraph(42f, y-38f, 700f, AlignHorizontal.LEFT, TextBlock(style, "This test analyzes cfDNA patterns to screen for cancer, but a cancer signal does not necessarily indicate a\ncancer diagnosis."))
        stream.circle(36f, y-68f, 1.5f).fill()
        stream.paragraph(42f, y-72f, 700f, AlignHorizontal.LEFT, TextBlock(style, "Test performance may vary depending on the stage or type of cancer."))
        stream.circle(36f, y-87f, 1.5f).fill()
        stream.paragraph(42f, y-91f, 700f, AlignHorizontal.LEFT, TextBlock(style, "Sensitivity may vary depending on the location and genetic characteristics of the cancer."))
        stream.circle(36f, y-106f, 1.5f).fill()
        stream.paragraph(42f, y-110f, 700f, AlignHorizontal.LEFT, TextBlock(style, "This test cannot be used as a replacement for standard-of-care testing or conventional MRD (minimal\nresidual disease) testing."))
        stream.circle(36f, y-140f, 1.5f).fill()
        stream.paragraph(42f, y-144f, 700f, AlignHorizontal.LEFT, TextBlock(style, "The sensitivity of the analysis may decrease when the cfDNA concentration is low due to cancer\ntreatment, etc."))
        stream.circle(36f, y-174f, 1.5f).fill()
        stream.paragraph(42f, y-178f, 700f, AlignHorizontal.LEFT, TextBlock(style, "It may be difficult to accurately detect cancer-related patterns in organ transplant recipients and patients\nwith autoimmune diseases."))
        stream.restoreGraphicsState()

        style = template.resource().styleContentRegualar().clone().color(Color(87,90,88)).fontSize(9f)
        stream.rect(57f, y-232f, 480f, 45f).setStrokingColor(Color(242,242,242)).setNonStrokingColor(Color(242,242,242)).fillAndStroke()
        stream.paragraph(70f, y-245f, 100f, AlignHorizontal.LEFT, TextBlock(style, "*\n\n*"))
        stream.paragraph(75f, y-245f, 495f, AlignHorizontal.LEFT, TextBlock(style, "This test has not established the clinical significance of its results, and there is still insufficient evidence for the utility of\n" +
                "Treatment-related actions based on it.\n"), TextBlock(style, "This test was developed and its performance characteristics determined by GC Genome."))
        return stream
    }
}
