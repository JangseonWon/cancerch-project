package com.greencross.lims.report.ON204.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import com.greencross.lims.report.builder.Util_EnUS
import java.awt.Color

class SectionTitle(private val y: Float = 745f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        val img = template!!.resource().imgTitle()
        val width = img.width * TITLE_HEIGHT / img.height
        stream.drawImage(img, 100f - width / 2, y-1, width, TITLE_HEIGHT)

        val firstHeaderLine = 182.5f
        val firstValueLine = firstHeaderLine+ 67.5f
        val secondHeaderLine = firstHeaderLine * 2
        val secondValueLine = secondHeaderLine + 105f

        stream.paragraph(firstHeaderLine, y + 40f,   50f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(9f), "Institution"))
        stream.paragraph(firstHeaderLine, y + 24f,   100f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(9f), "Sample Type"))
        stream.paragraph(secondHeaderLine,y + 24f,   100f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(9f), "Medical Record No."))
        stream.paragraph(firstHeaderLine, y + 8f,    50f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(9f), "Sample ID"))
        stream.paragraph(secondHeaderLine,y + 8f,    100f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(9f), "Sample Collection Date"))
        stream.paragraph(firstHeaderLine, y - 6f,    50f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(9f), "Age/Gender"))
        stream.paragraph(secondHeaderLine,y - 6f,    100f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(9f), "Receipt/Report Date"))

        stream.paragraph(firstValueLine, y + 40f,   150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(9f), dto.medicalInstitution))
        stream.paragraph(firstValueLine, y + 24f,   300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(9f), dto.specimenType))
        stream.paragraph(secondValueLine,y + 24f,   300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(9f), Util_EnUS.dashIfEmpty(dto.medicalRecordNumber!!)))
        stream.paragraph(firstValueLine, y +  8f,   150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(9f), dto.requestNumber))
        stream.paragraph(secondValueLine,y +  8f,   150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(9f), Util_EnUS.dashIfEmpty(Util_EnUS.date(dto.collectionDate)!!)))
        stream.paragraph(firstValueLine, y -  6f,   150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(9f), Util_EnUS.dashIfEmpty(dto.age!!)),
            TextBlock(template.resource().styleContentRegualar().fontSize(9f), " / "),
            TextBlock(template.resource().styleContentRegualar().fontSize(9f), Util_EnUS.dashIfEmpty(Util_EnUS.sex(dto.sex))))
        stream.paragraph(secondValueLine,y -  6f,   150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(9f), Util_EnUS.dashIfEmpty(
            Util_EnUS.date(dto.receiptDate)!!)),
            TextBlock(template.resource().styleContentRegualar().fontSize(9f), " / "),
            TextBlock(template.resource().styleContentRegualar().fontSize(9f), Util_EnUS.dashIfEmpty(Util_EnUS.date(dto.reportDate)!!))
        )
        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-27f).lineTo(560f,y-27f).fill()

        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val TITLE_HEIGHT = 40f
    }
}
