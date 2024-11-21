package com.greencross.lims.report.avoid

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.avoid.repository.CancerRepo
import com.greencross.lims.report.builder.Sex
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color
import kotlin.math.round

class SectionCancerTypeDanger(private val y: Float = 410f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ Cancer's Danger content & images
        var img = template!!.resource().imgCancerTypeTitle()
        var width = img.width * DANGER_CONTENT_RATE / img.height
        var style = template.resource().styleContentSpecial().fontSize(12f).color(Color(255,255,255))
        stream.drawImage(img, 306f - width/2, y-DANGER_CONTENT_RATE, width, DANGER_CONTENT_RATE)
        stream.paragraph(555f-width/2, y-13, 80f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDangerTitle()))

        img = template.resource().imgCancerReadingGuide()
        width = img.width * DANGER_GUIDE_RATE / img.height
        style = template.resource().styleContentRegualar().clone().fontSize(7.3f).color(Color(72,71,71))
        val bold = template.resource().styleContentBold().clone().fontSize(7.3f)
        stream.drawImage(img, 307f-width/2, y-DANGER_TITLE_RATE-25f, width, DANGER_GUIDE_RATE)
        stream.paragraph(456.5f-width/2, y-DANGER_TITLE_RATE-20f, 300f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDangerIntroStart()),
            TextBlock(bold, template.lblDangerIntroBridge()),
            TextBlock(style, template.lblDangerIntroEnd())
        )

        img = template.resource().imgHuman()
        width = img.width * DANGER_HUMAN_RATE / img.height
        stream.drawImage(img, 310f - width/2, y - DANGER_HUMAN_RATE-70, width, DANGER_HUMAN_RATE)
        drawCancerContent(stream, template, 156f, y - DANGER_CANCER_CONTENT- 70, dto!!, CancerRepo.암종.폐암)
        drawCancerContent(stream, template, 156f, y - DANGER_CANCER_CONTENT-135, dto,   CancerRepo.암종.대장암)
        drawCancerContent(stream, template, 156f, y - DANGER_CANCER_CONTENT-200, dto,   CancerRepo.암종.간암)
        drawCancerIcon(   stream, template, 294f, y - DANGER_CANCER_CONTENT-112, dto,   CancerRepo.암종.폐암)
        drawCancerIcon(   stream, template, 297f, y - DANGER_CANCER_CONTENT-200, dto,   CancerRepo.암종.대장암)
        drawCancerIcon(   stream, template, 278f, y - DANGER_CANCER_CONTENT-150, dto,   CancerRepo.암종.간암)
        drawCancerIcon(   stream, template, 342f, y - DANGER_CANCER_CONTENT-150, dto,   CancerRepo.암종.췌장담도암)
        drawCancerIcon(   stream, template, 326f, y - DANGER_CANCER_CONTENT-80,  dto,   CancerRepo.암종.식도암)

        drawCancerDashLine(stream, 222f, y - DANGER_CANCER_CONTENT/2- 70, 279f, y - DANGER_CANCER_CONTENT/2-109)
        drawCancerDashLine(stream, 222f, y - DANGER_CANCER_CONTENT/2-135, 278f, y - DANGER_CANCER_CONTENT/2-208)
        drawCancerDashLine(stream, 222f, y - DANGER_CANCER_CONTENT/2-200, 264f, y - DANGER_CANCER_CONTENT/2-172)

        if(dto.sex == Sex.F) {
            drawCancerContent(stream, template, 460f, y - DANGER_CANCER_CONTENT -  49, dto, CancerRepo.암종.췌장담도암)
            drawCancerContent(stream, template, 460f, y - DANGER_CANCER_CONTENT - 109, dto, CancerRepo.암종.식도암)
            drawCancerIcon(   stream, template, 343f, y - DANGER_CANCER_CONTENT - 195, dto, CancerRepo.암종.난소암)
            drawCancerContent(stream, template, 460f, y - DANGER_CANCER_CONTENT - 169, dto, CancerRepo.암종.난소암)
            drawCancerContent(stream, template, 460f, y - DANGER_CANCER_CONTENT - 229, dto, CancerRepo.암종.기타암종)

            drawCancerDashLine(stream, 352f, y - DANGER_CANCER_CONTENT/2-145, 394f, y - DANGER_CANCER_CONTENT/2- 49)
            drawCancerDashLine(stream, 345f, y - DANGER_CANCER_CONTENT/2- 90, 394f, y - DANGER_CANCER_CONTENT/2-109)
            drawCancerDashLine(stream, 358f, y - DANGER_CANCER_CONTENT/2-195, 394f, y - DANGER_CANCER_CONTENT/2-169)

        } else {
            drawCancerContent(stream, template, 460f, y-DANGER_CANCER_CONTENT- 70, dto, CancerRepo.암종.췌장담도암)
            drawCancerContent(stream, template, 460f, y-DANGER_CANCER_CONTENT-135, dto, CancerRepo.암종.식도암)
            drawCancerContent(stream, template, 460f, y-DANGER_CANCER_CONTENT-200, dto, CancerRepo.암종.기타암종)
            drawCancerDashLine(stream, 352f, y - DANGER_CANCER_CONTENT/2-145, 394f, y - DANGER_CANCER_CONTENT/2- 70)
            drawCancerDashLine(stream, 345f, y - DANGER_CANCER_CONTENT/2- 90, 394f, y - DANGER_CANCER_CONTENT/2-135)
        }

        style = template.resource().styleContentRegualar().clone().fontSize(7f).color(Color(159,160,160))
        stream.paragraph(85f-width/2, y-DANGER_CONTENT_RATE-16, 400f, AlignHorizontal.LEFT, TextBlock(style, template.lblDangerTMI()))

        stream.restoreGraphicsState()
        return stream
    }
    private fun drawCancerDashLine(stream: PDPageContentStreamPageAccessible, sx: Float, sy: Float, ex: Float, ey: Float) {
        stream.circle(sx, sy, 1f).setNonStrokingColor(Color.GRAY).fill()
        stream.line(sx, sy, ex, ey).setStrokingColor(Color.GRAY).setLineWidth(0.3f).setLineDashPattern(floatArrayOf(1f, 1.5f), 1f).stroke()
        stream.circle(ex, ey, 1f).setNonStrokingColor(Color.GRAY).fill()
    }
    private fun drawCancerContent(stream: PDPageContentStreamPageAccessible,
                     template: AvoidTemplate<AvoidResource>,
                     x: Float, y: Float, dto: AvoidDto, cancer: CancerRepo.암종) {
        val repo = CancerRepo()
        val age : Int = dto.age!!.toInt()
        var img = template.resource().imgCancerTypeContent()
        var width = img.width * DANGER_CANCER_CONTENT / img.height
        val ppv = if(round(repo.findASRbyAgeAndCancerAndSex(cancer, age, dto.sex!!)!!.div(1000)*100)/100 == 0.0) 0.01 else round(repo.findASRbyAgeAndCancerAndSex(cancer, age, dto.sex!!)!!.div(1000)*100)/100
        stream.drawImage(img, x-width/2, y, width, DANGER_CANCER_CONTENT)

        img = template.resource().imgCancerTypeImage(cancer.name)
        width = img.width * DANGER_CANCER_ICON / img.height
        stream.drawImage(img, x-width/2-40, y+15, width, DANGER_CANCER_ICON)

        img = template.resource().imgUnderBar()
        width = img.width * DANGER_UNDERBAR_RATE / img.height
        stream.drawImage(img, x-width/2+23, y+10, width, DANGER_UNDERBAR_RATE+7.5f)
        var style = template.resource().styleContentRegualar().clone().fontSize(9f).color(Color(35,24,15))
        stream.paragraph(x-41, y+7, 100f, AlignHorizontal.CENTER,TextBlock(style, template.lblDangerCancerName(convNameToNum(cancer.name))))

        style = template.resource().styleContentRegualar().clone().fontSize(6.5f).color(Color(114,113,113))
        stream.paragraph(x+21, y+7, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))

        img = template.resource().imgBarGray()
        width = img.width * DANGER_BAR_RATE / img.height
        stream.paragraph(x-width/2+17, y+18+DANGER_BAR_RATE,  100f, AlignHorizontal.CENTER, TextBlock(style, "${ppv}"+"%"))
        stream.drawImage(img, x-width/2+9, y+14, width, DANGER_BAR_RATE)

        when(dto.result){
            AvoidDto.Results.RISK -> {
                val checker = dto.first.name == cancer.name
                img = if(checker) template.resource().imgBarDanger() else template.resource().imgBarMiddle()
                val riskPPV = repo.findPPVbyAgeAndCancerAndSex(cancer, age, dto.sex!!)!!
                val height = if(checker) if(riskPPV >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE

                val value = if(checker) "${riskPPV}%" else ">${ppv}"+"%"
                style =  if(checker) template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29))
                else template.resource().styleContentBold().clone().fontSize(7f).color(Color(217, 166, 71))
                stream.paragraph(x-width/2+49, y+18+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, x-width/2+41, y+14, width, height)

                if(checker) {
                    img = template.resource().imgCancerTypeDetect()
                    width = img.width * DANGER_GUIDE_RATE / img.height
                    stream.drawImage(img, x-width/2-26, y+DANGER_CANCER_ICON, width, DANGER_GUIDE_RATE)
                }
            }
            AvoidDto.Results.CONCERN -> {
                img = template.resource().imgBarMiddle()
                style = template.resource().styleContentBold().clone().fontSize(7f).color(Color(217, 166, 71))
                stream.paragraph(x-width/2+49, y+18+DANGER_BAR_RATE+5, 100f, AlignHorizontal.CENTER, TextBlock(style, ">${ppv}"+"%"))
                stream.drawImage(img, x-width/2+41, y+14, width, DANGER_BAR_RATE+5)
            }
            else -> {
                img = template.resource().imgBarNormal()
                style = template.resource().styleContentBold().clone().fontSize(7f).color(Color(81, 78, 145))
                stream.paragraph(x-width/2+49, y+18+DANGER_BAR_RATE, 100f, AlignHorizontal.CENTER, TextBlock(style, "≤${ppv}"+"%"))
                stream.drawImage(img, x-width/2+41, y+14, width, DANGER_BAR_RATE)
            }
        }
    }
    private fun drawCancerIcon(stream: PDPageContentStreamPageAccessible,
                               template: AvoidTemplate<AvoidResource>,
                               x: Float, y: Float, dto: AvoidDto, cancer: CancerRepo.암종) {
        val img = when(cancer) {
            CancerRepo.암종.식도암 -> template.resource().imgEsop (dto.first.name)
            CancerRepo.암종.폐암   -> template.resource().imgLung (dto.first.name)
            CancerRepo.암종.간암   -> template.resource().imgLiver(dto.first.name)
            CancerRepo.암종.췌장담도암 -> template.resource().imgPanc (dto.first.name)
            CancerRepo.암종.대장암 -> template.resource().imgColon(dto.first.name)
            else -> template.resource().imgOvary(dto.first.name)
        }
        val width = img.width * DANGER_ICON_RATE / img.height
        stream.drawImage(img, x-width/2, y, width, DANGER_ICON_RATE)
    }
    private fun convNameToNum(name: String) = when(name){
        "폐암"     -> 1
        "대장암"   -> 2
        "간암"     -> 3
        "췌장담도암"   -> 4
        "식도암"   -> 5
        "난소암"   -> 6
        "기타암종"-> 7
        "유방암"   -> 8
        else       -> 0
    }
    companion object {
        private const val DANGER_TITLE_RATE = 20.9f
        private const val DANGER_CONTENT_RATE = 291f
        private const val DANGER_HUMAN_RATE = 200f
        private const val DANGER_CANCER_CONTENT = 60F
        private const val DANGER_CANCER_ICON = 35F
        private const val DANGER_GUIDE_RATE = 15f
        private const val DANGER_ICON_RATE = 40f
        private const val DANGER_UNDERBAR_RATE = 1.5f
        private const val DANGER_BAR_RATE = 4.5f
    }
}
