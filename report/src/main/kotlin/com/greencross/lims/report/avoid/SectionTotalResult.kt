package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionTotalResult(private val y: Float = 612f) : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ TotalResult Title, Content Img
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        val style = template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(12f)
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, template.lblOverviewTitle()))

        img = template.resource().imgTotalResultContent()
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(img, 305f-width/2, y- CONTENT_TITLE_RATE-72, width, RESULT_CONTENT_RATE)

        if(dto!!.result == AvoidDto.Results.저위험) {
            img = template.resource().imgTotalResultLowRisk()
            width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 185f-width/2, y-CONTENT_TITLE_RATE-57, width, RESULT_IMAGE_LOW_RATE)
        }
        else {
            img = template.resource().imgTotalResultHighRisk()
            width = img.width * RESULT_IMAGE_HIGH_RATE / img.height
            stream.drawImage(img, 185f-width/2, y-CONTENT_TITLE_RATE-55, width, RESULT_IMAGE_HIGH_RATE)
        }
        //endregion

        //region  □ TotalResult Title, Content Text
        val styleRegular = template.resource().styleContentRegualar().clone().fontSize(8.5f)
        val styleBold    = template.resource().styleContentBold().clone().fontSize(8.5f)

        if(dto.result == AvoidDto.Results.저위험) {
            stream.paragraph(
                305f, y - 37, 400f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblOverviewCommon()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, "님은 "),
                TextBlock(styleBold, "저위험군"),
                TextBlock(styleRegular, template.lblOverviewLowLisk())
            )
        }
        else if(dto.result == AvoidDto.Results.고위험)
            stream.paragraph(305f, y-30, 400f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblOverviewCommon()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, "님은 암환자 군과 유사한 "),
                TextBlock(styleBold, "이상 패턴이 발견"),
                TextBlock(styleRegular, "되어 "),
                TextBlock(styleBold, "고위험군"),
                TextBlock(styleRegular, "입니다.\n가장 의심되는 암종은 "),
                TextBlock(styleBold, dto.first.name),
                TextBlock(styleRegular, "이며, 그 다음으로는 "),
                TextBlock(styleBold, dto.second.name),
                TextBlock(styleRegular, template.lblOverviewHighLisk()))
        else
            stream.paragraph(305f, y-30, 400f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblOverviewCommon()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, "님은 암환자 군과 유사한 "),
                TextBlock(styleBold, "이상 패턴이 발견"),
                TextBlock(styleRegular, "되어 "),
                TextBlock(styleBold, "고위험군"),
                TextBlock(styleRegular, "입니다.\n검사에 포함된 7종 암을 제외한 "),
                TextBlock(styleBold, "기타 암"),
                TextBlock(styleRegular, "이 의심됩니다.\n"+template.lblOverviewHighLisk().substring(5)))
        //endregion
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 21f
        private const val RESULT_CONTENT_RATE = 86f
        private const val RESULT_IMAGE_LOW_RATE = 55f
        private const val RESULT_IMAGE_HIGH_RATE = 53f
    }
}