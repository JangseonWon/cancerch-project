package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.AlignVertical
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
        y -= CONTENT_TITLE_RATE + 10 + if(dto!!.result == CancerchDto.Results.GENERAL) -35 else 0

        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        template.lblGuideLineHeader(stream, y, dto)

        if(dto.result != CancerchDto.Results.GENERAL) {
            y -= 8
            stream.line(34f, y, 561f, y).setStrokingColor(Color(55,55,55)).setLineWidth(0.2f).stroke()
            when(dto.result){
                CancerchDto.Results.RISK -> {
                    img = template.resource().imgGuideLineTable(if(dto.first.name == "기타암종") "OTHERS" else dto.result.name)
                    val rate = RESULT_CONTENT_RATE
                    width = img.width * rate / img.height
                    y -= rate + 10

                    stream.drawImage(img, 297f - width / 2, y-24, width, rate)
                    template.lblGuideLineTop(stream, y + rate - 9, dto)

                    img = template.resource().imgGuideLineCancer(dto.first.name)
                    width = img.width * CONTENT_CANCER_RATE / img.height
                    stream.drawImage(img, 75f - width / 2, y + rate - 90, width, CONTENT_CANCER_RATE)

                    template.lblGuideLineTableHedaer0(stream, y, rate, dto)
                    template.lblGuideLineTableHeader1(stream, y, rate)
                    template.lblGuideLineTableHeader2(stream, y, rate, dto)
                    template.lblGuideLineDetection(stream, y , rate)
                    template.lblGuideLineTime(stream, y, rate, dto)
                    template.lblGuideLineComment(stream, y, dto)
                }
                else -> {
                    img = template.resource().imgGuideLineTable(CancerchDto.Results.CONCERN.name)
                    width = img.width * RESULT_CONTENT_OTH_RATE / img.height
                    y -= RESULT_CONTENT_OTH_RATE + 10

                    stream.drawImage(img, 297f - width / 2, y-24, width, RESULT_CONTENT_OTH_RATE)
                    template.lblGuideLineTop(stream, y + RESULT_CONTENT_OTH_RATE - 9, dto)

                    img = template.resource().imgGuideLineCancer(dto.first.name)
                    width = img.width * CONTENT_CANCER_RATE / img.height
                    stream.drawImage(img, 75f - width / 2, y + CONTENT_CANCER_RATE+30, width, CONTENT_CANCER_RATE)
                    val style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
                    stream.paragraph(140f, y+125, 100f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(style, template.lblResultToWord(dto.result)))
                    template.lblGuideLineTableHeader2(stream, y, 0f, dto)
                    template.lblGuideLineTime(stream, y, 0f, dto)
                    template.lblGuideLineConcern(stream, y)
                }
            }

        } else {
            img = template.resource().imgGuideLineTotalCancer()
            width = img.width * RESULT_CONTENT_LOW_RATE / img.height
            y -= RESULT_CONTENT_LOW_RATE + 10
            stream.drawImage(img, 296.5f - width / 2, y, width, RESULT_CONTENT_LOW_RATE)

            template.lblGuideLineNormalHeader(stream, y)
            template.lblGuideLineNormalCancer(stream, y)
            template.lblGuideLineNormalTarget(stream, y)
            template.lblGuideLineNormalFrequency(stream, y)
            template.lblGuideLineNormalTest(stream, y)
            template.lblGuideLineCaption(stream, y)
        }
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val CONTENT_CANCER_RATE = 60f
        private const val RESULT_CONTENT_RATE = 164.5f
        private const val RESULT_CONTENT_OTH_RATE = 182f
        private const val RESULT_CONTENT_LOW_RATE = 225f
    }
}