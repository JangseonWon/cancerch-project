package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color
import kotlin.math.round

class SectionAnalysisComment(private val y: Float = 480f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var y = 0f
        if (dto!!.result != CancerchDto.Results.RISK) {
            y = this.y - RESULT_IMAGE_COMMENT_RATE + 85
            val img = template!!.resource().imgAnalysisContentBox()
            val width = img.width * RESULT_IMAGE_COMMENT_RATE / img.height
            stream.drawImage(img, 297f - width / 2, y, width, RESULT_IMAGE_COMMENT_RATE - 95)
        } else {
            if (dto.first.name != "기타암종") {
                y = this.y - RESULT_IMAGE_COMMENT_RATE + 60
                val img = template!!.resource().imgAnalysisContentBox()
                val width = img.width * RESULT_IMAGE_COMMENT_RATE / img.height
                stream.drawImage(img, 297f - width / 2, y, width, RESULT_IMAGE_COMMENT_RATE - 80)
            }
        }
        val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(9f)
        val styleBold = template.resource().styleContentBold().clone().fontSize(9f)
        val blackbold = styleBold.clone().color(Color(11, 11, 11))

        if (CancerchDto.Results.GENERAL == dto.result) {
            stream.paragraph(
                52f, y + RESULT_IMAGE_COMMENT_RATE - 115, 480f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine1()),
                TextBlock(styleBold, template.lblDetailResultAnalysisContentLine1Risk(dto.result)),
                TextBlock(styleRegular, "인 "),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2NRM()),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3NRM()),
                TextBlock(blackbold, template.lblDetailResultAnalysisContentLine3NRMLast())
            )
        } else if (CancerchDto.Results.CONCERN == dto.result)
            stream.paragraph(
                52f,
                y + RESULT_IMAGE_COMMENT_RATE - 120,
                480f,
                AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine1()),
                TextBlock(styleBold, template.lblDetailResultAnalysisContentLine1Risk(dto.result)),
                TextBlock(styleRegular, "인 "),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2MID()),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3MID_1()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3MID_2()),
                TextBlock(styleBold, template.lblDetailResultAnalysisTableContentMID()),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3MID_3()),
                TextBlock(blackbold, template.lblDetailResultAnalysisContentLine3MID_4())
            )
        else {
            if (dto.first.name != "기타암종") stream.paragraph(
                52f,
                y + RESULT_IMAGE_COMMENT_RATE - 105,
                480f,
                AlignHorizontal.LEFT,
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine1()),
                TextBlock(styleBold, template.lblDetailResultAnalysisContentLine1Risk(dto.result)),
                TextBlock(styleRegular, "인 "),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2HIG()),
                TextBlock(styleBold, template.lblCancerToWord(dto.first.name)),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2END()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_1()),
                TextBlock(styleBold, template.lblCancerToWord(dto.first.name)),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_2()),
                TextBlock(styleBold, template.lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_3()),
                TextBlock(styleBold, (round(dto.first.asr.div(1000) * 10000) / 10000).toString() + "%"),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_4()),
                TextBlock(styleBold, template.lblDetailResultAnalysisContentLine3ASR(dto.first.asr.toString())),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_5()),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_7()),
                TextBlock(styleBold, dto.first.name),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_8()),
                TextBlock(styleBold, dto.first.ppv.toString() + "%"),
                TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_9()),
                TextBlock(blackbold, template.lblDetailResultAnalysisContentLine3HIG_12())
            )
            else {
                stream.line(47f,491f, 542f, 491f).setLineWidth(0.4f).setStrokingColor(Color.BLACK).stroke()
                stream.paragraph(
                    52f,
                    y + RESULT_IMAGE_COMMENT_RATE+275,
                    480f,
                    AlignHorizontal.LEFT,
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine1()),
                    TextBlock(styleBold, template.lblDetailResultAnalysisContentLine1Risk(dto.result)),
                    TextBlock(styleRegular, "인 "),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2HIG()),
                    TextBlock(styleBold, template.lblCancerToWord(dto.first.name)),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine2END()),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_1()),
                    TextBlock(styleBold, template.lblCancerToWord(dto.first.name)),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_2()),
                    TextBlock(styleBold, template.lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_3()),
                    TextBlock(styleBold, (round(dto.first.asr.div(1000) * 10000) / 10000).toString() + "%"),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_4()),
                    TextBlock(styleBold, template.lblDetailResultAnalysisContentLine3ASR(dto.first.asr.toString())),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_5()),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_7()),
                    TextBlock(styleBold, template.lblCancerToWord(dto.first.name)),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_8()),
                    TextBlock(styleBold, dto.first.ppv.toString() + "%"),
                    TextBlock(styleRegular, template.lblDetailResultAnalysisContentLine3HIG_9()),
                    TextBlock(blackbold, template.lblDetailResultAnalysisContentLine3HIG_12()+"\n"),
                    TextBlock(blackbold, dto.first.comment)
                )
            }
        }
        return stream
    }

    companion object {
        private const val RESULT_IMAGE_COMMENT_RATE = 201f
    }
}