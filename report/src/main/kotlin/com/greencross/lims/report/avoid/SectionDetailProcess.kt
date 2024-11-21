package com.greencross.lims.report.avoid

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class SectionDetailProcess (private var y: Float = 685f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        var style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(12f)
        var styleBold = template.resource().styleContentBold().clone()
        var styleRegular = template.resource().styleContentRegualar().clone().color(Color(81,81,81)).fontSize(12f)
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, template.lblDetailProcessHeader()))

        img = template.resource().imgAnalysisContentBox()
        width = img.width * RESULT_CONTENT_LOW_RATE / img.height
        val height = RESULT_CONTENT_LOW_RATE*2.9f
        y -= height+10
        stream.drawImage(img, 305f-width/2, y, width, height)

        img = template.resource().imgProcess()
        width = img.width * PROCESS_CONTENT / img.height
        stream.paragraph(305f, y+height-30, 200f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblDetailProcessTitle()))
        stream.drawImage(img, 320f-width/2, y+height-PROCESS_CONTENT-50, width, PROCESS_CONTENT)
        styleRegular = styleRegular.clone().color(Color(0,0,0)).fontSize(9f)
        stream.paragraph(87f, y+height-150, 115f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblDetailProcessContentCode(0)))
        stream.paragraph(225f, y+height-145, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular,  template.lblDetailProcessContentCode(1)),
            TextBlock(styleRegular.clone().fontSize(8f),  template.lblDetailProcessContentCode(2))
        )
        stream.paragraph(143f, y+height-105, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblDetailProcessContentCode(3)))
        stream.paragraph(295f, y+height-105, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblDetailProcessContentCode(4)))
        stream.paragraph(446f, y+height-60, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(255,255,255)), template.lblDetailProcessContentCode(5)))
        stream.paragraph(500f, y+height-80, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(131, 131, 131)), template.lblDetailProcessContentCode(6)))
        stream.paragraph(500f, y+height-103, 100f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().fontSize(11f), template.lblDetailProcessContentCode(7)))
        stream.paragraph(500f, y+height-125, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(131, 131, 131)), template.lblDetailProcessContentCode(8)))
        stream.paragraph(500f, y+height-148, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(131, 131, 131)), template.lblDetailProcessContentCode(9)))
        stream.paragraph(388f, y+height-148, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,  template.lblDetailProcessContentCode(10)))

        img = template.resource().imgLBxBox()
        width = img.width * LBXBOX_CONTENT / img.height
        stream.drawImage(img, 145f-width/2, y+75, width, LBXBOX_CONTENT)
        stream.paragraph(70f, y+LBXBOX_CONTENT-11, 200f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().fontSize(9f), template.lblDetailProcessContentCode(11)))
        stream.paragraph(70f, y+LBXBOX_CONTENT-41, 300f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7.5f), template.lblLBx()))
        img = template.resource().imgLBx()
        width = img.width * LBX_CONTENT / img.height
        stream.drawImage(img, 145f-width/2, y+90, width, LBX_CONTENT)

        img = template.resource().imgNGS()
        width = img.width * NGS_CONTENT / img.height
        stream.drawImage(img, 350f-width/2, y+LBXBOX_CONTENT-70, width, NGS_CONTENT)
        stream.paragraph(268f, y+ LBXBOX_CONTENT+1, 300f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().fontSize(9f), template.lblDetailProcessContentCode(12)))
        stream.paragraph(268f, y+LBXBOX_CONTENT-35, 300f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7.5f), template.lblNGS()))

        img = template.resource().imgAiBox()
        width = img.width * AI_CONTENT / img.height
        stream.drawImage(img, 407f-width/2, y+47, width, AI_CONTENT)
        stream.paragraph(265f, y+258, 300f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().fontSize(9f), template.lblDetailProcessContentCode(13)))
        stream.paragraph(265f, y+233, 300f, AlignHorizontal.LEFT,
            TextBlock(styleRegular.clone().fontSize(7.5f), template.lblDetailProcessContentCode(14)),
            TextBlock(styleBold.clone().fontSize(7.5f), template.lblDetailProcessContentCode(15)),
            TextBlock(styleRegular.clone().fontSize(7.5f), template.lblDetailProcessContentCode(16))
        )
        img = template.resource().imgAi()
        width = img.width * AI_INNER_CONTENT / img.height
        stream.drawImage(img, 407f-width/2, y+67, width, AI_INNER_CONTENT)
        stream.paragraph(393f, y+149f, 100f, TextBlock(styleRegular.clone().fontSize(8f), template.lblDetailProcessContentCode(17)))
        stream.paragraph(393f, y+136f, 100f, TextBlock(styleRegular.clone().fontSize(8f), template.lblDetailProcessContentCode(18)))
        stream.paragraph(388f, y+111f, 100f, TextBlock(styleBold.clone().fontSize(8f), template.lblDetailProcessContentCode(19)))

        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 21f
        private const val PROCESS_CONTENT = 126f
        private const val LBXBOX_CONTENT = 370f
        private const val AI_CONTENT = 370f
        private const val AI_INNER_CONTENT = 95f
        private const val LBX_CONTENT = 105f
        private const val NGS_CONTENT = 125f
        private const val RESULT_CONTENT_LOW_RATE = 199f
    }
}
