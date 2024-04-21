package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Util_EnUS
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.AlignVertical
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color
import kotlin.math.round

class SectionAnalysisComment2(private var y: Float = 490f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {

    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
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
        if (CancerchDto.Results.GENERAL == dto.result) {
            val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(8f)
            val styleBold = template.resource().styleContentBold().clone().fontSize(8f)

            stream.paragraph(
                52f, y + RESULT_IMAGE_COMMENT_RATE - 148, 500f, AlignHorizontal.LEFT, AlignVertical.MIDDLE,
                TextBlock(styleRegular, "The test result of "),
                TextBlock(styleBold, "${dto.patientName} "),
                TextBlock(styleRegular, "is "),
                TextBlock(styleBold, "General Risk "),
                TextBlock(styleRegular, "and DNA patterns is similar to that of healthy individuals.\nAs a result of cfDNA analysis by artificial intelligence algorithm, "),
                TextBlock(styleBold, "${dto.patientName} "),
                TextBlock(styleRegular, "has a low probability of cancer than the general population.\n" +
                        "The ai-CANCERCH test cannot detect all types of cancer, and its detection performance may differ depending on the stage or type of\n" +
                        "cancer. This test is a cancer screening test, not a cancer diagnostic test, so physician-driven diagnosis is recommended.\n" +
                        "This result is only for your current status.\n" +
                        "It is recommended to undergo regular health check-ups and life-style management to monitor the constantly evolving future health status.")
            )
        } else if (CancerchDto.Results.CONCERN == dto.result) {
            val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(8f)
            val styleBold = template.resource().styleContentBold().clone().fontSize(8f)
            val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(8f)

            stream.paragraph(
                52f,
                y + RESULT_IMAGE_COMMENT_RATE - 115,
                480f,
                AlignHorizontal.LEFT,
                TextBlock(styleRegular, "The test result of "),
                TextBlock(styleBold, "${dto.patientName} "),
                TextBlock(styleRegular, "is "),
                TextBlock(styleBold, "Intermediate Risk "),
                TextBlock(
                    styleRegular,
                    "and DNA patterns is similar to that of cancer patients.\nAs a result of cfDNA analysis by artificial intelligence algorithm, "
                ),
                TextBlock(styleBold, "${dto.patientName} "),
                TextBlock(styleRegular, "has a "),
                TextBlock(styleBold, "twice higher "),
                TextBlock(
                    styleRegular,
                    "cancer risk. But it is unclear to predict the origin of tumor. It is able to take several months to develop cancer even with the "
                ),
                TextBlock(styleBold, "Intermediate Risk "),
                TextBlock(styleRegular, "result.\nIt is recommended for follow-up and monitoring.\n\n"),
                TextBlock(
                    blackbold,
                    "Note: Even normal people may be reported as subjects of Intermediate Risk depending on their health status (benign disease, autoimmune disease, etc.) (about 5%)."
                )
            )
        }
        else {
            if (dto.first.name != "기타암종") {
                val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(8f)
                val styleBold = template.resource().styleContentBold().clone().fontSize(8f)
                val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(8f)

                stream.paragraph(
                    52f,
                    y + RESULT_IMAGE_COMMENT_RATE - 96,
                    480f,
                    AlignHorizontal.LEFT,
                    TextBlock(styleRegular, "The test result of "),
                    TextBlock(styleBold, "${dto.patientName} "),
                    TextBlock(styleRegular, "is "),
                    TextBlock(styleBold, "High Risk "),
                    TextBlock(styleRegular, "and DNA patterns is most similar to that of "),
                    TextBlock(styleBold, "${Util_EnUS.cancerToEng(dto.first.name)} "),
                    TextBlock(styleRegular, " patients.\nAs a result of cfDNA analysis by artificial intelligence algorithm, "),
                    TextBlock(styleBold, "${dto.patientName} "),
                    TextBlock(styleRegular, "has a high probability of "),
                    TextBlock(styleBold, Util_EnUS.cancerToEng(dto.first.name)),
                    TextBlock(styleRegular, ", predicted to be more than 10 times higher than the general population.\n"),
                    TextBlock(styleBold, Util_EnUS.lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
                    TextBlock(styleRegular, "typically have a "),
                    TextBlock(styleBold, (round(dto.first.asr.div(1000) * 10000) / 10000).toString() + "% "),
                    TextBlock(styleRegular, "occurrence rate ("),
                    TextBlock(styleBold, dto.first.asr.toString()),
                    TextBlock(styleRegular, " out of 100,000 individuals), but the probability for "),
                    TextBlock(styleBold, "${dto.patientName} "),
                    TextBlock(styleRegular, "to have "),
                    TextBlock(styleBold, "${Util_EnUS.cancerToEng(dto.first.name)} "),
                    TextBlock(styleRegular, "is approximately "),
                    TextBlock(styleBold, dto.first.ppv.toString() + "%"),
                    TextBlock(styleRegular, ".\nIt is able to take several months to develop cancer even with the "),
                    TextBlock(styleBold, "High Risk"),
                    TextBlock(styleRegular, " result.\nIt is recommended for follow-up and monitoring.\n\n"),
                    TextBlock(blackbold, "Note: Even normal people may be reported as subjects of High Risk depending on their health status (benign disease, autoimmune disease, etc.) (about 1%).")
                )
            }
            else {
                stream.line(47f,493f, 542f, 493f).setLineWidth(0.4f).setStrokingColor(Color.BLACK).stroke()

                val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(8f)
                val styleBold = template.resource().styleContentBold().clone().fontSize(8f)
                val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(8f)

                stream.paragraph(
                    52f,
                    y + RESULT_IMAGE_COMMENT_RATE - 213,
                    480f,
                    AlignHorizontal.LEFT,
                    TextBlock(styleRegular, "The test result of "),
                    TextBlock(styleBold, "${dto.patientName} "),
                    TextBlock(styleRegular, "is "),
                    TextBlock(styleBold, "High Risk "),
                    TextBlock(styleRegular, "and DNA patterns is most similar to that of "),
                    TextBlock(styleBold, "Other cancer"),
                    TextBlock(styleRegular, " patients.\nAs a result of cfDNA analysis by artificial intelligence algorithm, "),
                    TextBlock(styleBold, "${dto.patientName} has a high probability of cancer, predicted to be more then 10 times higher than the general population.\n"),
                    TextBlock(styleRegular, "Generally, the prevalence of "),
                    TextBlock(styleBold, "Other cancer "),
                    TextBlock(styleRegular, "in "),
                    TextBlock(styleBold, Util_EnUS.lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
                    TextBlock(styleBold, " is "),
                    TextBlock(styleBold, (round(dto.first.asr.div(1000) * 10000) / 10000).toString() + "% "),
                    TextBlock(styleRegular, "but the risk of "),
                    TextBlock(styleBold, "${dto.patientName} "),
                    TextBlock(styleBold, "is " + dto.first.ppv.toString() + "%"),
                    TextBlock(styleRegular, ".\nIt is able to take several months to develop cancer even with the "),
                    TextBlock(styleBold, "High Risk"),
                    TextBlock(styleRegular, " result.\nIt is recommended for follow-up and monitoring.\n\n"),
                    TextBlock(blackbold, "Note: Even normal people may be reported as subjects of High Risk depending on their health status (benign disease, autoimmune disease, etc.) (about 1%).\n\n"),
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
