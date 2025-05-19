package com.greencross.lims.report.ON206.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import com.greencross.lims.report.builder.Util_EnUS
import java.awt.Color

class SectionTitle(private val y: Float = 745f): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACTTemplate<DNACTResource>?,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        val img = template!!.resource().imgTitle()
        val width = img.width * TITLE_HEIGHT / img.height
        stream.drawImage(img, 98f - width / 2, y-7, width, TITLE_HEIGHT)

        val firstHeaderLine = 182.5f
        val firstValueLine = firstHeaderLine+ 67.5f
        val secondHeaderLine = firstHeaderLine * 2
        val secondValueLine = secondHeaderLine + 105f
        val headerStyle  = template.resource().styleContentBold().color(Color(0,80,109)).fontSize(9f)
        val contentStyle = template.resource().styleContentRegualar().fontSize(9f)

        stream.paragraph(firstHeaderLine, y + 40f,   50f,  AlignHorizontal.LEFT, TextBlock(headerStyle, "依頼機関"))
        stream.paragraph(firstHeaderLine, y + 24f,   100f, AlignHorizontal.LEFT, TextBlock(headerStyle, "検体"))
        stream.paragraph(secondHeaderLine,y + 24f,   100f, AlignHorizontal.LEFT, TextBlock(headerStyle, "患者ID"))
        stream.paragraph(firstHeaderLine, y + 8f,    50f,  AlignHorizontal.LEFT, TextBlock(headerStyle, "検体番号"))
        stream.paragraph(secondHeaderLine,y + 8f,    100f, AlignHorizontal.LEFT, TextBlock(headerStyle, "検体採取日"))
        stream.paragraph(firstHeaderLine, y - 6f,    50f,  AlignHorizontal.LEFT, TextBlock(headerStyle, "年齢/性別"))
        stream.paragraph(secondHeaderLine,y - 6f,    100f, AlignHorizontal.LEFT, TextBlock(headerStyle, "受付日/報告日"))

        stream.paragraph(firstValueLine, y + 40f,   150f, AlignHorizontal.LEFT, TextBlock(contentStyle, dto.medicalInstitution))
        stream.paragraph(firstValueLine, y + 24f,   300f, AlignHorizontal.LEFT, TextBlock(contentStyle, dto.specimenType))
        stream.paragraph(secondValueLine,y + 24f,   300f, AlignHorizontal.LEFT, TextBlock(contentStyle, Util_EnUS.dashIfEmpty(dto.medicalRecordNumber!!)))
        stream.paragraph(firstValueLine, y +  8f,   150f, AlignHorizontal.LEFT, TextBlock(contentStyle, dto.requestNumber))
        stream.paragraph(secondValueLine,y +  8f,   150f, AlignHorizontal.LEFT, TextBlock(contentStyle, Util_EnUS.dashIfEmpty(
            Util_EnUS.date(dto.collectionDate)!!))
        )
        stream.paragraph(firstValueLine, y -  6f,   150f, AlignHorizontal.LEFT, TextBlock(contentStyle, Util_EnUS.dashIfEmpty(dto.age!!)),
            TextBlock(contentStyle, " / "),
            TextBlock(contentStyle, Util_EnUS.dashIfEmpty(Util_EnUS.sex(dto.sex)))
        )
        stream.paragraph(secondValueLine,y -  6f,   150f, AlignHorizontal.LEFT, TextBlock(contentStyle, Util_EnUS.dashIfEmpty(
            Util_EnUS.date(dto.receiptDate)!!)),
            TextBlock(contentStyle, " / "),
            TextBlock(contentStyle, Util_EnUS.dashIfEmpty(Util_EnUS.date(dto.reportDate)!!))
        )
        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-27f).lineTo(560f,y-27f).fill()

        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val TITLE_HEIGHT = 48f
    }
}
