package com.greencross.lims.report.ON206.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionPlot(private val y: Float = 708f): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        val headerFont = template.resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(13f)
        val smallHeaderFont = template.resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(9f)
        val subTitleFont = template.resource().styleContentRegualar().color(Color(0,80,109)).fontSize(13f)
        val subTitleFontItalic = template.resource().styleContentBoldIt().clone().color(Color(0,80,109)).fontSize(13f)
        stream.rect(35f, y-32f, 525f, 32f).setStrokingColor(Color(0,80,109)).setNonStrokingColor(Color(0,80,109)).fillAndStroke()
        stream.paragraph(44f, y-46f, 500f, AlignHorizontal.LEFT,
            TextBlock(headerFont, "Genomic Instability Plot\n"),
            TextBlock(smallHeaderFont, "The genomic instability plot shows numerical changes across the entire chromosome observed in cancer patients."))
        stream.paragraph(44f, y-83f, 500f, AlignHorizontal.LEFT, TextBlock(subTitleFont, "Genomic Instability Plot of "), TextBlock(subTitleFontItalic, "Examinee"))

        var img = template.resource().imgGenomicInstability(dto.result[0].GenomicPath)
        var width = img.width * GENOMIC_RATE / img.height
        stream.drawImage(img, 308f - width / 2, y - GENOMIC_RATE - 70, width, GENOMIC_RATE)

        img = template.resource().imgGenomicInstabilityExample()
        width = img.width * GENOMIC_EXAMPLE_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - GENOMIC_EXAMPLE_RATE - 200, width, GENOMIC_EXAMPLE_RATE)

        img = template.resource().imgFEMS(dto.result[0].FEMSPath)
        width = img.width * FEMS_RATE / img.height
        stream.drawImage(img, 308f - width / 2, y - FEMS_RATE - 360, width, FEMS_RATE)
        stream.rect(35f, y-297f, 525f, 32f).setStrokingColor(Color(0,80,109)).setNonStrokingColor(Color(0,80,109)).fillAndStroke()
        stream.paragraph(44f, y-311f, 500f, AlignHorizontal.LEFT,
            TextBlock(headerFont, "FEMS Heatmap\n"),
            TextBlock(smallHeaderFont, "The FEMS plot shows changes in the patterns of cfDNA size and end motifs observed in cancer patients."))
        stream.paragraph(44f, y-348f, 500f, AlignHorizontal.LEFT, TextBlock(subTitleFont, "FEMS Heatmap of "), TextBlock(subTitleFontItalic, "Examinee"))

        img = template.resource().imgFEMSExample()
        width = img.width * FEMS_EXAMPLE_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - FEMS_EXAMPLE_RATE - 500, width, FEMS_EXAMPLE_RATE)
        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val GENOMIC_RATE = 160f
        private const val GENOMIC_EXAMPLE_RATE = 87f
        private const val FEMS_RATE = 135f
        private const val FEMS_EXAMPLE_RATE = 107f
    }
}
