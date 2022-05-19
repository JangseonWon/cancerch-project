package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
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
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, "검 사 상 세 설 명"))

        img = template.resource().imgAnalysisContentBox()
        width = img.width * RESULT_CONTENT_LOW_RATE / img.height
        val height = RESULT_CONTENT_LOW_RATE*2.9f
        y -= height+10
        stream.drawImage(img, 305f-width/2, y, width, height)

        img = template.resource().imgProcess()
        width = img.width * PROCESS_CONTENT / img.height
        stream.paragraph(305f, y+height-30, 200f, AlignHorizontal.CENTER, TextBlock(styleRegular, "[ AVOID 검사 과정 ]"))
        stream.drawImage(img, 320f-width/2, y+height-PROCESS_CONTENT-50, width, PROCESS_CONTENT)
        styleRegular = styleRegular.clone().color(Color(0,0,0)).fontSize(9f)
        stream.paragraph(87f, y+height-150, 115f, AlignHorizontal.CENTER, TextBlock(styleRegular, "수검자"))
        stream.paragraph(225f, y+height-145, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular, "유전체 정보 생산\n"),
            TextBlock(styleRegular.clone().fontSize(8f), "; 차세대염기서열분석검사(NGS)")
        )
        stream.paragraph(143f, y+height-105, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "혈액"))
        stream.paragraph(295f, y+height-105, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "입력"))
        stream.paragraph(446f, y+height-60, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(255,255,255)), "암 존재 가능성 확인"))
        stream.paragraph(513f, y+height-80, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(131, 131, 131)), "정상인"))
        stream.paragraph(513f, y+height-103, 100f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().fontSize(11f), "간암"))
        stream.paragraph(513f, y+height-125, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(131, 131, 131)), "췌장암"))
        stream.paragraph(513f, y+height-148, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(131, 131, 131)), "유방암"))
        stream.paragraph(388f, y+height-148, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "학습된 알고리즘"))

        img = template.resource().imgLBxBox()
        width = img.width * LBXBOX_CONTENT / img.height
        stream.drawImage(img, 145f-width/2, y+75, width, LBXBOX_CONTENT)
        stream.paragraph(70f, y+LBXBOX_CONTENT-11, 200f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().fontSize(9f), "액체생검(Liquid Biopsy; LBx)"))
        stream.paragraph(70f, y+LBXBOX_CONTENT-41, 300f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7.5f), template.lblLBx()))
        img = template.resource().imgLBx()
        width = img.width * LBX_CONTENT / img.height
        stream.drawImage(img, 145f-width/2, y+90, width, LBX_CONTENT)

        img = template.resource().imgNGS()
        width = img.width * NGS_CONTENT / img.height
        stream.drawImage(img, 350f-width/2, y+LBXBOX_CONTENT-70, width, NGS_CONTENT)
        stream.paragraph(268f, y+ LBXBOX_CONTENT+1, 300f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().fontSize(9f), "차세대염기서열분석검사\n(Next-Generation Sequencing; NGS)"))
        stream.paragraph(268f, y+LBXBOX_CONTENT-35, 300f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7.5f), template.lblNGS()))

        img = template.resource().imgAiBox()
        width = img.width * AI_CONTENT / img.height
        stream.drawImage(img, 407f-width/2, y+47, width, AI_CONTENT)
        stream.paragraph(265f, y+258, 300f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().fontSize(9f), "인공지능(Artificial Intelligence; AI) - 특허출원"))
        stream.paragraph(265f, y+233, 300f, AlignHorizontal.LEFT,
            TextBlock(styleRegular.clone().fontSize(7.5f), "인공지능 기술 중 하나인 딥 러닝(deep learning) 기술을 이용해\n" +
                "수검자의 암세포에서 유래된 cfDNA를 분석합니다.\n" +
                "딥 러닝은 경험적 데이터를 기반으로 학습하여, 새로운 데이터를 예측하고 스스로의\n" +
                "성능을 향상시키는 인공지능(AI) 기술입니다.\n" +
                "GC녹십자지놈에서 자체개발한 AVOID 검사의 딥 러닝 알고리즘은"),
            TextBlock(styleBold.clone().fontSize(7.5f), "은 약 2천명 이상의\n" +
                "암환자와 정상인의 cfDNA의 특징을 학습"),
            TextBlock(styleRegular.clone().fontSize(7.5f), "하였습니다. 이를 바탕으로 수검자의 cfDNA를\n" +
                    "분석하여 암의 존재 가능성을 예측할 수 있습니다.")
        )
        img = template.resource().imgAi()
        width = img.width * AI_INNER_CONTENT / img.height
        stream.drawImage(img, 407f-width/2, y+67, width, AI_INNER_CONTENT)
        stream.paragraph(393f, y+149f, 100f, TextBlock(styleRegular.clone().fontSize(8f), "Artificial Intelligence"))
        stream.paragraph(393f, y+136f, 100f, TextBlock(styleRegular.clone().fontSize(8f), "Machine Learning"))
        stream.paragraph(388f, y+111f, 100f, TextBlock(styleBold.clone().fontSize(8f), "Deep Learning"))

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