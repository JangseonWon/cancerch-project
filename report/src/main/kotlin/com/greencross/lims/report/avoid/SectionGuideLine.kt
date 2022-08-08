package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionGuideLine (private var y: Float = 375f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        var style = template!!.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(12f)
        var styleBold = template.resource().styleContentBold().clone().fontSize(9f)
        var styleRegular = template.resource().styleContentRegualar().clone().fontSize(8f)
        y -= CONTENT_TITLE_RATE+10+if(dto!!.result == AvoidDto.Results.GENERAL)-17 else 0

        var img = template.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y+7, 300f, AlignHorizontal.CENTER, TextBlock(style, template.lblGuideLineHeader(dto.result, dto.patientName!!)))

        if(dto.result != AvoidDto.Results.GENERAL) {
            img = template.resource().imgGuideLineTable()
            width = img.width * RESULT_CONTENT_RATE / img.height
            y -= RESULT_CONTENT_RATE + 10
            val cancer = when {
                dto.first.name == "기타암종" -> "기타 암"
                else -> dto.first.name
            }
            stream.drawImage(img, 305f - width / 2, y, width, RESULT_CONTENT_RATE)
            stream.paragraph(
                305f,
                y + RESULT_CONTENT_RATE - 17,
                200f,
                AlignHorizontal.CENTER,
                TextBlock(styleBold, template.lblGuideLineTop(dto.result, cancer))
            )

            img = template.resource().imgGuideLineCancer(dto.first.name)
            width = img.width * CONTENT_CANCER_RATE / img.height
            stream.drawImage(img, 100f - width / 2, y + RESULT_CONTENT_RATE - 100, width, CONTENT_CANCER_RATE)
            style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
            stream.paragraph(
                135f,
                y + RESULT_CONTENT_RATE - 102 + CONTENT_CANCER_RATE * 0.5f,
                100f,
                AlignHorizontal.LEFT,
                TextBlock(style, dto.first.name)
            )
            stream.paragraph(
                290f, y + RESULT_CONTENT_RATE - 79 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
                TextBlock(styleBold.clone().color(Color(255, 255, 255)).fontSize(10f), template.lblGuideLineTableHeader1())
            )
            stream.paragraph(
                462f, y + RESULT_CONTENT_RATE - 79 + CONTENT_CANCER_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(styleBold.clone().color(Color(255, 255, 255)).fontSize(10f),  template.lblGuideLineTableHeader2())
            )
            stream.paragraph(
                255f, y+RESULT_CONTENT_RATE-77, 200f, AlignHorizontal.LEFT,
                TextBlock(styleRegular.clone().fontSize(9.5f), template.lblGuideLineDetection())
            )
            stream.paragraph(
                462f, y+RESULT_CONTENT_RATE-77, 160f, AlignHorizontal.CENTER,
                TextBlock(styleRegular.clone().fontSize(10f), template.lblGuideLineTime(dto.first.name))
            )
            stream.paragraph(
                153f, y+64, 400f, AlignHorizontal.LEFT,
                TextBlock(styleRegular.clone().fontSize(9f), template.lblGuideLineComment(dto.first.name))
            )
        } else {
            img = template.resource().imgGuideLineTotalCancer()
            width = img.width * RESULT_CONTENT_LOW_RATE / img.height
            y -= RESULT_CONTENT_LOW_RATE + 20
            stream.drawImage(img, 305f - width / 2, y, width, RESULT_CONTENT_LOW_RATE)
            styleBold = styleBold.clone().color(Color(255,255,255)).fontSize(9f)

            stream.paragraph(89f,  y+ RESULT_CONTENT_LOW_RATE-12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineNormalHeader(0)))
            stream.paragraph(198f, y+ RESULT_CONTENT_LOW_RATE-12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineNormalHeader(1)))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineNormalHeader(2)))
            stream.paragraph(440f, y+ RESULT_CONTENT_LOW_RATE-12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineNormalHeader(3)))

            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(0)))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-71, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(1)))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-108, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(2)))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-147, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(3)))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-179, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(4)))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-207, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(5)))


            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-33, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(0f)))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-71, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(1f)))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-100, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(2f)), TextBlock(styleRegular.clone().fontSize(7f), template.lblGuideLineNormalTarget(2.5f)))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-141, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(3f)))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-179, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(4f)))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-207, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(5f)))

            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(0)))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-71, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(1)))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-108, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(2)))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-147, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(1)))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-173, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(3)))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-207, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(4)))

            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-38, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(0f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-66, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(1f)), TextBlock(styleRegular.clone().fontSize(6.5f), template.lblGuideLineNormalTest(1.5f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-108, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(2f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-147, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(3f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-179, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(4f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-207, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(5f)))

            stream.paragraph(380f, y+ RESULT_CONTENT_LOW_RATE-228, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(55,55,55)).fontSize(5f), template.lblGuideLineCaption()))
        }
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 21f
        private const val CONTENT_CANCER_RATE = 70f
        private const val RESULT_CONTENT_RATE = 199f
        private const val RESULT_CONTENT_LOW_RATE = 216f
    }
}