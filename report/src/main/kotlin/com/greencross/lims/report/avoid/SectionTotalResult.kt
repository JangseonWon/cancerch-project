package com.greencross.lims.report.avoid

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
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

        if(dto!!.result == AvoidDto.Results.GENERAL) {
            img = template.resource().imgTotalResultLowRisk()
            width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 240f-width/2, y-CONTENT_TITLE_RATE-55, width, RESULT_IMAGE_LOW_RATE)
            stream.paragraph(165f, y-CONTENT_TITLE_RATE-35, 200f, AlignHorizontal.RIGHT, TextBlock(style.clone().fontSize(21f).color(Color(141, 197, 86)), template.lblResultToWord(dto.result)))
        }
        else if(dto.result == AvoidDto.Results.CONCERN){
            img = template.resource().imgTotalResultMiddleRisk()
            width = img.width * RESULT_IMAGE_HIGH_RATE / img.height
            stream.drawImage(img, 240f-width/2, y-CONTENT_TITLE_RATE-55, width, RESULT_IMAGE_HIGH_RATE)
            stream.paragraph(165f, y-CONTENT_TITLE_RATE-35, 200f, AlignHorizontal.RIGHT, TextBlock(style.clone().fontSize(21f).color(Color(239, 167, 24)), template.lblResultToWord(dto.result)))
        }
        else {
            img = template.resource().imgTotalResultHighRisk()
            width = img.width * RESULT_IMAGE_HIGH_RATE / img.height
            stream.drawImage(img, 240f-width/2, y-CONTENT_TITLE_RATE-55, width, RESULT_IMAGE_HIGH_RATE)
            stream.paragraph(165f, y-CONTENT_TITLE_RATE-35, 200f, AlignHorizontal.RIGHT, TextBlock(style.clone().fontSize(21f).color(Color(217,  52, 29)), template.lblResultToWord(dto.result)))
        }
        //endregion

        //region  □ TotalResult Title, Content Text
        val styleRegular = template.resource().styleContentRegualar().clone().fontSize(8.5f)
        val styleBold    = template.resource().styleContentBold().clone().fontSize(8.5f)
        val styleRegular7= template.resource().styleContentRegualar().clone().fontSize(6f)

        if(dto.result == AvoidDto.Results.GENERAL) {
            stream.paragraph(
                325f, y - 31, 400f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblOverviewCommon()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblOverviewBridgeWord()),
                TextBlock(styleBold, template.lblResultToWord(dto.result)),
                TextBlock(styleRegular, template.lblOverviewLowLisk())
            )
        }
        else if(dto.result == AvoidDto.Results.CONCERN)
            stream.paragraph(325f, y-20, 400f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblOverviewCommon()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblOverviewBridgeWord()),
                TextBlock(styleBold, template.lblResultToWord(dto.result)),
                TextBlock(styleRegular, template.lblOverviewRiskBridge()),
                TextBlock(styleBold, template.lblOverViewRisk()),
                TextBlock(styleRegular, template.lblOverViewMiddleRisk()),
                TextBlock(styleRegular7, template.lblOverviewMidHighEnd(dto.result))
            )
        else
            stream.paragraph(325f, y-20, 400f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblOverviewCommon()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblOverviewBridgeWord()),
                TextBlock(styleBold, template.lblResultToWord(dto.result)),
                TextBlock(styleRegular, template.lblOverviewRiskBridge()),
                TextBlock(styleBold, template.lblOverViewRisk()),
                TextBlock(styleRegular, template.lblOverViewHighRisk(dto.first.name)),
                TextBlock(styleBold, template.lblCancerToWord(dto.first.name)),
                TextBlock(styleRegular, template.lblOverViewHighRiskEnd()),
                TextBlock(styleRegular7, template.lblOverviewMidHighEnd(dto.result))
            )
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
