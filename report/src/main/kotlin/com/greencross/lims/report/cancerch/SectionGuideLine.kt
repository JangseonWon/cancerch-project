package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionGuideLine(private var y: Float = 329f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var style = template!!.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
        var styleBold = template.resource().styleContentBold().clone().fontSize(11f)
        val styleRegular = template.resource().styleContentRegualar().clone().fontSize(8f)
        y -= CONTENT_TITLE_RATE + 10 + if(dto!!.result == CancerchDto.Results.GENERAL) -35 else 0

        var img = template.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(style, template.lblGuideLineHeader(dto.result, dto.patientName!!)))

        if(dto.result != CancerchDto.Results.GENERAL) {
            y -= 8
            stream.line(34f, y, 561f, y).setStrokingColor(Color(55,55,55)).setLineWidth(0.2f).stroke()
            when(dto.result){
                CancerchDto.Results.RISK -> {
                    img = template.resource().imgGuideLineTable(if(dto.first.name == "기타암종") "OTHERS" else dto.result.name)
                    val rate = RESULT_CONTENT_RATE
                    width = img.width * rate / img.height
                    y -= rate + 10
                    val cancer = when {
                        dto.first.name == "기타암종" -> "기타 암"
                        else -> dto.first.name
                    }
                    stream.drawImage(img, 297f - width / 2, y-24, width, rate)
                    stream.paragraph(305f, y + rate - 9, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineTop(dto.result, cancer)))

                    img = template.resource().imgGuideLineCancer(dto.first.name)
                    width = img.width * CONTENT_CANCER_RATE / img.height
                    stream.drawImage(img, 75f - width / 2, y + rate - 100, width, CONTENT_CANCER_RATE)
                    style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(if(dto.first.name == "췌장담도암") 12f else 14f)
                    stream.paragraph(148f, y + rate - 102 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
                        TextBlock(style, template.lblCancerToWord(dto.first.name))
                    )
                    stream.paragraph(280f, y + rate - 73 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
                        TextBlock(styleBold.clone().color(Color(255, 255, 255)).fontSize(10f), template.lblGuideLineTableHeader1())
                    )
                    stream.paragraph(468f, y + rate - 73 + CONTENT_CANCER_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                        TextBlock(styleBold.clone().color(Color(255, 255, 255)).fontSize(10f),  template.lblGuideLineTableHeader2())
                    )
                    stream.paragraph(238f, y+rate-74, 200f, AlignHorizontal.LEFT,
                        TextBlock(styleRegular.clone().fontSize(9.5f), template.lblGuideLineDetection())
                    )
                    stream.paragraph(468f, y+rate-75, 160f, AlignHorizontal.CENTER,
                        TextBlock(styleRegular.clone().fontSize(10f), template.lblGuideLineTime(dto.first.name))
                    )
                    stream.paragraph(144f, y+34, 400f, AlignHorizontal.LEFT,
                        TextBlock(styleRegular.clone().fontSize(9f), template.lblGuideLineComment(dto.first.name))
                    )
                }
                else -> {
                    img = template.resource().imgGuideLineTable(CancerchDto.Results.CONCERN.name)
                    width = img.width * RESULT_CONTENT_OTH_RATE / img.height
                    y -= RESULT_CONTENT_OTH_RATE + 10
                    val cancer = when {
                        dto.first.name == "기타암종" -> "기타 암"
                        else -> dto.first.name
                    }
                    stream.drawImage(img, 297f - width / 2, y-24, width, RESULT_CONTENT_OTH_RATE)
                    stream.paragraph(305f, y + RESULT_CONTENT_OTH_RATE - 9, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineTop(dto.result, cancer)))

                    img = template.resource().imgGuideLineCancer(dto.first.name)
                    width = img.width * CONTENT_CANCER_RATE / img.height
                    stream.drawImage(img, 80f - width / 2, y + CONTENT_CANCER_RATE+15, width, CONTENT_CANCER_RATE)
                    style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
                    stream.paragraph(120f, y+115, 100f, AlignHorizontal.LEFT,
                        TextBlock(style, template.lblResultToWord(dto.result))
                    )
                    stream.paragraph(380f, y + RESULT_CONTENT_OTH_RATE - 129 + RESULT_CONTENT_OTH_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                        TextBlock(styleBold.clone().color(Color(255, 255, 255)).fontSize(10f),  template.lblGuideLineTableHeader2())
                    )
                    stream.paragraph(380f, y+ RESULT_CONTENT_OTH_RATE -77, 160f, AlignHorizontal.CENTER,
                        TextBlock(styleRegular.clone().fontSize(10f), template.lblGuideLineTime(dto.first.name))
                    )
                    stream.paragraph(134f, y+50, 500f, AlignHorizontal.LEFT,
                        TextBlock(styleRegular.clone().fontSize(9f), template.lblGuideLineConcern())
                    )
                }
            }

        } else {
            img = template.resource().imgGuideLineTotalCancer()
            width = img.width * RESULT_CONTENT_LOW_RATE / img.height
            y -= RESULT_CONTENT_LOW_RATE + 10
            stream.drawImage(img, 296.5f - width / 2, y, width, RESULT_CONTENT_LOW_RATE)
            styleBold = styleBold.clone().color(Color(255,255,255)).fontSize(9f)

            stream.paragraph(59f,  y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineNormalHeader(0)))
            stream.paragraph(178f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineNormalHeader(1)))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineNormalHeader(2)))
            stream.paragraph(440f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblGuideLineNormalHeader(3)))

            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(0)))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(1)))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(2)))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(3)))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(4)))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalCancer(5)))

            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -34, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(0f)))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -73, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(1f)))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -102, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(2f)), TextBlock(styleRegular.clone().fontSize(7f), template.lblGuideLineNormalTarget(2.5f)))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -147, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7f), template.lblGuideLineNormalTarget(3f)))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -187, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(4f)))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -215, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTarget(5f)))

            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(0)))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(1)))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(2)))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(1)))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -182, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(3)))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblGuideLineNormalFrequency(4)))

            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(0f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -69, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(1f)), TextBlock(styleRegular.clone().fontSize(6.5f), template.lblGuideLineNormalTest(1.5f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -113, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(2f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(3f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblGuideLineNormalTest(4f)))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -215, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7.5f), template.lblGuideLineNormalTest(5f)))

            stream.paragraph(400f, y+ RESULT_CONTENT_LOW_RATE -232, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(
                Color(55,55,55)
            ).fontSize(5f), template.lblGuideLineCaption())
            )
        }
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val CONTENT_CANCER_RATE = 70f
        private const val RESULT_CONTENT_RATE = 164.5f
        private const val RESULT_CONTENT_OTH_RATE = 182f
        private const val RESULT_CONTENT_LOW_RATE = 225f
    }
}