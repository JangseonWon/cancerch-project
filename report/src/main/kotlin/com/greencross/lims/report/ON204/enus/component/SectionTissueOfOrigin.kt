package com.greencross.lims.report.ON204.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import com.greencross.lims.report.builder.Sex
import java.awt.Color

class SectionTissueOfOrigin(private val y: Float = 398f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        val checker = dto.risk == DNACXDto.Risk.HIGH && dto.result.signalScore99CutOff <= dto.result.signalScore
        val (color, comment) =
            if (checker) Pair(Color(186,26,50), " "+template!!.resource().lblCancerName(dto.result.cancer))
            else Pair(Color(114,113,113), " Not applicable")

        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "Tissue of Origin:"),
            TextBlock(template.resource().styleContentBold().clone().color(color).fontSize(15f), comment))

        var img = template.resource().imgHuman()
        var width = img.width * DANGER_HUMAN_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - DANGER_HUMAN_RATE - 20, width, DANGER_HUMAN_RATE)

        drawCancerBox(stream, template, "폐암", 125f, y- CANCER_BOX_RATE + 10, checker && "폐암" == dto.result.cancer)
        drawCancerIcon(stream, template, "폐암", 285f, y- CANCER_ICON_RATE - 47, checker && "폐암" == dto.result.cancer)
        drawCancerDashLine(stream, 200f, y- CANCER_BOX_RATE - 22, 261f, y- CANCER_ICON_RATE - 69)
        drawCancerBox(stream, template, "간암", 125f, y- CANCER_BOX_RATE - 55, checker && "간암" == dto.result.cancer)
        drawCancerIcon(stream, template, "간암", 265f, y- CANCER_ICON_RATE - 94, checker && "간암" == dto.result.cancer)
        drawCancerDashLine(stream, 200f, y- CANCER_BOX_RATE - 87, 243f, y- CANCER_ICON_RATE - 116)
        drawCancerBox(stream, template, "대장암", 125f, y- CANCER_BOX_RATE - 120, checker && "대장암" == dto.result.cancer)
        drawCancerIcon(stream, template, "대장암", 291f, y- CANCER_ICON_RATE - 148, checker && "대장암" == dto.result.cancer)
        drawCancerDashLine(stream, 200f, y- CANCER_BOX_RATE - 152, 269f, y- CANCER_ICON_RATE - 170)
        drawCancerBox(stream, template, "식도암", 477f, y- CANCER_BOX_RATE + 10, checker && "식도암" == dto.result.cancer)
        drawCancerIcon(stream, template, "식도암", 315f, y- CANCER_ICON_RATE + 1, checker && "식도암" == dto.result.cancer)
        drawCancerDashLine(stream, 400f, y- CANCER_BOX_RATE - 22, 337f, y- CANCER_ICON_RATE - 21)
        drawCancerBox(stream, template, "췌장담도암", 477f, y- CANCER_BOX_RATE - 55, checker && "췌장담도암" == dto.result.cancer)
        drawCancerIcon(stream, template, "췌장담도암", 333f, y- CANCER_ICON_RATE - 88, checker && "췌장담도암" == dto.result.cancer)
        drawCancerDashLine(stream, 400f, y- CANCER_BOX_RATE - 87, 355f, y- CANCER_ICON_RATE - 110)
        if(dto.sex == Sex.F) {
            drawCancerBox(stream, template, "난소암", 477f, y- CANCER_BOX_RATE - 120, checker && "난소암" == dto.result.cancer)
            drawCancerIcon(stream, template, "난소암", 343f, y- CANCER_ICON_RATE - 143, checker && "난소암" == dto.result.cancer)
            drawCancerDashLine(stream, 400f, y- CANCER_BOX_RATE - 152, 365f, y- CANCER_ICON_RATE - 165)
        }

        if(!checker) {
            img = template.resource().imgHumanBox()
            width = img.width * IMG_HUMAN_BOX_RATE / img.height
            stream.drawImage(img, 302f - width / 2, y- CANCER_BOX_RATE - 98, width, IMG_HUMAN_BOX_RATE)
            stream.paragraph(300f, y- CANCER_BOX_RATE - 80, 200f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentBold().clone().color(Color(114,113,113)).fontSize(13f), "Not applicable"))
        }

        stream.restoreGraphicsState()
        return stream
    }
    private fun drawCancerDashLine(
        stream: PDPageContentStreamPageAccessible,
        sx: Float,
        sy: Float,
        ex: Float,
        ey: Float
    ) {
        stream.saveGraphicsState()
        stream.setNonStrokingColor(Color.GRAY).setStrokingColor(Color.GRAY)
            .setLineWidth(0.3f).setLineDashPattern(floatArrayOf(1f, 1.5f), 1f)
        stream.line(sx, sy, ex, ey).stroke()
        stream.restoreGraphicsState()
    }

    private fun drawCancerBox(stream: PDPageContentStreamPageAccessible, template: DNACXTemplate<DNACXResource>, cancer: String, x: Float, y: Float, checker: Boolean) {
        var img = template.resource().imgCancerBox()
        var width = img.width * CANCER_BOX_RATE / img.height
        stream.saveGraphicsState()
        stream.drawImage(img, x - width/2, y - CANCER_BOX_RATE, width, CANCER_BOX_RATE)
        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).line(x-width/2+40, y - CANCER_BOX_RATE/2, x+width/2-15, y - CANCER_BOX_RATE/2).stroke()
        stream.paragraph(x+12, y - CANCER_BOX_RATE/2 + 8, 100f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentBold().clone().color(Color(35,24,21)).fontSize(11f), template.resource().lblCancerName(cancer)))
        stream.paragraph(x+12, y - CANCER_BOX_RATE/2 - 15, 100f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentBold().clone().color(if(checker)Color(186,26,50) else Color(114,113,113)).fontSize(13f), if(checker) "Suspected" else "Not applicable"))

        img = template.resource().imgCancerTypeImage(cancer)
        width = img.width * CANCER_GRAY_ICON_RATE / img.height
        stream.drawImage(img, x-width - 42, y- CANCER_BOX_RATE/2-14, width, CANCER_GRAY_ICON_RATE)
        stream.restoreGraphicsState()
    }

    private fun drawCancerIcon(stream: PDPageContentStreamPageAccessible, template: DNACXTemplate<DNACXResource>, cancer: String, x: Float, y: Float, checker: Boolean) {
        val img = template.resource().imgCangerTypeIcon(cancer, checker)
        val width = img.width * CANCER_ICON_RATE / img.height
        stream.drawImage(img, x-width/2, y- CANCER_ICON_RATE, width, CANCER_ICON_RATE)
    }



    companion object {
        private const val DANGER_HUMAN_RATE = 260f
        private const val CANCER_BOX_RATE = 65f
        private const val IMG_HUMAN_BOX_RATE = 40f
        private const val CANCER_ICON_RATE = 48f
        private const val CANCER_GRAY_ICON_RATE = 30f
    }
}
