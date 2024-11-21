package com.greencross.lims.report.cancerch.GangbukComponent

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.cancerch.repository.CancerchRepo
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.AlignVertical
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color
import kotlin.math.round

class GangbukSectionCancerTypeDangerKoKr(private val y: Float = 725f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var img = template!!.resource().imgCancerTypeTitle(dto!!.result == CancerchDto.Results.GENERAL)
        var width = img.width * rate(dto.result == CancerchDto.Results.GENERAL) / img.height
        stream.drawImage(img, 298f - width / 2, y - rate(dto.result == CancerchDto.Results.GENERAL) + 63, width, rate(dto.result == CancerchDto.Results.GENERAL))

        var style = template.resource().styleContentSpecial().fontSize(15f).color(Color(255, 255, 255))
        stream.paragraph(560f - width / 2, y + 45, 120f, AlignHorizontal.CENTER, TextBlock(style, "암종별 위험도"))

        stream.rect(139f, y + 24, 315f, 16f).setStrokingColor(Color(108, 109, 112)).setNonStrokingColor(Color.WHITE).setLineWidth(0.2f)
            .setLineDashPattern(floatArrayOf(2.5f, 1.5f), 2f).fillAndStroke()

        style = template.resource().styleContentRegualar().clone().fontSize(7.3f).color(Color(72, 71, 71))
        var bold = template.resource().styleContentBold().clone().fontSize(7.3f)
        stream.paragraph(295f, y + 13, 300f, AlignHorizontal.CENTER, TextBlock(style, "암종별 위험도는 수검자가 "), TextBlock(bold, "실제로 암일 확률"), TextBlock(style, "을 동일집단(동일한 연령대, 성별)과 비교합니다."))

        img = template.resource().imgHuman()
        width = img.width * DANGER_HUMAN_RATE / img.height
        stream.drawImage(
            img, 298f - width / 2, y - DANGER_HUMAN_RATE - 15, width,
            DANGER_HUMAN_RATE
        )

        drawCancerContent(stream, template, 129f, y - DANGER_CANCER_CONTENT - 35, dto!!, CancerchRepo.암종.폐암)
        drawCancerContent(stream, template, 129f, y - DANGER_CANCER_CONTENT - 95, dto, CancerchRepo.암종.대장암)
        drawCancerContent(stream, template, 129f, y - DANGER_CANCER_CONTENT - 155, dto, CancerchRepo.암종.간암)
        if (dto.sex == Sex.F) {
            drawCancerContent(stream, template, 464f, y - DANGER_CANCER_CONTENT, dto, CancerchRepo.암종.췌장담도암)
            drawCancerContent(stream, template, 464f, y - DANGER_CANCER_CONTENT - 60, dto, CancerchRepo.암종.식도암)
            drawCancerIcon(stream, template, 337f, y - DANGER_CANCER_CONTENT - 150, dto, CancerchRepo.암종.난소암)
            drawCancerContent(stream, template, 464f, y - DANGER_CANCER_CONTENT - 120, dto, CancerchRepo.암종.난소암)
            drawCancerContent(stream, template, 464f, y - DANGER_CANCER_CONTENT - 180, dto, CancerchRepo.암종.기타암종)
        } else {
            drawCancerContent(stream, template, 464f, y - DANGER_CANCER_CONTENT - 35, dto, CancerchRepo.암종.췌장담도암)
            drawCancerContent(stream, template, 464f, y - DANGER_CANCER_CONTENT - 95, dto, CancerchRepo.암종.식도암)
            drawCancerContent(stream, template, 464f, y - DANGER_CANCER_CONTENT - 155, dto, CancerchRepo.암종.기타암종)
        }
        drawCancerIcon(stream, template, 282f, y - DANGER_CANCER_CONTENT - 59, dto, CancerchRepo.암종.폐암)
        drawCancerIcon(stream, template, 289f, y - DANGER_CANCER_CONTENT - 155, dto, CancerchRepo.암종.대장암)
        drawCancerIcon(stream, template, 263f, y - DANGER_CANCER_CONTENT - 108, dto, CancerchRepo.암종.간암)
        drawCancerIcon(stream, template, 325f, y - DANGER_CANCER_CONTENT - 100, dto, CancerchRepo.암종.췌장담도암)
        drawCancerIcon(stream, template, 317f, y - DANGER_CANCER_CONTENT - 19, dto, CancerchRepo.암종.식도암)

        stream.setNonStrokingColor(Color.GRAY).setStrokingColor(Color.GRAY)
            .setLineWidth(0.3f) .setLineDashPattern(floatArrayOf(1f, 1.5f), 1f)
        stream.saveGraphicsState()
        drawCancerDashLine(stream, 195f, y - DANGER_CANCER_CONTENT / 2 - 35, 260f, y - DANGER_CANCER_CONTENT / 2 - 67)
        drawCancerDashLine(stream, 195f, y - DANGER_CANCER_CONTENT / 2 - 95, 269f, y - DANGER_CANCER_CONTENT / 2 - 164)
        drawCancerDashLine(stream, 195f, y - DANGER_CANCER_CONTENT / 2 - 155, 243f, y - DANGER_CANCER_CONTENT / 2 - 122)
        if(dto.sex == Sex.F) {
            drawCancerDashLine(stream, 344f, y - DANGER_CANCER_CONTENT / 2 - 100, 396f, y - DANGER_CANCER_CONTENT / 2)
            drawCancerDashLine(stream, 337f, y - DANGER_CANCER_CONTENT / 2 - 30,  396f, y - DANGER_CANCER_CONTENT / 2 - 60)
            drawCancerDashLine(stream, 353f, y - DANGER_CANCER_CONTENT / 2 - 145, 396f, y - DANGER_CANCER_CONTENT / 2 - 120)
        } else {
            drawCancerDashLine(stream, 344f, y - DANGER_CANCER_CONTENT / 2 - 100, 396f, y - DANGER_CANCER_CONTENT / 2 - 35)
            drawCancerDashLine(stream, 337f, y - DANGER_CANCER_CONTENT / 2 - 30,  396f, y - DANGER_CANCER_CONTENT / 2 - 95)
        }

        style = template.resource().styleContentRegualar().clone().fontSize(6.5f).color(Color(159, 160, 160))
        if(dto.result == CancerchDto.Results.GENERAL) stream.paragraph(630f - width / 2, y - DANGER_CONTENT_RATE +70, 400f, AlignHorizontal.RIGHT, TextBlock(style, "* 각 암종별 평균 위험도는 수검자와 동일한 연령대, 성별에서의 유병률에 해당합니다. (국가암등록사업 연례 보고서-2020 암등록통계)"))
        else stream.paragraph(115f - width / 2, y - DANGER_CONTENT_RATE +30, 400f, AlignHorizontal.LEFT, TextBlock(style, "* 각 암종별 평균 위험도는 수검자와 동일한 연령대, 성별에서의 유병률에 해당합니다. (국가암등록사업 연례 보고서-2020 암등록통계)"))

        stream.restoreGraphicsState()
        return stream
    }

    private fun drawCancerDashLine(
        stream: PDPageContentStreamPageAccessible,
        sx: Float,
        sy: Float,
        ex: Float,
        ey: Float
    ) {
        stream.circle(sx, sy, 1f).fill()
        stream.line(sx, sy, ex, ey).stroke()
        stream.circle(ex, ey, 1f).fill()
    }

    private fun drawCancerContent(
        stream: PDPageContentStreamPageAccessible,
        template: CancerchTemplate<CancerchResource>,
        x: Float, y: Float, dto: CancerchDto, cancer: CancerchRepo.암종
    ) {
        val repo = CancerchRepo()
        val age: Int = dto.age!!.toInt()
        var img = template.resource().imgCancerTypeContent()
        var width = img.width * DANGER_CANCER_CONTENT / img.height
        val ppv = if (round(
                repo.findASRbyAgeAndCancerAndSex(cancer, age, dto.sex!!)!!.div(1000) * 100
            ) / 100 == 0.0
        ) 0.01 else round(repo.findASRbyAgeAndCancerAndSex(cancer, age, dto.sex!!)!!.div(1000) * 100) / 100
        stream.drawImage(img, x - width / 2, y, width, DANGER_CANCER_CONTENT)

        img = template.resource().imgCancerTypeImage(cancer.name)
        width = img.width * DANGER_CANCER_ICON / img.height
        stream.drawImage(img, x - width / 2 - 35, y + 22, width, DANGER_CANCER_ICON)

        var style2 = template.resource().styleContentRegualar().clone().fontSize(8f).color(Color(35, 24, 15))
        val cancerName = when (cancer.name) {
            "기타암종"    -> "기타 암종"
            else -> cancer.name
        }
        stream.paragraph(x - 36, y + 9, 100f, AlignHorizontal.CENTER, TextBlock(style2, cancerName))
        style2 = template.resource().styleContentRegualar().clone().fontSize(6.5f).color(Color(114, 113, 113))
        stream.paragraph(x + 21, y + 7, 100f, AlignHorizontal.CENTER, TextBlock(style2, "평균 위험도    수검자"))

        var style = template.resource().styleContentRegualar().clone().fontSize(7f).color(Color(114, 113, 113))

        img = template.resource().imgBarGray()
        width = img.width * DANGER_BAR_RATE / img.height
        stream.paragraph(x - width / 2 + 17, y + 18 + DANGER_BAR_RATE, 100f, AlignHorizontal.CENTER, TextBlock(style, "${ppv}" + "%"))
        stream.drawImage(img, x - width / 2 + 9, y + 14, width, DANGER_BAR_RATE)

        when (dto.result) {
            CancerchDto.Results.RISK -> {
                val checker = dto.first.name == cancer.name
                img = if (checker) template.resource().imgBarDanger() else template.resource().imgBarMiddle()
                val height = if (checker) DANGER_BAR_RATE * 6.5f else DANGER_BAR_RATE * 4f
                val value = if (checker) "약 10배 이상" else "평균 초과"
                style =
                    if (checker) template.resource().styleContentBold().clone().fontSize(6f).color(Color(217, 52, 29))
                    else template.resource().styleContentBold().clone().fontSize(6f).color(Color(217, 166, 71))
                stream.paragraph(x - width / 2 + 49, y + 18 + height, 100f, AlignHorizontal.CENTER, AlignVertical.BOTTOM, TextBlock(style, value))
                stream.drawImage(img, x - width / 2 + 41, y + 14, width, height)

                if (checker) {
                    template.resource().imgCancerTypeDetect(stream, x, y, DANGER_CANCER_ICON)

                    img = template.resource().imgCancerTypeDetectArrow()
                    width = img.width * 24.75f / img.height
                    stream.drawImage(img, x - width / 2 + 25, y + 14 + DANGER_BAR_RATE, width, 24.75f)
                    stream.line(x-width/2+19, y+14+ DANGER_BAR_RATE, x-width/2+34, y+14+ DANGER_BAR_RATE)
                        .setStrokingColor(Color(217, 52, 29))
                        .setLineDashPattern(floatArrayOf(1f, 1.5f), 1f).stroke()
                }
                stream.restoreGraphicsState()
                stream.saveGraphicsState()
                stream.line(x-7, y + 14, x + 60, y + 14).setStrokingColor(Color.BLACK).setLineWidth(0.1f).stroke()
            }

            CancerchDto.Results.CONCERN -> {
                img = template.resource().imgBarMiddle()
                style = template.resource().styleContentBold().clone().fontSize(6f).color(Color(217, 166, 71))
                stream.paragraph(x - width / 2 + 49, y + 18 + DANGER_BAR_RATE + 10, 100f, AlignHorizontal.CENTER, AlignVertical.BOTTOM, TextBlock(style, "평균 초과"))

                stream.restoreGraphicsState()
                stream.saveGraphicsState()
                stream.drawImage(img, x - width / 2 + 41, y + 14, width, DANGER_BAR_RATE + 10)
                stream.line(x-7, y + 14, x + 60, y + 14).setStrokingColor(Color.BLACK).setLineWidth(0.1f).stroke()
            }

            else -> {
                img = template.resource().imgBarNormal()
                style = template.resource().styleContentBold().clone().fontSize(6f).color(Color(81, 78, 145))
                stream.paragraph(x - width / 2 + 49, y + 18 + DANGER_BAR_RATE, 100f, AlignHorizontal.CENTER, AlignVertical.BOTTOM, TextBlock(style, "평균 이하"))

                stream.restoreGraphicsState()
                stream.saveGraphicsState()
                stream.drawImage(img, x - width / 2 + 41, y + 14, width, DANGER_BAR_RATE)
                stream.line(x-7, y + 14, x + 60, y + 14).setStrokingColor(Color.BLACK).setLineWidth(0.1f).stroke()
            }
        }
    }

    private fun drawCancerIcon(
        stream: PDPageContentStreamPageAccessible,
        template: CancerchTemplate<CancerchResource>,
        x: Float, y: Float, dto: CancerchDto, cancer: CancerchRepo.암종
    ) {
        val img = when (cancer) {
            CancerchRepo.암종.식도암 -> template.resource().imgEsop(dto.first.name)
            CancerchRepo.암종.폐암 -> template.resource().imgLung(dto.first.name)
            CancerchRepo.암종.간암 -> template.resource().imgLiver(dto.first.name)
            CancerchRepo.암종.췌장담도암 -> template.resource().imgPanc(dto.first.name)
            CancerchRepo.암종.대장암 -> template.resource().imgColon(dto.first.name)
            else -> template.resource().imgOvary(dto.first.name)
        }
        val width = img.width * DANGER_ICON_RATE / img.height
        stream.drawImage(img, x - width / 2, y, width, DANGER_ICON_RATE)
    }
    private fun rate(tf: Boolean) = if(tf) DANGER_CONTENT_RATE else DANGER_CONTENT_RATE_WITH_OTHER

    companion object {
        private const val DANGER_CONTENT_RATE = 445f
        private const val DANGER_CONTENT_RATE_WITH_OTHER = 469f
        private const val DANGER_HUMAN_RATE = 230f
        private const val DANGER_CANCER_CONTENT = 60f
        private const val DANGER_CANCER_ICON = 32f
        private const val DANGER_ICON_RATE = 45f
        private const val DANGER_BAR_RATE = 4.5f
    }
}
