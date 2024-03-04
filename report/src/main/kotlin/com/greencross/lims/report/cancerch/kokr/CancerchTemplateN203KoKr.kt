package com.greencross.lims.report.cancerch.kokr

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.builder.Util
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchTemplateN203
import com.greencross.lims.report.cancerch.SectionGuideLine
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import java.awt.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import kotlin.math.round

class CancerchTemplateN203KoKr(
    resource: CancerchResourceN203KoKr,
    testInfo: TestInfo,
) : CancerchTemplateKoKr<CancerchResourceN203KoKr>(testInfo),
    CancerchTemplateN203<CancerchResourceN203KoKr> {
    private val resource: CancerchResourceN203KoKr = resource
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun resource(): CancerchResourceN203KoKr {
        return resource
    }

    override fun lblTitleSmallLogo() = "[Pan-cancer : 주요 암]"
    override fun lblMedicalInstitution(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(222.5f, y + 45.5f, 50f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(8.2f), "의뢰기관")) }
    override fun lblRequestNumber(stream: PDPageContentStreamPageAccessible, y: Float)  { stream.paragraph(382.5f, y + 45.5f, 50f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(8.2f), "접수번호")) }
    override fun lblPatientName(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(222.5f, y + 28f, 50f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(8.2f), "성명")) }
    override fun lblAgeSex(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(382.5f, y + 28f, 50f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(8.2f), "나이/성별")) }
    override fun lblMedicalRecordNumber(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(222.5f, y + 11f, 50f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(8.2f), "등록번호")) }
    override fun lblSpecimenType(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(382.5f, y + 11f, 50f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(8.2f), "검체종류")) }
    override fun lblSpecimenDate(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(222.5f, y - 6, 50f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(8.2f),"검체채취일")) }
    override fun lblReceiptReportDate(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(382.5f, y - 6, 50f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().fontSize(8.2f), "접수일/보고일")) }

    override fun lblMedicalInstitution(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(285f, y + 45.5f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(8.2f), dto.medicalInstitution)) }
    override fun lblRequestNumber(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {stream.paragraph(445f, y + 45.5f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(8.2f), dto.requestNumber))}
    override fun lblPatientName(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {stream.paragraph(285f, y + 28f, 300f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(8.2f), dto.patientName))}
    override fun lblAgeSex(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(445f, y + 28f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(dto.age!!)), TextBlock(resource().styleContentRegualar().fontSize(8.2f), " / "), TextBlock(resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(sex(dto.sex))))}
    override fun lblMedicalRecordNumber(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(285f, y + 11f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(dto.medicalRecordNumber!!)))}
    override fun lblSpecimenType(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(445f, y + 11f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(8.2f), dto.specimenType)) }
    override fun lblSpecimenDate(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(285f, y - 6f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(date(dto.collectionDate)!!)))}
    override fun lblReceiptReportDate(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) { stream.paragraph(445f, y - 6f, 100f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(date(dto.receiptDate)!!)), TextBlock(resource().styleContentRegualar().fontSize(8.2f), " / "), TextBlock(resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(date(dto.reportDate)!!)))}


    override fun lblIntroHeader(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f), "인공지능 액체생검 주요 6종 암 선별검사")) }
    override fun lblIntroContent(stream: PDPageContentStreamPageAccessible, y: Float) { stream.paragraph(51f, y-32, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().fontSize(9f), "아이캔서치 검사는 약 3,500명의 암 환자 및 정상인에서 특징적으로 나타나는 DNA 패턴을 학습한 인공지능으로 수검자의 DNA\n" +
            "패턴을 분석하여 주요 6종 암의 존재 가능성을 예측합니다. 본 검사의 결과는 암의 진단 혹은 완전한 배제를 의미하지 않습니다.")) }

    override fun lblOverviewTitle(stream: PDPageContentStreamPageAccessible, y: Float) {stream.paragraph(297f, y + 11, 300f, AlignHorizontal.CENTER, TextBlock(resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "종 합 결 과")) }
    override fun lblDoubtSquareTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float) {stream.paragraph(562f - width/2,y - SQUARE_RATE + 53, 300f, AlignHorizontal.CENTER, TextBlock(resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f),"이상 패턴 검출 여부")) }
    override fun lblDoubtSquareContent(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto) {
        val colors = when(dto.result){
            CancerchDto.Results.GENERAL     -> Color(141, 197, 86)
            CancerchDto.Results.CONCERN     -> Color(239, 167, 24)
            else                            -> Color(217,  52, 29)
        }
        when (dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(562f - width/2,y - SQUARE_RATE+12, 300f, AlignHorizontal.CENTER,
                TextBlock(resource().styleContentSpecial().clone().color(colors).fontSize(18f), "미검출"))
            else -> stream.paragraph(562f - width/2,y - SQUARE_RATE+12, 300f, AlignHorizontal.CENTER,
                TextBlock(resource().styleContentSpecial().clone().color(colors).fontSize(18f), "검 출"))
        }
    }
    override fun lblOverviewResultImg(stream: PDPageContentStreamPageAccessible, y: Float, TITLE_RATE: Float, dto: CancerchDto) {
        val RESULT_IMAGE_RATE = 64f

        when(dto.result) {
            CancerchDto.Results.GENERAL -> {
                val img = resource().imgTotalResultLowRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 272f - width / 2, y - TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.CONCERN -> {
                val img = resource().imgTotalResultMiddleRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 272f - width / 2, y - TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.RISK -> {
                val img = resource().imgTotalResultHighRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 272f - width / 2, y - TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
        }
    }
    override fun lblOverviewResult(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, TITLE_RATE: Float, dto: CancerchDto) {
        stream.paragraph(
            177f,
            y - TITLE_RATE - 41,
            200f,
            AlignHorizontal.RIGHT,
            when(dto.result) {
                CancerchDto.Results.GENERAL -> TextBlock(resource().styleContentSpecial().clone().fontSize(27f).color(Color(141, 197, 86)), lblResultToWord(dto.result))
                CancerchDto.Results.CONCERN -> TextBlock(resource().styleContentSpecial().clone().fontSize(27f).color(Color(239, 167, 24)), lblResultToWord(dto.result))
                CancerchDto.Results.RISK -> TextBlock(resource().styleContentSpecial().clone().fontSize(27f).color(Color(217, 52, 29)), lblResultToWord(dto.result))
            })
    }
    override fun lblDoubtContentTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float) {
        stream.paragraph(
            562f - width / 2, y - SQUARE_RATE + 121, 300f, AlignHorizontal.CENTER,
            TextBlock(resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "6종 암 중 인공지능 예측 암종")
        )
    }
    override fun lblDoubtContentLarge(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto) {
        val style = TextStyle().color(resource().colorText()).fonts(resource().fontHeader(), resource().fontDefault()).color(Color(67, 72, 142)).fontSize(13f)

        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(423f - width / 2 + 130, y - SQUARE_RATE + 75, 400f, AlignHorizontal.CENTER, TextBlock(style, "해당없음"))
            CancerchDto.Results.CONCERN -> stream.paragraph(423f - width / 2 + 130, y - SQUARE_RATE + 75, 400f, AlignHorizontal.CENTER, TextBlock(style, "해당없음 : 추적관찰 권장"))
            CancerchDto.Results.RISK -> stream.paragraph(323f - width / 2 + 130, y - SQUARE_RATE + 75, 400f, AlignHorizontal.LEFT, TextBlock(style, (if (dto.first.name == "기타암종") "6종 암 외 " else "6종 암 중 ") + lblCancerToWord(dto.first.name) + "의 DNA 패턴과 가장 유사합니다."))
        }
    }

    override fun lblDoubtContentSmall(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto) {
        val style = resource().styleContentRegualar().clone().color(Color(0, 0, 0)).fontSize(10f)
        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(423f - width / 2 + 130, y - SQUARE_RATE + 48, 300f, AlignHorizontal.CENTER, TextBlock(style, "DNA 패턴 분석 결과, 암 존재 가능성이 낮게 예측되었습니다."))
            CancerchDto.Results.CONCERN -> stream.paragraph(423f - width / 2 + 130, y - SQUARE_RATE + 48, 300f, AlignHorizontal.CENTER, TextBlock(style, "DNA 패턴 분석 결과, 6종 암으로 예측되지는 않습니다.\n " +
                    "그러나 암의 존재 가능성이 발견되었으므로 추적관찰을 권장합니다."))
            CancerchDto.Results.RISK -> stream.paragraph(323f - width / 2 + 130, y - SQUARE_RATE + 48, 300f, AlignHorizontal.LEFT, TextBlock(style, "본 검사에 포함된 6종 암 중 가장 유사한 암종에 대해 예측하므로,\n" +
                    "타 암종에 대해서는 정확한 분석이 어렵습니다."))
        }
    }

    override fun lblDetailResultAnalysisTableHeaderTop(stream: PDPageContentStreamPageAccessible, y: Float) {
        val styleRegular = resource().styleContentRegualar().clone().fontSize(6f)
        stream.paragraph(113f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "암종"))
    }

    override fun lblDetailResultAnalysisTableContentTop(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val styleBold = resource().styleContentBold().clone().fontSize(26f)
        if(dto.result == CancerchDto.Results.RISK) {
            if (dto.first.name == "기타암종") {
                stream.line(375f,  y+67, 375f, y-98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
                stream.paragraph(365f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f),"의심 암종 : 기타 암"))
            } else {
                stream.line(375f,  y+113, 375f, y-98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
                stream.paragraph(290f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(13f), "의심 암종 : ${dto.first.name}"))
                stream.paragraph(468f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(13f), "기타 암"))
            }
        } else {
        stream.line(375f, y + 67, 375f, y - 98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
            val results = when (dto.result) {
                CancerchDto.Results.GENERAL -> "해당없음"
                else -> "해당없음 : 추적관찰 권장"
            }
            stream.paragraph(375f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f), results))
        }
    }
    override fun lblDetailResultAnalysisTableHeaderBot(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val styleRegular = resource().styleContentRegualar().clone().fontSize(6f)
        if (dto.result != CancerchDto.Results.GENERAL) {
            stream.rect(45f, y-27f, 138f, 53f).setNonStrokingColor(Color(242,242,247)).fill()
            if(dto.result == CancerchDto.Results.RISK) stream.rect(56f, y-40.5f, 10f, 10f).setNonStrokingColor(Color(217,  52, 29)).fill()
            else stream.rect(56f, y-40.5f, 10f, 10f).setNonStrokingColor(Color(239, 167, 24)).fill()
            stream.rect(56f, y-59f, 10f, 10f).setNonStrokingColor(Color(108,109,112)).fill()
            stream.paragraph(71f, y - 49, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10.5f), lblPatientSir(dto.patientName!!)))
            stream.paragraph(71f, y - 67, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10.5f), lblPatientInfo(dto.age!!, dto.sex!!)))
            stream.paragraph(113f, y, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "위험도 비교"))
        } else {
            stream.paragraph(113f, y-20, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "위험도 비교"))
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
            stream.paragraph(290f, y-20, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(11f), "해당없음"))
            stream.paragraph(470f, y-20, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128, 128, 128)).clone().fontSize(11f), "암의 존재가능성이 낮음"))
        } else if (dto.result == CancerchDto.Results.CONCERN) {
            var img = resource().imgBackgroundCancer(dto.first.name)
            var width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 290 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

            img = resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 258 - width / 2, y-98, width, height)
            stream.paragraph(258f, y-98 + height + 10, 60f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "약 1배"))

            img = resource().imgBarMiddle()
            height = CONTENT_SQUARE_RATE * 4f
            stream.drawImage(img, 318 - width / 2, y-98, width, height)
            stream.paragraph(318f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(13f), "약 2배 이상"))
            stream.paragraph(470f, y, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11, 11, 11)).clone().fontSize(11f), "특정 암으로 예측하기에는\n" +
                    "불분명하나 암 존재 가능성이\n" +
                    "약 2배 이상 높을 것으로 예측됨"))
        } else {
            var img = resource().imgBackgroundCancer(dto.first.name)
            var width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 290 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

            img = resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 258 - width / 2, y-98, width, height)
            stream.paragraph(258f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "약 1배"))

            img = resource().imgBarDanger()
            height = CONTENT_SQUARE_RATE * 8f
            stream.drawImage(img, 318 - width / 2, y-98, width, height)
            stream.paragraph(318f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(13f), "약 10배 이상"))
            if (dto.first.name != "기타암종") {
                img = resource().imgBackgroundCancer("기타암종")
                width = img.width * RESULT_IMAGE_LOW_RATE / img.height
                stream.drawImage(img, 468 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

                img = resource().imgBarGray()
                width = img.width * CONTENT_SQUARE_RATE / img.height

                height = CONTENT_SQUARE_RATE
                stream.drawImage(img, 438 - width / 2, y-98, width, height)
                stream.paragraph(438f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "약 1배"))

                img = resource().imgBarDanger()
                height = CONTENT_SQUARE_RATE * 5f
                stream.drawImage(img, 498 - width / 2, y-98, width, height)
                stream.paragraph(498f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(13f), "약 5배 이상"))
            } else {
                stream.paragraph(470f, y, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11, 11, 11)).clone().fontSize(11f), "6종 암 외의 암 존재 가능성이\n\n약 10배 이상 높을 것으로 예측됨"))
            }
        }
    }
    override fun lblDangerTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float) {
        val style = resource().styleContentSpecial().fontSize(15f).color(Color(255, 255, 255))
        stream.paragraph(560f - width / 2, y + 45, 120f, AlignHorizontal.CENTER, TextBlock(style, "암종별 위험도"))
    }
    override fun lblDangerIntro(stream: PDPageContentStreamPageAccessible, y: Float) {
        val style = resource().styleContentRegualar().clone().fontSize(7.3f).color(Color(72, 71, 71))
        val bold = resource().styleContentBold().clone().fontSize(7.3f)
        stream.paragraph(295f, y + 13, 300f, AlignHorizontal.CENTER, TextBlock(style, "암종별 위험도는 수검자가 "), TextBlock(bold, "실제로 암일 확률"), TextBlock(style, "을 동일집단(동일한 연령대, 성별)과 비교합니다."))
    }
    override fun lblDangerCancerName(stream: PDPageContentStreamPageAccessible, x: Float, y: Float, cancer: String) {
        val style = resource().styleContentRegualar().clone().fontSize(8f).color(Color(35, 24, 15))
        val cancerName = when (cancer) {
            "기타암종"    -> "기타 암종"
            else -> cancer
        }
        stream.paragraph(x - 36, y + 9, 100f, AlignHorizontal.CENTER, TextBlock(style, cancerName))
    }
    override fun lblDangerGraphGuide(stream: PDPageContentStreamPageAccessible, x: Float, y: Float) {
        val style = resource().styleContentRegualar().clone().fontSize(6.5f).color(Color(114, 113, 113))
        stream.paragraph(x + 21, y + 7, 100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))
    }
    override fun lblDangerTMI(stream: PDPageContentStreamPageAccessible, width: Float, y: Float, dto: CancerchDto) {
        val DANGER_CONTENT_RATE = 318f
        val style = resource().styleContentRegualar().clone().fontSize(6.5f).color(Color(159, 160, 160))
        stream.paragraph(110f - width / 2, y - DANGER_CONTENT_RATE +if(dto.result == CancerchDto.Results.RISK && dto.first.name == "기타암종") -100 else +54, 400f, AlignHorizontal.LEFT, TextBlock(style, "* 각 암종별 평균 위험도는 수검자와 동일한 연령대, 성별에서의 유병률에 해당합니다. (국가암등록사업 연례 보고서-2020 암등록통계)"))
    }
    override fun lblDetailResultAnalysisGeneral(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val RESULT_IMAGE_COMMENT_RATE = 201f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(9f)
        val styleBold = resource().styleContentBold().clone().fontSize(9f)
        val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(9f)

        stream.paragraph(
            52f, y + RESULT_IMAGE_COMMENT_RATE - 115, 480f, AlignHorizontal.LEFT,
            TextBlock(styleRegular, "아이캔서치 검사 결과 "),
            TextBlock(styleBold, "${lblResultToWord(dto.result)+"군"}"),
            TextBlock(styleRegular, "인 "),
            TextBlock(styleBold, "${dto.patientName}"),
            TextBlock(styleRegular, "님은 건강인의 DNA패턴과 유사합니다.\n단, 아이캔서치 검사는 모든 암을 검출할 수 없으며, 암의 병기나 종류에 따라 검출 성능이 달라질 수 있습니다.\n본 검사는 수검자의 암 존재 가능성을 확인하는 검사로 정확한 진단을 위한 검사는 아니며,\n확진을 위해서는 의료진 상담을 통한 정밀검사를 권장합니다.\n\n"),
            TextBlock(blackbold, "현재 암 일반관리군으로 분류된 것이 미래에 암이 발병하지 않음을 의미하는 것은 아니므로,\n정기적인 건강검진과 생활습관 관리를 통해 암을 예방할 것을 권장합니다.")
        )
    }
    override fun lblDetailResultAnalysisConcern(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val RESULT_IMAGE_COMMENT_RATE = 201f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(9f)
        val styleBold = resource().styleContentBold().clone().fontSize(9f)
        val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(9f)

        stream.paragraph(
            52f,
            y + RESULT_IMAGE_COMMENT_RATE - 120,
            480f,
            AlignHorizontal.LEFT,
            TextBlock(styleRegular, "아이캔서치 검사 결과 "),
            TextBlock(styleBold, lblResultToWord(dto.result)+"군"),
            TextBlock(styleRegular, "인 "),
            TextBlock(styleBold, "${dto.patientName}"),
            TextBlock(styleRegular, "님은 암 환자의 이상 DNA패턴과 다소 유사합니다.\n혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과, "),
            TextBlock(styleBold, "${dto.patientName}"),
            TextBlock(styleRegular, "님은 암의 존재 가능성이\n"),
            TextBlock(styleBold, "약 2배 이상 "),
            TextBlock(styleRegular, "높을 것으로 예측되나, 암 종을 예측하기에는 불분명합니다.\n\n관심관리 대상자여도 암으로 확진되기까지 수 개월이 걸릴 수도 있으므로 추적관찰을 요합니다.\n또한 "),
            TextBlock(blackbold, "정상인이라도 건강상태(양성질환, 자가면역질환 등)에 따라 관심관리 대상자로 보고될 수 있습니다.(약 5%).")
        )
    }
    override fun lblDetailResultAnalysisCancer(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val RESULT_IMAGE_COMMENT_RATE = 201f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(9f)
        val styleBold = resource().styleContentBold().clone().fontSize(9f)
        val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(9f)

        stream.paragraph(
            52f,
            y + RESULT_IMAGE_COMMENT_RATE - 102,
            480f,
            AlignHorizontal.LEFT,
            TextBlock(styleRegular, "아이캔서치 검사 결과 "),
            TextBlock(styleBold, lblResultToWord(dto.result)+"군"),
            TextBlock(styleRegular, "인 "),
            TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, "님은 "),
            TextBlock(styleBold, dto.first.name),
            TextBlock(styleRegular, " 환자의 이상 DNA패턴과 가장 유사합니다.\n혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과, "),
            TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, "님은 암의 존재 가능성이 다소 높을 것으로\n예측되어 일반인 대비 "),
            TextBlock(styleBold, dto.first.name),
            TextBlock(styleRegular, " 존재 가능성이 약 10배 이상 높을 것으로 예측됩니다.\n일반적으로 "),
            TextBlock(styleBold, lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
            TextBlock(styleRegular, " 환자는 "),
            TextBlock(styleBold, (round(dto.first.asr.div(1000) * 10000) / 10000).toString() + "%"),
            TextBlock(styleRegular, "(10만명 중에 "),
            TextBlock(styleBold, dto.first.asr.toString()+"명"),
            TextBlock(styleRegular, ")의 확률로 발생하지만,\n"),
            TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, "님이 "),
            TextBlock(styleBold, dto.first.name),
            TextBlock(styleRegular, "일 확률은 약 "),
            TextBlock(styleBold, dto.first.ppv.toString() + "%"),
            TextBlock(styleRegular, "입니다.\n\n집중관리 대상자여도 암으로 확진되기까지 수 개월이 걸릴 수도 있으므로 추적관찰을 요합니다.\n또한 "),
            TextBlock(blackbold,"정상인이라도 건강상태(양성질환, 자가면역질환 등)에 따라 집중관리 대상자로 보고될 수 있습니다(약 1%).")
        )
    }
    override fun lblDetailResultAnalysisOthers(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val RESULT_IMAGE_COMMENT_RATE = 201f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(9f)
        val styleBold = resource().styleContentBold().clone().fontSize(9f)
        val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(9f)

        stream.paragraph(
            52f,
            y + RESULT_IMAGE_COMMENT_RATE + 275,
            480f,
            AlignHorizontal.LEFT,
            TextBlock(styleRegular, "아이캔서치 검사 결과 "),
            TextBlock(styleBold, lblResultToWord(dto.result)+"군"),
            TextBlock(styleRegular, "인 "),
            TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, "님은 "),
            TextBlock(styleBold, "기타 암"),
            TextBlock(styleRegular, " 환자의 이상 DNA패턴과 가장 유사합니다.\n혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과, "),
            TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, "님은 암의 존재 가능성이 다소 높을 것으로\n예측되어 일반인 대비 "),
            TextBlock(styleBold, dto.first.name),
            TextBlock(styleRegular, " 존재 가능성이 약 10배 이상 높을 것으로 예측됩니다.\n일반적으로 "),
            TextBlock(styleBold, lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
            TextBlock(styleRegular, " 환자는 "),
            TextBlock(styleBold, (round(dto.first.asr.div(1000) * 10000) / 10000).toString() + "%"),
            TextBlock(styleRegular, "(10만명 중에 "),
            TextBlock(styleBold, dto.first.asr.toString()+"명"),
            TextBlock(styleRegular, ")의 확률로 발생하지만,\n"),
            TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, "님이 "),
            TextBlock(styleBold, "기타 암"),
            TextBlock(styleRegular, "일 확률은 약 "),
            TextBlock(styleBold, dto.first.ppv.toString() + "%"),
            TextBlock(styleRegular, "입니다.\n\n집중관리 대상자여도 암으로 확진되기까지 수 개월이 걸릴 수도 있으므로 추적관찰을 요합니다. 또한 "),
            TextBlock(blackbold,"정상인이라도 건강상태(양성질환, 자가면역질환 등)에 따라 집중관리 대상자로 보고될 수 있습니다(약 1%).\n\n"),
            TextBlock(blackbold, dto.first.comment)
        )
    }
    override fun lblGuideLineHeader(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val style = resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
        val title = when(dto.result) {
            CancerchDto.Results.GENERAL ->
                "암 검진 가이드라인"
            else -> dto.patientName + "님의 맞춤 가이드라인"
        }
        stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(style, title))
    }
    override fun lblGuideLineTableHedaer0(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float,  dto: CancerchDto) {
        val CONTENT_CANCER_RATE = 60f
        val style = resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(if(dto.first.name == "췌장담도암") 12f else 14f)
        stream.paragraph(148f, y + rate - 97 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
            TextBlock(style, if(dto.first.name == "기타암종") "기타 암" else dto.first.name)
        )
    }
    override fun lblGuideLineTableHeader1(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float) {
        val CONTENT_CANCER_RATE = 60f
        stream.paragraph(280f, y + rate - 68 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
            TextBlock(resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(10f), "정밀검사")
        )
    }
    override fun lblGuideLineTableHeader2(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto) {
        val RESULT_CONTENT_OTH_RATE = 182f
        val CONTENT_CANCER_RATE = 60f
        when(dto.result) {
            CancerchDto.Results.RISK -> stream.paragraph(468f, y + rate - 68 + CONTENT_CANCER_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(10f), "아이캔서치 검사 모니터링 권장 기간")
            )
            else -> stream.paragraph(380f, y + RESULT_CONTENT_OTH_RATE - 129 + RESULT_CONTENT_OTH_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(10f), "아이캔서치 검사 모니터링 권장 기간")
            )
        }
    }
    override fun lblGuideLineTop(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val styleBold = resource().styleContentBold().clone().fontSize(11f)
        when (dto.result) {
            CancerchDto.Results.CONCERN -> stream.paragraph(305f, y, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, "관심관리군 맞춤 가이드라인은 다음과 같습니다"))
            else -> {
                val cancer = when {
                    dto.first.name == "기타암종" -> "기타 암"
                    else -> dto.first.name
                }
                stream.paragraph(305f, y, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, "$cancer 집중관리군 맞춤 가이드라인은 다음과 같습니다."))
            }
        }
    }

    override fun lblGuideLineDetection(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float) {
        stream.paragraph(238f, y+rate-74, 200f, AlignHorizontal.LEFT,
            TextBlock(resource().styleContentRegualar().clone().fontSize(9.5f),  "· 주치의와 상담요함")
        )
    }

    override fun lblGuideLineTime(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto) {
        val RESULT_CONTENT_OTH_RATE = 182f
        val time = when (dto.first.name) {
            "폐암" -> "3개월 후"
            "대장암" -> "3개월 후"
            "간암" -> "3개월 후"
            "췌장담도암" -> "3개월 후"
            "식도암" -> "3개월 후"
            "유방암" -> "3개월 후"
            "난소암" -> "3개월 후"
            else -> "3개월 후"
        }
        when (dto.result) {
            CancerchDto.Results.RISK -> stream.paragraph(468f, y+rate-74, 160f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().fontSize(10f), time))
            else -> stream.paragraph(380f, y+ RESULT_CONTENT_OTH_RATE -75, 160f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().fontSize(10f), time))
        }

    }
    override fun lblGuideLineNormalHeader(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleBold = resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(9f)
        stream.paragraph(59f,  y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold,"암종"))
        stream.paragraph(178f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "대상"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "주기"))
        stream.paragraph(440f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "검사"))
    }

    override fun lblGuideLineNormalCancer(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(8f)
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "폐암"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "대장암"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "간암"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "췌장담도암"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "식도암"))
        stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "난소암"))
    }

    override fun lblGuideLineNormalTarget(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(8f)

        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -34, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 54세 이상 만 74세 이하 남녀\n 폐암 발생 고위험군(30갑년*이상 흡연력)"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -73, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 50세 이상 남녀"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -102, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 40세 이상 남녀 중 간암 발생 고위험군\n"), TextBlock(styleRegular.clone().fontSize(7f), "(간경변증이나 B형 간염 바이러스 항원 또는\nC형 간염 바이러스 항체 양성으로 확인된 자)"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -147, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7f), "만 70세 이상 남녀\n췌장담도암 가족력/장기 흡연자/만성췌장염 병력"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -187, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "증상이 있거나, 식도암이 의심되는 자"))
        stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -215, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "-"))
    }

    override fun lblGuideLineNormalFrequency(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(8f)

        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "2년"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "1년"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "6개월"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "1년"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -182, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "정기적인\n검사 권장"))
        stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "-"))
    }

    override fun lblGuideLineNormalTest(stream: PDPageContentStreamPageAccessible, y: Float) {
        val RESULT_CONTENT_LOW_RATE = 225f
        val styleRegular = resource().styleContentRegualar().clone().fontSize(8f)

        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "저선량흉부CT검사"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -69, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "분변잠혈검사 이상소견 시, 대장내시경검사\n"), TextBlock(styleRegular.clone().fontSize(6.5f), "(단, 대장내시경을 실시하기 어려운 경우 대장이중조영검사 선택적 시행)"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -113, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular,"간초음파검사, 혈청알파태아단백검사"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "복부초음파검사, 복부CT검사"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "식도-위 내시경검사"))
        stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -215, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7.5f), "CA125 암표지자 검사 이상소견 시 초음파검사, CT검사, MRI검사"))
    }
    override fun lblGuideLineCaption(stream: PDPageContentStreamPageAccessible, y:Float) {
        val RESULT_CONTENT_LOW_RATE = 225f

        stream.paragraph(400f, y+ RESULT_CONTENT_LOW_RATE -232, 230f, AlignHorizontal.LEFT, TextBlock(resource().styleContentBold().clone().color(Color(55,55,55)).fontSize(5f), "*갑년 : 일평균 흡연량(갑) x 흡연기간(년) ex) 2갑 x 15년 = 30갑년(검진 대상)"))
    }

    override fun lblGuideLineComment(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto) {
        val content =  when(dto.first.name) {
            "폐암" -> "아이캔서치 검사 폐암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 폐암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

            "대장암" -> "아이캔서치 검사 대장암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 대장암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

            "간암" -> "아이캔서치 검사 간암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 간암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

            "췌장담도암" -> "아이캔서치 검사 췌장담도암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 췌장담도암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

            "식도암" -> "아이캔서치 검사 식도암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 식도암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

            "유방암" -> "아이캔서치 검사 유방암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 유방암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

            "난소암" -> "아이캔서치 검사 난소암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 난소암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

            else -> "아이캔서치 검사 기타 암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
        }
        stream.paragraph(144f, y+34, 400f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().fontSize(9f), content))
    }
    override fun lblGuideLineConcern(stream: PDPageContentStreamPageAccessible, y: Float) {
        stream.paragraph(134f, y+50, 500f, AlignHorizontal.LEFT,
            TextBlock(resource().styleContentRegualar().clone().fontSize(9f), "아이캔서치 검사 관심관리군은 3개월 후 본 검사를 통해 암 DNA를 추적할 것을 권장합니다.\n" +
                    "관심관리군은 암환자와 다소 유사한 DNA 이상 패턴이 관찰되었으나,\n" +
                    "건강상태(양성질환, 자가면역질환 등)에 따른 일시적인 이상 패턴 검출의 가능성을 배재할 수 없는 경우입니다.\n" +
                    "3개월 주기로 본 검사를 통해 암 DNA에 의한 이상 패턴을 추적할 것을 권장합니다.\n" +
                    "증상 등이 동반되어 특정 암종이 의심될 경우 의료진 상담을 통한 해당 암종에 대한 정밀 검사를 권장합니다.")
        )
    }

    override fun lblLimitationHeader(stream: PDPageContentStreamPageAccessible, y: Float){
        stream.paragraph(297f, y+10, 200f, AlignHorizontal.CENTER, TextBlock(resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "검 사 한 계"))

    }
    override fun lblLimitationTableHeader(stream: PDPageContentStreamPageAccessible, y: Float, col: Int){
        when (col) {
            0 -> stream.paragraph(88f, y+117, 100f, AlignHorizontal.CENTER,  TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "암종"))
            1 -> stream.paragraph(188f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "특이도¹⁾"))
            2 -> stream.paragraph(293f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "민감도²⁾"))
            3 -> stream.paragraph(392f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "양성예측도³⁾"))
            else -> stream.paragraph(498f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(resource().styleContentRegualar().clone().color(Color(255,255,255)).fontSize(8f), "음성예측도⁴⁾"))
        }
    }

    override fun lblLimitationTableTotalCancer(col: Int) = when (col) {
        0 -> "전체"
        1 -> "96.5%"
        2 -> "81.1%"
        3 -> "74.4%"
        else -> "97.6%"
    }

    override fun lblLimitationTableLungCancer(col: Int) = when (col) {
        0 -> "폐암"
        1 -> "96.5%"
        2 -> "73.1%"
        3 -> "12.7%"
        else -> ">98%"
    }

    override fun lblLimitationTableColorCancer(col: Int) = when (col) {
        0 -> "대장암"
        1 -> "96.5%"
        2 -> "70.1%"
        3 -> "17.8%"
        else -> ">98%"
    }

    override fun lblLimitationTableLiverCancer(col: Int) = when (col) {
        0 -> "간암"
        1 -> "96.5%"
        2 -> "94.5%"
        3 -> "9.8%"
        else -> ">98%"
    }

    override fun lblLimitationTablePanCancer(col: Int) = when (col) {
        0 -> "췌장담도암"
        1 -> "96.5%"
        2 -> "91.3%"
        3 -> "6.8%"
        else -> ">98%"
    }

    override fun lblLimitationTableEsopCancer(col: Int) = when (col) {
        0 -> "식도암"
        1 -> "96.5%"
        2 -> "88.4%"
        3 -> "2.0%"
        else -> ">98%"
    }

    override fun lblLimitationTableOverCancer(col: Int) = when (col) {
        0 -> "난소암"
        1 -> "96.5%"
        2 -> "70.4%"
        3 -> "1.7%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Header(col: Int) = when (col) {
        0 -> "암종"
        1 -> "검사"
        2 -> "특이도"
        3 -> "민감도"
        4 -> "양성예측도"
        else -> "음성예측도"
    }

    override fun lblLimitationTable2Row1(col: Int) = when (col) {
        0 -> "폐암"
        1 -> "저선량흉부CT검사"
        2 -> "약 92.6%"
        3 -> "약 88.9%"
        4 -> "약 6.3%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row2(col: Int) = when (col) {
        0 -> "대장암"
        1 -> "분변잠혈검사"
        2 -> "95.4%"
        3 -> "약 40.0%"
        4 -> "약 7.5%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row3(col: Int) = when (col) {
        0 -> "대장내시경 검사"
        1 -> "약 99.0%"
        2 -> "약 85.0~95.0%"
        3 -> "약 44.1~46.9%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row4(col: Int) = when (col) {
        0 -> "간암"
        1 -> "간초음파 및 혈청알파태아단백검사"
        2 -> "약 94.0%"
        3 -> "약 80.0%"
        4 -> "약 4.5%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row5(col: Int) = when (col) {
        0 -> "췌장담도암"
        1 -> "CA19-9 암표지자 검사"
        2 -> "약 79.9~85.3%"
        3 -> "약 76.1~80.2%"
        4 -> "약 0.3~0.4%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row6(col: Int) = when (col) {
        0 -> "식도암"
        1 -> "식도-위내시경 검사"
        2 -> "약 79.0%"
        3 -> "약 62.0%"
        4 -> "약 0.2%"
        else -> ">98%"
    }

    override fun lblLimitationTable2Row7(col: Int) = when (col) {
        0 -> "난소암"
        1 -> "CA125 암표지자 검사"
        2 -> "약 95.0%"
        3 -> "약 43.3%"
        4 -> "약 0.6%"
        else -> ">98%"
    }

    override fun lblLimitation(stream: PDPageContentStreamPageAccessible, y: Float, row: Int) {
        when (row) {
            0 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 암세포 유래 cfDNA 특성 분석을 통해 암의 존재 가능성을 예측하는 검사로, 확진 목적으로 사용할 수 없습니다."))
            1 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 모든 암을 검출할 수 없으며, 암의 병기나 종류에 따라 검출 성적이 달라질 수 있습니다."))
            2 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사의 데이터는 주요 암종인 폐암, 대장암, 간암, 췌장담도암, 식도암, 난소암을 포함하고 있으며 기타 암종은 정확한 분석이 어렵습니다."))
            3 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "암종의 위치 및 유전적 특성에 따라 검출민감도가 상이할 수 있습니다."))
            4 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 내부적으로 축적된 데이터에 따라 검사 대상 암종 확대 및 성능이 변경될 수 있습니다."))
            5 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 양성질환, 자가면역질환 등에서 위양성으로 보고될 수 있으며, 항암치료, 세포치료 등에 따라서 위음성으로 보고될 수 있습니다."))
            else -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), ""))
        }
    }

    override fun lblLimitationDescription(stream: PDPageContentStreamPageAccessible, y: Float, row: Int) {
        when (row) {
            0 -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), "1) 특이도 : 정상인을 검사했을 때 아이캔서치 검사가 일반관리군으로 판단한 비율을 의미합니다."))
            1 -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), "2) 민감도 : 암환자를 검사했을 때 아이캔서치 검사가 관심관리군 · 집중관리군으로 판단한 비율을 의미합니다."))
            2 -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), "3) 양성예측도 : 아이캔서치 검사에서 관심관리 · 집중관리로 판단한 수검자가 실제 암환자일 비율을 의미합니다. 50대 이상의 유병률에 기초하여 양성예측도가 계산되었습니다."))
            3 -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), "4) 음성예측도 : 아이캔서치 검사에서 일반관리로 판단한 수검자가 실제 정상인일 비율을 의미합니다. 50대 이상의 유병률에 기초하여 음성예측도가 계산되었습니다."))
            else -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), ""))
        }
    }

    override fun lblReferenceTitle() = "참 고 문 헌"

    override fun lblReferenceLeft(): String {
        return "1.\tCancer Biol Ther. 2019; 20(8): 1057–1067.\n" +
                "2.\tMutat Res. Jul-Sep 2019;781:100-129.\n" +
                "3.\tBMC Cancer. 2017; 17: 697.\n" +
                "4.\tCancer Resaerch. 2022:82(12, Supplement):6371-6371\n" +
                "5.\tBr J Cancer. 2008;98(10):1602-7.\n" +
                "6.\t대한소화기내시경학회지, 2007;35(2): 68-73."
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
        stream.paragraph(297f, y, 600f, AlignHorizontal.CENTER,
            TextBlock(resource().styleContentRegualar().clone().color(Color(151,151,151)).fontSize(6.5f), "※ 본 검사는 검사 결과가 갖는 임상적 의미가 확립되지 않았으며, 이에 따르는 건강에 관련된 행위가 유용하다는 객관적 타당성이 아직 부족합니다.\n" +
                    "※ 이 검사는 "),
            TextBlock(resource().styleContentRegualar().clone().color(Color(81,81,81)).fontSize(6.5f), "GC지놈에서 자체 개발한 검사(Laboratory-developed Test, LDT)로 적절한 평가를 통해 성능을 확인"),
            TextBlock(resource().styleContentRegualar().clone().color(Color(151,151,151)).fontSize(6.5f), "하였습니다.")
        )
    }

    override fun lblPerformance(): String {
        return "아이캔서치 검사의 암종별 성능"
    }

    override fun lblPerformanceBasic(): String = "기존 선별검사 성능"

    override fun lblResultToWord(result: CancerchDto.Results) = when (result) {
        CancerchDto.Results.GENERAL -> "일반관리"
        CancerchDto.Results.CONCERN -> "관심관리"
        else -> "집중관리"
    }

    override fun lblPatientInfo(age: String, sex: Sex): String {
        val ageStream: String = (age.toInt() / 10 * 10).toString()
        val cut: String = when {
            age.substring(age.length - 1, age.length).toInt() >= 5 -> "후반"
            else -> "초반"
        }
        val sexStr: String = when {
            sex == Sex.F -> "여성"
            else -> "남성"
        }
        return ageStream + "대 " + cut + " " + sexStr + " 평균"
    }

    override fun lblPatientInfoWithCancer(age: String, sex: Sex, cancer: String): String {
        val ageStream: String = (age.toInt() / 10 * 10).toString()
        val cut: String = when {
            age.substring(age.length - 1, age.length).toInt() >= 5 -> "후반"
            else -> "초반"
        }
        val sexStr: String = when {
            sex == Sex.F -> "여성"
            else -> "남성"
        }
        val canStr: String = when (cancer) {
            "기타암종" -> "전체 암"
            else -> cancer
        }
        return ageStream + "대 " + cut + " " + sexStr + " " + canStr
    }

    override fun lblPatientSir(name: String) = "${name}님"

    override fun lblCancerForOthers() = "암"
    override fun lblDetailResultAnalysisContentOther(patient: String?): String =
        "\n\n" + patient + "님에 대한 기타 암 집중관리군 추가 결과 해석입니다."

    fun date(date: LocalDate?): String? {
        return if (date == null) null else DTF.format(date)
    }

    fun date(date: LocalDateTime?): String? {
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
            Sex.M -> "남"
            Sex.F -> "여"
        }
    }
    override fun lblHighRisk() = "약 10배 이상"
    override fun lblMiddleRisk() = "평균 초과"
    override fun lblNormalRisk() = "평균 이하"
}