package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color
import kotlin.math.round

class SectionDetailResultComment (private var y: Float = 581f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        val rate = if(AvoidDto.Results.GENERAL == dto!!.result) RESULT_IMAGE_COMMENT_RATE_LOW else RESULT_IMAGE_COMMENT_RATE
        y = y - rate - 10
        val img = template!!.resource().imgAnalysisContentBox()
        val width = img.width*RESULT_IMAGE_COMMENT_RATE /img.height
        stream.drawImage(img, 305f - width /2 , y, width, rate)
        val styleRegular = template.resource().styleContentRegualar().clone().fontSize(9f)
        val styleBold    = template.resource().styleContentBold().clone().fontSize(9f)
        val blackbold = styleBold.clone().color(Color(11,11,11))
        if(AvoidDto.Results.GENERAL == dto.result) {
            stream.paragraph(
                80f, y + rate - 35, 480f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine1()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2NRM()),
                TextBlock(styleBold, template.lblResultToWord(dto.result)),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2END()),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3NRM())
            )
        }
        else if(AvoidDto.Results.CONCERN == dto.result)
            stream.paragraph(
                80f, y+rate-35, 480f, AlignHorizontal.LEFT,
                TextBlock(styleRegular,  template.lblDetailResultAnalysisContentLine1()),
                TextBlock(styleBold, dto.patientName), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2MID()),
                TextBlock(styleBold, template.lblResultToWord(dto.result)), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2END()),
                TextBlock(styleRegular, dto.patientName),TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3MID_1()),
                TextBlock(styleBold, template.lblDetailResultAnalysisTableContentMID()), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3MID_2()),
                TextBlock(styleBold, template.lblResultToWord(dto.result)), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3MID_3()),
                TextBlock(blackbold, template.lblDetailResultAnalysisContentLine3MID_4()), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3MIDHIG())
            )
        else stream.paragraph(
            80f, y + rate - 20, 480f, AlignHorizontal.LEFT,
            TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine1()), TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2HIG()),
            TextBlock(styleBold, template.lblResultToWord(dto.result)), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2END()),
            TextBlock(styleBold, dto.patientName), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_1()),
            TextBlock(styleBold, dto.first.name),  TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_2()),
            TextBlock(styleBold, template.lblPatientInfo(dto.age!!, dto.sex!!)), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_3()),
            TextBlock(styleBold, (round(dto.first.asr.div(1000) * 10000) / 10000).toString()+"%"),
            TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_4()), TextBlock(styleBold, template.lblDetailResultAnalysisContentLine3ASR(dto.first.asr.toString())),
            TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_5()), TextBlock(styleBold, dto.first.name+" "),
            TextBlock(styleBold, template.lblResultToWord(dto.result)), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_6()),
            TextBlock(styleBold, dto.patientName), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_7()),
            TextBlock(styleBold, dto.first.name), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_8()),
            TextBlock(styleBold, dto.first.ppv.toString()+"%"), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_9()),
            TextBlock(styleBold, dto.first.name), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_10()),
            TextBlock(styleBold, template.lblResultToWord(dto.result)), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_11()),
            TextBlock(blackbold, template.lblDetailResultAnalysisContentLine3HIG_12()), TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3MIDHIG())
        )
        return stream
    }
    companion object {
        private const val RESULT_IMAGE_COMMENT_RATE = 186f
        private const val RESULT_IMAGE_COMMENT_RATE_LOW = 186f*0.8f
    }
}