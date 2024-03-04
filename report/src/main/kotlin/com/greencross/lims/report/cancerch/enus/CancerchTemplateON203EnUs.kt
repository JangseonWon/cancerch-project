package com.greencross.lims.report.cancerch.enus

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.builder.Util
import com.greencross.lims.report.cancerch.*
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.AlignVertical
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import java.awt.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import kotlin.math.round

class CancerchTemplateON203EnUs(
    resource: CancerchResourceON203EnUs,
    testInfo: TestInfo
) : CancerchTemplateEnUs<CancerchResourceON203EnUs>(testInfo),
    CancerchTemplateON203<CancerchResourceON203EnUs> {
    private val resource: CancerchResourceON203EnUs = resource
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun resource(): CancerchResourceON203EnUs {
        return resource
    }

    override fun lblTitleSmallLogo() = "[Pan-cancer : 주요 암]"
    override fun lblMedicalInstitution(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(215f, y + 50f, 70f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(6.5f), "Institution")) }
    override fun lblRequestNumber(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(215f, y + 20.6f, 70f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(6.5f), "Receipt No.")) }
    override fun lblPatientName(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(215f, y + 35.3f, 70f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(6.5f), "Name")) }
    override fun lblAgeSex(stream: PDPageContentStreamPageAccessible, y: Float) {stream.paragraph(215f, y + 5.6f, 70f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(6.5f), "Age/Gender")) }
    override fun lblMedicalRecordNumber(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(386f, y + 20.6f, 70f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(6.5f), "Registration No.")) }
    override fun lblSpecimenType(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(386f, y + 5.6f, 70f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(6.5f), "Sample Type")) }
    override fun lblSpecimenDate(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(215f, y - 9.4f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(6.5f), "Sample Collection Date")) }
    override fun lblReceiptReportDate(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(386f, y - 9.4f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(6.5f), "Receipt/Report Date")) }

    override fun lblMedicalInstitution(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(297f, y + 50f, 300f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(6.5f), dto.medicalInstitution)) }
    override fun lblRequestNumber(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {stream.paragraph(297f, y + 20.6f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(6.5f), dto.requestNumber))}
    override fun lblPatientName(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {stream.paragraph(297f, y + 35.3f, 300f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(6.5f), dto.patientName))}
    override fun lblAgeSex(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(297f, y + 5.6f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(6.5f), Util.dashIfEmpty(dto.age!!)), TextBlock(resource().styleContentRegualar().fontSize(6.5f), " / "), TextBlock(resource().styleContentRegualar().fontSize(6.5f), Util.dashIfEmpty(sex(dto.sex))))}
    override fun lblMedicalRecordNumber(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(468f, y + 20.6f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(6.5f), Util.dashIfEmpty(dto.medicalRecordNumber!!)))}
    override fun lblSpecimenType(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(468f, y + 5.6f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(6.5f), dto.specimenType)) }
    override fun lblSpecimenDate(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(297f, y - 9.4f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(6.5f), Util.dashIfEmpty(date(dto.collectionDate)!!)))}
    override fun lblReceiptReportDate(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(468f, y - 9.4f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(6.5f), Util.dashIfEmpty(date(dto.receiptDate)!!)), TextBlock(resource().styleContentRegualar().fontSize(6.5f), " / "), TextBlock(resource().styleContentRegualar().fontSize(6.5f), Util.dashIfEmpty(date(dto.reportDate)!!)))}

    override fun lblIntroHeader(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f), "AI-powered Blood Test for Cancer Screening")) }
    override fun lblIntroContent(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(48f, y-29, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().fontSize(7f), "ai-CANCERCH is the multi cancer early detection(MCED) test powered by artificial intelligence(AI) that has trained distinctive DNA patterns from approximately\n" +
            "3,500 cancer patients and healthy individuals. It analyzes DNA patterns to predict the likelihood of the 6 major types of cancers. The test results indicate the\n" +
            "potential presence of circulating tumor DNA and require further diagnostic confirmations.")) }

    override fun lblOverviewTitle(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(297f, y + 11, 300f, AlignHorizontal.CENTER, TextBlock(resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "Test Results")) }
    override fun lblDoubtSquareTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float) { stream.paragraph(562f - width/2,y - SQUARE_RATE + 53, 300f, AlignHorizontal.CENTER, TextBlock(resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f),"Abnormal Patterns")) }
    override fun lblDoubtSquareContent(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto) {
        val colors = when(dto.result) {
            CancerchDto.Results.GENERAL     -> Color(141, 197, 86)
            CancerchDto.Results.CONCERN     -> Color(239, 167, 24)
            else                            -> Color(217,  52, 29)
        }
        when (dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(562f - width/2,y - SQUARE_RATE+12, 300f, AlignHorizontal.CENTER,
                TextBlock(resource().styleContentSpecial().clone().color(colors).fontSize(18f), "Not detected"))
            else -> stream.paragraph(562f - width/2,y - SQUARE_RATE+12, 300f, AlignHorizontal.CENTER,
                TextBlock(resource().styleContentSpecial().clone().color(colors).fontSize(18f), "Detected"))
        }
    }

    override fun lblOverviewResultImg(stream: PDPageContentStreamPageAccessible, y: Float, TITLE_RATE: Float, dto: CancerchDto) {
        val RESULT_IMAGE_RATE = 62f

        when(dto.result) {
            CancerchDto.Results.GENERAL -> {
                val img = resource().imgTotalResultLowRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 295f - width / 2, y - TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.CONCERN -> {
                val img = resource().imgTotalResultMiddleRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 295f - width / 2, y - TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.RISK -> {
                val img = resource().imgTotalResultHighRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 295f - width / 2, y - TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
        }
    }
    override fun lblOverviewResult(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, TITLE_RATE: Float, dto: CancerchDto) {
        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(
                187f,
                y - TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(resource().styleContentSpecial().clone().fontSize(20f).color(Color(141, 197, 86)), lblResultToWord(dto.result)))
            CancerchDto.Results.CONCERN -> stream.paragraph(
                212f,
                y - TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(resource().styleContentSpecial().clone().fontSize(20f).color(Color(239, 167, 24)), lblResultToWord(dto.result)))
            CancerchDto.Results.RISK -> stream.paragraph(
                187f,
                y - TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(resource().styleContentSpecial().clone().fontSize(20f).color(Color(217, 52, 29)), lblResultToWord(dto.result)))
        }
    }
    override fun lblDoubtContentTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float) {
        stream.paragraph(
            562f - width / 2, y - SQUARE_RATE + 121, 300f, AlignHorizontal.CENTER,
            TextBlock(resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "Predicted Tumor of Origin")
        )
    }

    override fun lblDoubtContentLarge(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto) {
        val style = TextStyle().color(resource().colorText()).fonts(resource().fontHeader(), resource().fontDefault()).color(Color(67, 72, 142)).fontSize(12f)

        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(566f - width / 2, y - SQUARE_RATE + 75, 400f, AlignHorizontal.CENTER, TextBlock(style, "Not applicable"))
            CancerchDto.Results.CONCERN -> stream.paragraph(566f - width / 2, y - SQUARE_RATE + 87, 600f, AlignHorizontal.CENTER, TextBlock(style, "Not Specified : "), TextBlock(resource().styleContentBold().color(Color(67, 72, 142)).fontSize(12f),"Follow-up is recommended"))
            CancerchDto.Results.RISK -> stream.paragraph(437f - width / 2, y - SQUARE_RATE + 80, 400f, AlignHorizontal.LEFT, TextBlock(style, "Most similar to the DNA patterns of [ "), TextBlock(resource().styleContentBold().color(Color(67, 72, 142)).fontSize(12f), (if (dto.first.name == "기타암종") "Other Cancers" else cancerToEng(dto.first.name))), TextBlock(style, " ]"))
        }
    }

    override fun lblDoubtContentSmall(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto) {
        val style = resource().styleContentRegualar().clone().color(Color(72, 71, 71)).fontSize(9f)
        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(566f - width / 2, y - SQUARE_RATE + 48, 300f, AlignHorizontal.CENTER, TextBlock(style, "The DNA patterns analysis shows low probability of cancer."))
            CancerchDto.Results.CONCERN -> {
                stream.paragraph(566f - width / 2, y - SQUARE_RATE + 62, 300f, AlignHorizontal.CENTER, TextBlock(style, "The possibility of cancer has been found, follow-up is recommended.\n" +
                        "This test predicts the most similar cancer type among 6 major cancers.\n" +
                        "Further analysis for other cancer types is not optimized."))
                stream.paragraph(
                    566f - width / 2, y - SQUARE_RATE + 25f, 300f, AlignHorizontal.CENTER, TextBlock(
                        style.clone().fontSize(7f), "* 6 cancer types: Lung, Colon, Liver, Pancreatobiliary, Esophageal, and Ovarian cancer."
                    )
                )
            }
            CancerchDto.Results.RISK -> {
                stream.paragraph(
                    437f - width / 2, y - SQUARE_RATE + 61f, 300f, AlignHorizontal.LEFT, TextBlock(
                        style, "This test predicts the most similar cancer type among 6 major cancers.\n" +
                                "Further analysis for other cancer types is not optimized."
                    )
                )
                stream.paragraph(
                    437f - width / 2, y - SQUARE_RATE + 31f, 300f, AlignHorizontal.LEFT, TextBlock(
                        style.clone().fontSize(7f), "*6 cancer types: Lung, Colon, Liver, Pancreatobiliary, Esophageal, and Ovarian cancer."
                    )
                )
            }
        }
    }

    override fun lblDetailResultAnalysisTableHeaderTop(stream: PDPageContentStreamPageAccessible, y: Float) {
        stream.paragraph(113f,y +85,200f,AlignHorizontal.CENTER,TextBlock(resource().styleContentRegualar().clone().color(Color(0, 0, 0)).fontSize(12f), "Suspected Cancer Type"))
    }
    override fun lblDetailResultAnalysisTableContentTop(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val styleBold = resource().styleContentBold().clone().fontSize(26f)
        if (dto.result == CancerchDto.Results.RISK) {
            if (dto.first.name == "기타암종") {
                stream.line(375f, y + 67, 375f, y - 98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
                stream.paragraph(365f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f), cancerToEng(dto.first.name)))
            } else {
                stream.line(375f, y + 113, 375f, y - 98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
                stream.paragraph(290f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(13f), cancerToEng(dto.first.name)))
                stream.paragraph(468f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(13f), "Others"))
            }
        } else if (dto.result == CancerchDto.Results.CONCERN) {
            stream.line(375f, y + 67, 375f, y - 98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
            stream.paragraph(375f, y + 85, 400f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f),  "Not Specified : Follow-up is recommended"))
        } else {
            stream.paragraph(375f, y + 85, 400f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f),  "N/A"))
        }
    }
    override fun lblDetailResultAnalysisTableHeaderBot(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val styleRegular = resource().styleContentRegualar().clone().fontSize(6f)
        if (dto.result != CancerchDto.Results.GENERAL) {
            stream.rect(45f, y-27f, 138f, 53f).setNonStrokingColor(Color(242,242,247)).fill()
            if(dto.result == CancerchDto.Results.RISK) stream.rect(56f, y-40f, 10f, 10f).setNonStrokingColor(Color(217,  52, 29)).fill()
            else stream.rect(56f, y-40.5f, 10f, 10f).setNonStrokingColor(Color(239, 167, 24)).fill()
            stream.rect(56f, y-59f, 10f, 10f).setNonStrokingColor(Color(108,109,112)).fill()
            stream.paragraph(72f, y - 45, 110f, AlignHorizontal.LEFT, AlignVertical.MIDDLE, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), dto.patientName!!))
            stream.paragraph(71f, y - 67, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), lblPatientInfo(dto.age!!, dto.sex!!)))
            stream.paragraph(113f, y, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "Risk comparison"))
        } else {
            stream.paragraph(113f, y-20, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "Risk comparison"))
        }
    }
    override fun lblDetailResultAnalysisTableContentBot(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto){
        val styleRegular = resource().styleContentRegualar().clone().fontSize(6f)
        val styleBold = resource().styleContentBold().clone().fontSize(26f)
        val CONTENT_SQUARE_RATE = 12f
        val RESULT_IMAGE_LOW_RATE = 110f
        val colors = when (dto.result) {
            CancerchDto.Results.GENERAL -> Color(141, 197, 86)
            CancerchDto.Results.CONCERN -> Color(239, 167, 24)
            else -> Color(217, 52, 29)
        }

        if (dto.result == CancerchDto.Results.GENERAL) {
            stream.paragraph(375f, y-20, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f), "Low probability of cancer"))
        } else if (dto.result == CancerchDto.Results.CONCERN) {
            var img = resource().imgBackgroundCancer(dto.first.name)
            var width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 290 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

            img = resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 258 - width / 2, y-98, width, height)
            stream.paragraph(258f, y-98 + height + 10, 60f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10f), "About 1x"))

            img = resource().imgBarMiddle()
            height = CONTENT_SQUARE_RATE * 4f
            stream.drawImage(img, 318 - width / 2, y-98, width, height)
            stream.paragraph(318f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(13f), "About 2x higher"))
            stream.paragraph(470f, y, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11, 11, 11)).clone().fontSize(9f), "It is unclear to predict it as a specific\ncancer, but the possibility of cancer is\npredicted to be about "), TextBlock(styleRegular.color(Color(239, 167, 24)).clone().fontSize(9f),"twice as high."))
        } else {
            var img = resource().imgBackgroundCancer(dto.first.name)
            var width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 290 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

            img = resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 258 - width / 2, y-98, width, height)
            stream.paragraph(258f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10f), "About 1x"))

            img = resource().imgBarDanger()
            height = CONTENT_SQUARE_RATE * 8f
            stream.drawImage(img, 318 - width / 2, y-98, width, height)
            stream.paragraph(318f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(11f), "About 10x higher"))
            if (dto.first.name != "기타암종") {
                img = resource().imgBackgroundCancer("기타암종")
                width = img.width * RESULT_IMAGE_LOW_RATE / img.height
                stream.drawImage(img, 468 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

                img = resource().imgBarGray()
                width = img.width * CONTENT_SQUARE_RATE / img.height

                height = CONTENT_SQUARE_RATE
                stream.drawImage(img, 438 - width / 2, y-98, width, height)
                stream.paragraph(438f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10f), "About 1x"))

                img = resource().imgBarDanger()
                height = CONTENT_SQUARE_RATE * 5f
                stream.drawImage(img, 498 - width / 2, y-98, width, height)
                stream.paragraph(498f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(11f), "About 5x higher"))
            } else {
                stream.paragraph(470f, y, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11, 11, 11)).clone().fontSize(9f), "Possibility of cancer other than\n6 types of cancer is predicted to be\nabout "), TextBlock(styleRegular.color(Color(217, 52, 29)).clone().fontSize(9f),"10 times higher"))
            }
        }
    }

    override fun lblDangerTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float) {
        val style = resource().styleContentSpecial().fontSize(15f).color(Color(255, 255, 255))
        stream.paragraph(560f - width / 2, y + 45, 120f, AlignHorizontal.CENTER, TextBlock(style, "Cancer Risk"))
    }
    override fun lblDangerIntro(stream: PDPageContentStreamPageAccessible, y: Float) {
        val style = resource().styleContentRegualar().clone().fontSize(6.5f).color(Color(72, 71, 71))
        stream.paragraph(295f, y + 13, 300f, AlignHorizontal.CENTER, TextBlock(style, "Cancer risk is compared to the risk of similar demographics groups (age and gender)"))
    }
    override fun lblDangerCancerName(stream: PDPageContentStreamPageAccessible, x: Float, y: Float, cancer: String) {
        val style = resource().styleContentRegualar().clone().fontSize(6.5f).color(Color(35, 24, 15))
        val cancerName = when (cancer) {
            "폐암"         -> "Lung Cancer"
            "대장암"       -> "Colon Cancer"
            "간암"         -> "Liver Cancer"
            "췌장담도암"   -> "Pancreatobiliary\nCancer"
            "식도암"       -> "Esophageal\nCancer"
            "난소암"       -> "Ovarian Cancer"
            "기타암종"     -> "Other Cancers"
            "유방암"       -> "유방암"
            else -> ""
        }
        stream.paragraph(x - 36, y + 9, 80f, AlignHorizontal.CENTER, AlignVertical.BOTTOM, TextBlock(style, cancerName))
    }

    override fun lblDangerGraphGuide(stream: PDPageContentStreamPageAccessible, x: Float, y: Float) {
        val style = resource().styleContentRegualar().clone().fontSize(5f).color(Color(114, 113, 113))
        stream.paragraph(x + 24, y + 7, 100f, AlignHorizontal.CENTER, TextBlock(style, "Average Risk    Examinee"))
    }
    override fun lblDangerTMI(stream: PDPageContentStreamPageAccessible, width: Float, y: Float, dto: CancerchDto) {
        val DANGER_CONTENT_RATE = 318f
        val style = resource().styleContentRegualar().clone().fontSize(6f).color(Color(159, 160, 160))
        stream.paragraph(110f - width / 2, y - DANGER_CONTENT_RATE +if(dto.result == CancerchDto.Results.RISK && dto.first.name == "기타암종") -100 else +54, 500f, AlignHorizontal.LEFT, TextBlock(style, "* The average risk for each cancer type corresponds to prevalence among similar age and gender demographics as the examinee. (Annual report of cancer statistics in Korea in 2020)"))
    }

    override fun lblDetailResultAnalysisGeneral(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val RESULT_IMAGE_COMMENT_RATE = 201f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(8f)
        val styleBold = resource().styleContentBold().clone().fontSize(8f)

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
    }

    override fun lblDetailResultAnalysisConcern(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val RESULT_IMAGE_COMMENT_RATE = 201f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(8f)
        val styleBold = resource().styleContentBold().clone().fontSize(8f)
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
            TextBlock(styleRegular, "and DNA patterns is similar to that of cancer patients.\nAs a result of cfDNA analysis by artificial intelligence algorithm, "),
            TextBlock(styleBold, "${dto.patientName} "),
            TextBlock(styleRegular, "has a "),
            TextBlock(styleBold, "twice higher "),
            TextBlock(styleRegular, "cancer risk. But it is unclear to predict the origin of tumor. It is able to take several months to develop cancer even with the "),
            TextBlock(styleBold, "Intermediate Risk "),
            TextBlock(styleRegular, "result.\nIt is recommended for follow-up and monitoring.\n\n"),
            TextBlock(blackbold, "Note: Even normal people may be reported as subjects of Intermediate Risk depending on their health status (benign disease, autoimmune disease, etc.) (about 5%).")
        )
    }

    override fun lblDetailResultAnalysisCancer(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val RESULT_IMAGE_COMMENT_RATE = 201f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(8f)
        val styleBold = resource().styleContentBold().clone().fontSize(8f)
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
            TextBlock(styleBold, "${cancerToEng(dto.first.name)} "),
            TextBlock(styleRegular, " patients.\nAs a result of cfDNA analysis by artificial intelligence algorithm, "),
            TextBlock(styleBold, "${dto.patientName} "),
            TextBlock(styleRegular, "has a high probability of "),
            TextBlock(styleBold, cancerToEng(dto.first.name)),
            TextBlock(styleRegular, ", predicted to be more than 10 times higher than the general population.\n"),
            TextBlock(styleBold, lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
            TextBlock(styleRegular, "typically have a "),
            TextBlock(styleBold, (round(dto.first.asr.div(1000) * 10000) / 10000).toString() + "% "),
            TextBlock(styleRegular, "occurrence rate ("),
            TextBlock(styleBold, dto.first.asr.toString()),
            TextBlock(styleRegular, " out of 100,000 individuals), but the probability for "),
            TextBlock(styleBold, "${dto.patientName} "),
            TextBlock(styleRegular, "to have "),
            TextBlock(styleBold, "${cancerToEng(dto.first.name)} "),
            TextBlock(styleRegular, "is approximately "),
            TextBlock(styleBold, dto.first.ppv.toString() + "%"),
            TextBlock(styleRegular, ".\nIt is able to take several months to develop cancer even with the "),
            TextBlock(styleBold, "High Risk"),
            TextBlock(styleRegular, " result.\nIt is recommended for follow-up and monitoring.\n\n"),
            TextBlock(blackbold, "Note: Even normal people may be reported as subjects of High Risk depending on their health status (benign disease, autoimmune disease, etc.) (about 1%).")
        )
    }
    override fun lblDetailResultAnalysisOthers(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val RESULT_IMAGE_COMMENT_RATE = 201f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(8f)
        val styleBold = resource().styleContentBold().clone().fontSize(8f)
        val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(8f)

        stream.paragraph(
            52f,
            y + RESULT_IMAGE_COMMENT_RATE + 277,
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
            TextBlock(styleBold, lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
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

    override fun lblGuideLineHeader(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val style = resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
        val title = when(dto.result) {
            CancerchDto.Results.GENERAL -> "General Cancer Screening Guidelines"
            else -> "Personalized Guidelines"
        }
        stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(style, title))
    }

    override fun lblGuideLineTableHedaer0(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto) {
        val CONTENT_CANCER_RATE = 60f
        val style = resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(if(dto.first.name == "췌장담도암") 10f else 12f)
        val cancer = when(cancerToEng(dto.first.name)) {
            "Others" -> "Other cancer"
            else -> cancerToEng(dto.first.name)
        }
        stream.paragraph(144f, y + rate - 92 + CONTENT_CANCER_RATE * 0.5f, 80f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(style, cancer))
    }
    override fun lblGuideLineTableHeader1(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float) {
        val CONTENT_CANCER_RATE = 60f
        stream.paragraph(280f, y + rate - 68 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
            TextBlock(resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(9f), "Diagnostic test")
        )
    }
    override fun lblGuideLineTableHeader2(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto) {
        val RESULT_CONTENT_OTH_RATE = 182f
        val CONTENT_CANCER_RATE = 60f
        when(dto.result) {
            CancerchDto.Results.RISK -> stream.paragraph(468f, y + rate - 68 + CONTENT_CANCER_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(9f), "ai-CANCERCH Test")
            )
            else -> stream.paragraph(380f, y + RESULT_CONTENT_OTH_RATE - 129 + RESULT_CONTENT_OTH_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(9f), "ai-CANCERCH Test")
            )
        }

    }
    override fun lblGuideLineTop(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val styleBold = resource().styleContentBold().clone().fontSize(11f)
        when (dto.result) {
            CancerchDto.Results.CONCERN -> stream.paragraph(305f, y, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, "Intermediate Risk Guidelines for ${dto.patientName}"))
            else -> {
                val cancer = when(cancerToEng(dto.first.name)) {
                    "Others" -> "Other cancer"
                    else -> cancerToEng(dto.first.name)
                }
                stream.paragraph(305f, y, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, "$cancer Guidelines for ${dto.patientName}."))
            }
        }
    }

    override fun lblGuideLineDetection(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float) {
        stream.paragraph(238f, y+rate-74, 200f, AlignHorizontal.LEFT,
            TextBlock(resource().styleContentRegualar().clone().fontSize(9.5f),  "Consult to a Physician")
        )
    }

    override fun lblGuideLineTime(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto) {
        val RESULT_CONTENT_OTH_RATE = 182f
        val time = when (dto.first.name) {
            "폐암" -> "Retest After 3 months"
            "대장암" -> "Retest After 3 months"
            "간암" -> "Retest After 3 months"
            "췌장담도암" -> "Retest After 3 months"
            "식도암" -> "Retest After 3 months"
            "유방암" -> "Retest After 3 months"
            "난소암" -> "Retest After 3 months"
            else -> "Retest After 3 months"
        }
        when (dto.result) {
            CancerchDto.Results.RISK -> stream.paragraph(468f, y+rate-74, 160f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().fontSize(10f), time))
            else -> stream.paragraph(380f, y+ RESULT_CONTENT_OTH_RATE -75, 160f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().fontSize(10f), time))
        }
    }

    override fun lblGuideLineNormalHeader(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleBold = resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(11f)
        stream.paragraph(59f,  y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold,"Type"))
        stream.paragraph(178f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "Target"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "Intervals"))
        stream.paragraph(440f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "Test"))
    }

    override fun lblGuideLineNormalCancer(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentBold().clone().color(Color(23,18,15)).fontSize(7f)
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Lung"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Colon"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Liver"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Pancreatobiliary"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Esophageal"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Ovarian"))
    }

    override fun lblGuideLineNormalTarget(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentRegualar().clone().color(Color(23,18,15)).fontSize(7f)
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -34, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Individuals aged 54 to 74 with a smoking\nhistory of ≥ 30 pack-years*"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -73, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Individuals aged 50 or older"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -100, 200f, AlignHorizontal.LEFT,
            TextBlock(styleRegular, "Individuals aged 40 or older and at \nhigh risk of liver cancer\n"),
            TextBlock(styleRegular.clone().fontSize(6f),"(Those who have cirrhosis or test positive for the\nhepatitis B virus antigen or the hepatitis C virus antibody)"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -144, 200f, AlignHorizontal.LEFT,
            TextBlock(styleRegular.clone().fontSize(7f), "Individuals aged 70 or older \n"),
            TextBlock(styleRegular.clone().fontSize(6f),
            "(Family history of pancreatobiliary cancer/ long\nterm smoker / medical history of chronic pancreatitis)"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -184, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Individuals who have any symptoms or\nare suspected to have esophageal cancer"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -215, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "-"))
    }

    override fun lblGuideLineNormalFrequency(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentRegualar().clone().color(Color(23,18,15)).fontSize(7f)

        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Every 2 years"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Every 1 year"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Every 6 months"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Every 1 year"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -182, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Regular\ncheck-ups"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "-"))
    }

    override fun lblGuideLineNormalTest(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentRegualar().clone().color(Color(23,18,15)).fontSize(7f)

        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Low-dose chest CT"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -62, 220f, AlignHorizontal.LEFT, TextBlock(styleRegular.fontSize(5.5f), "If there are any abnormal findings in a fecal occult blood test,\ncolonoscopy can be considered.\n"), TextBlock(styleRegular.clone().fontSize(5.5f), "(If a colonoscopy is not available, a double contrast brium enema\nmay be an option.)"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -113, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular,"Liver ultrasound, AFP test"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Abdominal ultrasound, CT"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Esophageal-Gastric endoscopy"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -211, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular, "If there are any abnormal findings in a CA125 test, ultrasound,\nCT or MRI can be considered."))
    }

    override fun lblGuideLineCaption(stream: PDPageContentStreamPageAccessible, y:Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(7f)

        stream.paragraph(400f, y+ RESULT_CONTENT_LOW_RATE -232, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(55,55,55)).fontSize(6f), "*Pack-years: Average daily smoking amount (packs)")
        )
    }

    override fun lblGuideLineComment(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val content =  when(dto.first.name) {
            "폐암" -> "For a lung cancer High Risk result, consultation with the physician for follow-ups is recommended. Although lung cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                    "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

            "대장암" -> "For a colon cancer High Risk result, consultation with the physician for follow-ups is recommended. Although colon cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                    "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

            "간암" -> "For a liver cancer High Risk result, consultation with the physician for follow-ups is recommended. Although liver cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                    "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

            "췌장담도암" -> "For a pancreatobiliary cancer High Risk result, consultation with the physician for follow-ups is recommended. Although pancreatobiliary cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                    "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

            "식도암" -> "For a esophageal cancer High Risk result, consultation with the physician for follow-ups is recommended. Although esophageal cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                    "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

            "유방암" -> ""

            "난소암" -> "For a ovarian cancer High Risk result, consultation with the physician for follow-ups is recommended. Although ovarian cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                    "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

            else -> "For the High Risk result, consultation with the physician for diagnostic test is recommended. Although cancer is not\n" +
                    "confirmed through further test, potential risks for other cancer types can not be excluded.\n" +
                    "When you have the symptoms of other cancers, PET-CT test can be considered.\n" +
                    "If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."
        }
        stream.paragraph(138f, y+20, 420f, AlignHorizontal.LEFT, AlignVertical.MIDDLE, TextBlock(resource().styleContentRegualar().clone().fontSize(8f), content))
    }
    override fun lblGuideLineConcern(stream: PDPageContentStreamPageAccessible, y: Float) {
        stream.paragraph(128f, y+48, 500f, AlignHorizontal.LEFT,
            TextBlock(resource().styleContentRegualar().clone().fontSize(8f), "For the Intermediate Risk result, ai-CANCERCH test after three months for follow ups is recommended.\n" +
                    "The Intermediate Risk is a case in which abnormal DNA patterns is observed but the possibility of temporal abnormality\n" +
                    "due to the health status(benign disease, autoimmune disease, etc.) cannot be excluded.\n" +
                    "Please consider undergoing the ai-CANCERCH to monitor cancer risk every three months.\n" +
                    "If you have the symptoms for specific cancers, further test through the consultation with the physician is recommended.")
        )
    }

    override fun lblLimitationHeader(stream: PDPageContentStreamPageAccessible, y: Float) {
        stream.paragraph(297f, y+10, 200f, AlignHorizontal.CENTER, TextBlock(resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "Disclaimers"))
    }
    override fun lblLimitationTableHeader(stream: PDPageContentStreamPageAccessible, y: Float, col: Int) {
        when (col) {
            0 -> stream.paragraph(88f, y+117, 100f, AlignHorizontal.CENTER,  TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "Cancer Type"))
            1 -> stream.paragraph(188f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "Specificity¹⁾"))
            2 -> stream.paragraph(293f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "Sensitivity²⁾"))
            3 -> stream.paragraph(392f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "PPV³⁾"))
            else -> stream.paragraph(498f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "NPV⁴⁾"))
        }
    }

    override fun lblLimitationTableTotalCancer(col: Int) = when (col) {
        0 -> "Overall"
        1 -> "96.5%"
        2 -> "81.1%"
        3 -> "74.4%"
        else -> "97.6%"
    }

    override fun lblLimitationTableLungCancer(col: Int) = when (col) {
        0 -> "Lung"
        1 -> "96.5%"
        2 -> "73.1%"
        3 -> "12.7%"
        else -> ">98%"
    }

    override fun lblLimitationTableColorCancer(col: Int) = when (col) {
        0 -> "Colon"
        1 -> "96.5%"
        2 -> "70.1%"
        3 -> "17.8%"
        else -> ">98%"
    }

    override fun lblLimitationTableLiverCancer(col: Int) = when (col) {
        0 -> "Liver"
        1 -> "96.5%"
        2 -> "94.5%"
        3 -> "9.8%"
        else -> ">98%"
    }

    override fun lblLimitationTablePanCancer(col: Int) = when (col) {
        0 -> "Pancreatobiliary"
        1 -> "96.5%"
        2 -> "91.3%"
        3 -> "6.8%"
        else -> ">98%"
    }

    override fun lblLimitationTableEsopCancer(col: Int) = when (col) {
        0 -> "Esophageal"
        1 -> "96.5%"
        2 -> "88.4%"
        3 -> "2.0%"
        else -> ">98%"
    }

    override fun lblLimitationTableOverCancer(col: Int) = when (col) {
        0 -> "Ovarian"
        1 -> "96.5%"
        2 -> "70.4%"
        3 -> "1.7%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Header(col: Int) = when (col) {
        0 -> "Cancer Type"
        1 -> "Method"
        2 -> "Specificity"
        3 -> "Sensitivity"
        4 -> "PPV"
        else -> "NPV"
    }

    override fun lblLimitationTable2Row1(col: Int) = when (col) {
        0 -> "Lung"
        1 -> "Low-dose Chest CT"
        2 -> "~ 92.6%"
        3 -> "~ 88.9%"
        4 -> "~ 6.3%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row2(col: Int) = when (col) {
        0 -> "Colon"
        1 -> "Stool Occult Blood Test"
        2 -> "95.4%"
        3 -> "~ 40.0%"
        4 -> "~ 7.5%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row3(col: Int) = when (col) {
        0 -> "Colonoscopy"
        1 -> "~ 99.0%"
        2 -> "~ 85.0~95.0%"
        3 -> "~ 44.1~46.9%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row4(col: Int) = when (col) {
        0 -> "Liver"
        1 -> "Liver ultrasound & AFP test"
        2 -> "~ 94.0%"
        3 -> "~ 80.0%"
        4 -> "~ 4.5%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row5(col: Int) = when (col) {
        0 -> "Pancreatobiliary"
        1 -> "CA19-9 Test"
        2 -> "~ 79.9~85.3%"
        3 -> "~ 76.1~80.2%"
        4 -> "~ 0.3~0.4%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row6(col: Int) = when (col) {
        0 -> "Esophageal"
        1 -> "Endoscopy"
        2 -> "~ 79.0%"
        3 -> "~ 62.0%"
        4 -> "~ 0.2%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row7(col: Int) = when (col) {
        0 -> "Ovarian"
        1 -> "CA125 Test"
        2 -> "~ 95.0%"
        3 -> "~ 43.3%"
        4 -> "~ 0.6%"
        else -> ">98%"
    }

    override fun lblLimitation(stream: PDPageContentStreamPageAccessible, y: Float, row: Int) {
        when (row) {
            0 -> stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "This test screens for cancer by analyzing patterns in cfDNA, and a cancer signal does not indicate a diagnosis of cancer."))
            1 -> stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "This test cannot detect all types of cancer and the test performance may differ depending on the stage or type of cancer."))
            2 -> stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "This test is developed using Lung, Colon, Liver, Pancreatobiliary, Esophageal, and Ovarian cancer sample data. Other cancer types cannot be analyzed accurately."))
            3 -> stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "The sensitivity may vary depending on the location and genetic characteristics of the cancer."))
            4 -> stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "The test performance and tested cancer type can be modified according to the ML-algorithm improvement."))
            5 -> stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "This test may be reported as false positive in the examinee with benign diseases, autoimmune diseases, etc., and may be reported as false negative in case of\n" +
                    "having chemotherapy, cell therapy, etc."))
            else -> stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), ""))
        }
    }

    override fun lblLimitationDescription(stream: PDPageContentStreamPageAccessible, y: Float, row: Int) {
        when (row) {
            0 -> stream.paragraph(42f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), "1) Specificity: Indicates the proportion where the ai-CANCERCH test classifies a healthy individual to the low risk group."))
            1 -> stream.paragraph(42f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), "2) Sensitivity: Indicates the proportion where the ai-CANCERCH test classifies a cancer patient to high or intermediate risk group."))
            2 -> stream.paragraph(42f, y, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), "3) PPV: Positive Predictive Value. Represents the proportion of subjects identified by the ai-CANCERCH test as part of the high or intermediate risk group who are actual cancer patients. PPV has been calculated based on the\n" +
                 "   prevalence of the 50s and above Korean."))
            3 -> stream.paragraph(42f, y-6, 600f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), "4) NPV: Negative Predictive Value. Represents the proportion of subjects identified by the ai-CANCERCH test as part of the low risk group who are healthy individuals. NPV has been calculated based on the prevalence of the 50s and\n" +
                 "   above Korean."))
            else -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), ""))
        }
    }

    override fun lblReferenceTitle() = "References"

    override fun lblReferenceLeft(): String {
        return "1.\tCancer Biol Ther. 2019; 20(8): 1057–1067.\n" +
                "2.\tMutat Res. Jul-Sep 2019;781:100-129.\n" +
                "3.\tBMC Cancer. 2017; 17: 697.\n" +
                "4.\tCancer Resaerch. 2022:82(12, Supplement):6371-6371\n" +
                "5.\tBr J Cancer. 2008;98(10):1602-7.\n" +
                "6.\tJournal of Korean Society of Gastrointestinal Endoscopy,\n" +
                "\t2007;35(2): 68-73."
    }

    override fun lblReferenceRight(): String {
        return "7.\tJAMA . 2016;315(23):2564-2575.\n" +
                "8.\tAliment Pharmacol Ther. 2009;30(1):37-47.\n" +
                "9.\tOnco Targets Ther. 2016;9:7459-7467.\n" +
                "10.\tCurr Mol Med. 2013;13(3):340-51.\n" +
                "11.\tWorld J Gastroenterol. 2015;21(26):7933-43.\n" +
                "12.\tGynecol Oncol. 2008;108(2):402-8."

    }

    override fun lblReferenceDescription(stream: PDPageContentStreamPageAccessible, y: Float) {
        stream.paragraph(77f, y, 600f, AlignHorizontal.LEFT,
            TextBlock(resource().styleContentRegualar().clone().color(Color(151,151,151)).fontSize(6.5f), "※This test has not established the clinical significance of its results, and there is still insufficient evidence for the utility of health-related actions based on it.\n" +
                    "※This test was developed and its performance characteristics determined by GC Genome."),
        )
    }

    override fun lblPerformance(): String {
        return "ai-CANCERCH Performance"
    }

    override fun lblPerformanceBasic(): String = "Other Screening Test Performance"

    override fun lblResultToWord(result: CancerchDto.Results) = when (result) {
        CancerchDto.Results.GENERAL -> "General Risk"
        CancerchDto.Results.CONCERN -> "Intermediate Risk"
        else -> "High Risk"
    }

    override fun lblPatientInfo(age: String, sex: Sex): String {
        val ageStream: String = (age.toInt() / 10 * 10).toString()
        val cut: String = when {
            age.substring(age.length - 1, age.length).toInt() >= 5 -> "in late "
            else -> "in early "
        }
        val sexStr: String = when {
            sex == Sex.F -> "Female "
            else -> "Male "
        }
        return sexStr + cut + ageStream +"s"
    }

    override fun lblPatientInfoWithCancer(age: String, sex: Sex, cancer: String): String {
        val ageStream: String = (age.toInt() / 10 * 10).toString()
        val cut: String = when {
            age.substring(age.length - 1, age.length).toInt() >= 5 -> "late "
            else -> "early "
        }
        val sexStr: String = when {
            sex == Sex.F -> "Female "
            else -> "Male "
        }
        return when(cancer) {
            "기타암종" -> cut + ageStream +"s " + sexStr
            else -> sexStr + cancerToEng(cancer) + " patients in their "+ cut + ageStream + "s "
        }

    }

    override fun lblPatientSir(name: String) = "${name}님"

    override fun lblCancerForOthers() = "암"

    override fun lblDetailResultAnalysisContentOther(patient: String?): String =
        "\n\n" + patient + "님에 대한 기타 암 집중관리군 추가 결과 해석입니다."

    fun cancerToEng(cancer: String) = when (cancer) {
        "폐암" -> "Lung Cancer"
        "대장암" -> "Colon Cancer"
        "간암" -> "Liver Cancer"
        "췌장담도암" -> "Pancreatobiliary Cancer"
        "식도암" -> "Esophageal Cancer"
        "난소암" -> "Ovarian Cancer"
        else -> "Others"
    }
    fun date(date: LocalDate?): String? {
        return if (date == null) null else DTF.format(date)
    }

    fun age(birth: LocalDate?, sampling: LocalDate?): String {
        if (birth == null) return "-"
        return if (sampling == null) (Period.between(
            birth,
            LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        ).years + 1).toString() else (Period.between(
            birth,
            sampling.with(TemporalAdjusters.firstDayOfYear())
        ).years + 1).toString()
    }

    fun sex(sex: Sex?): String {
        return if (sex == null) "-" else when (sex) {
            Sex.M -> "M"
            Sex.F -> "F"
        }
    }
    override fun lblHighRisk() = "10x higher"
    override fun lblMiddleRisk() = "Above\nAverage"
    override fun lblNormalRisk() = "Below\nAverage"
}