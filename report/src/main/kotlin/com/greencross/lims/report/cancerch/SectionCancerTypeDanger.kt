package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.cancerch.repository.CancerchRepo
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color
import kotlin.math.round

class SectionCancerTypeDanger(private val y: Float = 745f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var img = template!!.resource().imgCancerTypeTitle(dto!!.result == CancerchDto.Results.RISK && dto.first.name == "기타암종")
        var width = img.width * rate(dto.result == CancerchDto.Results.RISK && dto.first.name == "기타암종") / img.height
        var style = template.resource().styleContentSpecial().fontSize(15f).color(Color(255, 255, 255))
        stream.drawImage(img, 298f - width / 2, y - rate(dto.result == CancerchDto.Results.RISK && dto.first.name == "기타암종") + 63, width, rate(dto.result == CancerchDto.Results.RISK && dto.first.name == "기타암종"))

        stream.paragraph(
            560f - width / 2,
            y + 45,
            120f,
            AlignHorizontal.CENTER,
            TextBlock(style, template.lblDangerTitle())
        )
        stream.rect(139f, y + 24, 315f, 16f).setStrokingColor(Color(108, 109, 112)).setNonStrokingColor(Color.WHITE).setLineWidth(0.2f)
            .setLineDashPattern(floatArrayOf(2.5f, 1.5f), 2f).fillAndStroke()

        style = template.resource().styleContentRegualar().clone().fontSize(7.3f).color(Color(72, 71, 71))
        val bold = template.resource().styleContentBold().clone().fontSize(7.3f)
        stream.paragraph(
            295f, y + 13, 300f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDangerIntroStart()),
            TextBlock(bold, template.lblDangerIntroBridge()),
            TextBlock(style, template.lblDangerIntroEnd())
        )

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
        stream.paragraph(
            110f - width / 2,
            y - DANGER_CONTENT_RATE +if(dto.result == CancerchDto.Results.RISK && dto.first.name == "기타암종") -100 else +54,
            400f,
            AlignHorizontal.LEFT,
            TextBlock(style, template.lblDangerTMI())
        )

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
        stream.drawImage(img, x - width / 2 - 35, y + 17, width, DANGER_CANCER_ICON)

        var style = template.resource().styleContentRegualar().clone().fontSize(8f).color(Color(35, 24, 15))
        stream.paragraph(x - 36, y + 9, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerCancerName(convNameToNum(cancer.name))))

        style = template.resource().styleContentRegualar().clone().fontSize(6.5f).color(Color(114, 113, 113))
        stream.paragraph(x + 21, y + 7, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))

        img = template.resource().imgBarGray()
        width = img.width * DANGER_BAR_RATE / img.height
        stream.paragraph(x - width / 2 + 17, y + 18 + DANGER_BAR_RATE, 100f, AlignHorizontal.CENTER, TextBlock(style, "${ppv}" + "%"))
        stream.drawImage(img, x - width / 2 + 9, y + 14, width, DANGER_BAR_RATE)

        when (dto.result) {
            CancerchDto.Results.RISK -> {
                val checker = dto.first.name == cancer.name
                img = if (checker) template.resource().imgBarDanger() else template.resource().imgBarMiddle()
                val height = if (checker) DANGER_BAR_RATE * 6.5f else DANGER_BAR_RATE * 4f

                val value = if (checker) "10배 이상" else "평균 초과"
                style =
                    if (checker) template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29))
                    else template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 166, 71))
                stream.paragraph(
                    x - width / 2 + 49,
                    y + 18 + height,
                    100f,
                    AlignHorizontal.CENTER,
                    TextBlock(style, value)
                )
                stream.drawImage(img, x - width / 2 + 41, y + 14, width, height)

                if (checker) {
                    img = template.resource().imgCancerTypeDetect()
                    width = img.width * DANGER_GUIDE_RATE / img.height
                    stream.drawImage(img, x - width / 2 - 26, y + DANGER_CANCER_ICON, width, DANGER_GUIDE_RATE)

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
                style = template.resource().styleContentBold().clone().fontSize(7f).color(Color(217, 166, 71))
                stream.paragraph(
                    x - width / 2 + 49,
                    y + 18 + DANGER_BAR_RATE + 10,
                    100f,
                    AlignHorizontal.CENTER,
                    TextBlock(style, "평균 초과")
                )

                stream.restoreGraphicsState()
                stream.saveGraphicsState()
                stream.drawImage(img, x - width / 2 + 41, y + 14, width, DANGER_BAR_RATE + 10)
                stream.line(x-7, y + 14, x + 60, y + 14).setStrokingColor(Color.BLACK).setLineWidth(0.1f).stroke()
            }

            else -> {
                img = template.resource().imgBarNormal()
                style = template.resource().styleContentBold().clone().fontSize(7f).color(Color(81, 78, 145))
                stream.paragraph(
                    x - width / 2 + 49,
                    y + 18 + DANGER_BAR_RATE,
                    100f,
                    AlignHorizontal.CENTER,
                    TextBlock(style, "평균 이하")
                )

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

    private fun convNameToNum(name: String) = when (name) {
        "폐암" -> 1
        "대장암" -> 2
        "간암" -> 3
        "췌장담도암" -> 4
        "식도암" -> 5
        "난소암" -> 6
        "기타암종" -> 7
        "유방암" -> 8
        else -> 0
    }
    private fun rate(tf: Boolean) = if(tf) DANGER_CONTENT_RATE_WITH_OTHER else DANGER_CONTENT_RATE

    companion object {
        private const val DANGER_CONTENT_RATE = 318f
        private const val DANGER_CONTENT_RATE_WITH_OTHER = 469f
        private const val DANGER_HUMAN_RATE = 230f
        private const val DANGER_CANCER_CONTENT = 60F
        private const val DANGER_CANCER_ICON = 35F
        private const val DANGER_GUIDE_RATE = 20f
        private const val DANGER_ICON_RATE = 45f
        private const val DANGER_BAR_RATE = 4.5f
    }
}