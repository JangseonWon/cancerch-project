package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.avoid.repository.CancerRepo
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.time.LocalDate
import java.time.Period
import kotlin.math.round
import kotlin.math.roundToInt

class SectionCancerTypeDanger(private val y: Float = 410f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        val repo: CancerRepo = CancerRepo()
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
        stream.drawImage(img, 307f-width/2, y-DANGER_TITLE_RATE-25f, width, DANGER_GUIDE_RATE)
        stream.paragraph(456.5f-width/2, y-DANGER_TITLE_RATE-20f, 300f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDangerIntro()))

        img = template.resource().imgHuman()
        width = img.width * DANGER_HUMAN_RATE / img.height
        stream.drawImage(img, 310f - width/2, y- DANGER_HUMAN_RATE-70, width, DANGER_HUMAN_RATE)

        img = template.resource().imgEsop(dto!!.first.name, dto.second.name)
        width = img.width * DANGER_ICON_RATE / img.height / 1.8f
        stream.drawImage(img, 296f - width/2, y- DANGER_ICON_RATE / 1.8f-102, width, DANGER_ICON_RATE / 1.8f)

        img = template.resource().imgLung(dto.first.name, dto.second.name)
        width = img.width * DANGER_ICON_RATE / img.height * 1.2f
        stream.drawImage(img, 310f - width/2, y- DANGER_ICON_RATE * 1.2f-130, width, DANGER_ICON_RATE * 1.2f)

        img = template.resource().imgLiver(dto.first.name, dto.second.name)
        width = img.width * DANGER_ICON_RATE / img.height * 0.5f
        stream.drawImage(img, 302f - width/2, y- DANGER_ICON_RATE * 0.5f-185, width, DANGER_ICON_RATE * 0.5f)

        img = template.resource().imgPanc(dto.first.name, dto.second.name)
        width = img.width * DANGER_ICON_RATE / img.height * 0.4f
        stream.drawImage(img, 323f - width/2, y- DANGER_ICON_RATE * 0.4f-190, width, DANGER_ICON_RATE * 0.4f)

        img = template.resource().imgColon(dto.first.name, dto.second.name)
        width = img.width * DANGER_ICON_RATE / img.height
        stream.drawImage(img, 311f - width/2, y- DANGER_ICON_RATE-210, width, DANGER_ICON_RATE)

        img = template.resource().imgBreast(dto.first.name, dto.second.name)
        width = img.width * DANGER_ICON_RATE / img.height
        stream.drawImage(img, 335f - width/2, y- DANGER_ICON_RATE-128, width, DANGER_ICON_RATE)

        img = template.resource().imgOvary(dto.first.name, dto.second.name)
        width = img.width * DANGER_ICON_RATE / img.height
        stream.drawImage(img, 345f - width/2, y- DANGER_ICON_RATE-200, width, DANGER_ICON_RATE)

        style = template.resource().styleContentRegualar().clone().fontSize(9f).color(Color(35,24,15))
        stream.paragraph(132f-width/2, y- DANGER_CONTENT_RATE+190, 100f, AlignHorizontal.CENTER,TextBlock(style, "폐암"))
        stream.paragraph(132f-width/2, y- DANGER_CONTENT_RATE+133, 100f, AlignHorizontal.CENTER, TextBlock(style, "대장암"))
        stream.paragraph(132f-width/2, y- DANGER_CONTENT_RATE+75, 100f, AlignHorizontal.CENTER, TextBlock(style, "간암"))
        stream.paragraph(132f-width/2, y- DANGER_CONTENT_RATE+17, 100f, AlignHorizontal.CENTER, TextBlock(style, "췌장암"))
        stream.paragraph(450f-width/2, y- DANGER_CONTENT_RATE+190, 100f, AlignHorizontal.CENTER,TextBlock(style, "식도암"))
        stream.paragraph(450f-width/2, y- DANGER_CONTENT_RATE+133, 100f, AlignHorizontal.CENTER, TextBlock(style, "유방암"))
        stream.paragraph(450f-width/2, y- DANGER_CONTENT_RATE+75, 100f, AlignHorizontal.CENTER, TextBlock(style, "난소암"))
        stream.paragraph(450f-width/2, y- DANGER_CONTENT_RATE+17, 100f, AlignHorizontal.CENTER, TextBlock(style, "기타 암종"))

        style = template.resource().styleContentRegualar().clone().fontSize(6f).color(Color(114,113,113))
        stream.paragraph(189f-width/2, y- DANGER_CONTENT_RATE+187, 100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))
        stream.paragraph(189f-width/2, y- DANGER_CONTENT_RATE+130, 100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))
        stream.paragraph(189f-width/2, y- DANGER_CONTENT_RATE+72,  100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))
        stream.paragraph(189f-width/2, y- DANGER_CONTENT_RATE+14,  100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))
        stream.paragraph(508f-width/2, y- DANGER_CONTENT_RATE+187, 100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))
        stream.paragraph(508f-width/2, y- DANGER_CONTENT_RATE+130, 100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))
        stream.paragraph(508f-width/2, y- DANGER_CONTENT_RATE+72,  100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))
        stream.paragraph(508f-width/2, y- DANGER_CONTENT_RATE+14,  100f, AlignHorizontal.CENTER, TextBlock(style, "평균 위험도    수검자"))

        style = template.resource().styleContentRegualar().clone().fontSize(7f).color(Color(114,113,113))
        var age : Int = dto.age!!.toInt()
        stream.paragraph(177f-width/2, y- DANGER_CONTENT_RATE+207,  100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.폐암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(177f-width/2, y- DANGER_CONTENT_RATE+150,  100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.대장암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(177f-width/2, y- DANGER_CONTENT_RATE+92,   100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.간암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(177f-width/2, y- DANGER_CONTENT_RATE+34,   100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.췌장암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(495f-width/2, y- DANGER_CONTENT_RATE+207,  100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.식도암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(495f-width/2, y- DANGER_CONTENT_RATE+150,  100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.유방암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(495f-width/2, y- DANGER_CONTENT_RATE+92,   100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.난소암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(495f-width/2, y- DANGER_CONTENT_RATE+34,   100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.모든암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))

        img = template.resource().imgBarGray()
        width = img.width * DANGER_BAR_RATE / img.height
        stream.drawImage(img, 157f-width/2, y- DANGER_CONTENT_RATE+195, width, DANGER_BAR_RATE)
        stream.drawImage(img, 157f-width/2, y- DANGER_CONTENT_RATE+138, width, DANGER_BAR_RATE)
        stream.drawImage(img, 157f-width/2, y- DANGER_CONTENT_RATE+80,  width, DANGER_BAR_RATE)
        stream.drawImage(img, 157f-width/2, y- DANGER_CONTENT_RATE+22,  width, DANGER_BAR_RATE)
        stream.drawImage(img, 475f-width/2, y- DANGER_CONTENT_RATE+195, width, DANGER_BAR_RATE)
        stream.drawImage(img, 475f-width/2, y- DANGER_CONTENT_RATE+138, width, DANGER_BAR_RATE)
        stream.drawImage(img, 475f-width/2, y- DANGER_CONTENT_RATE+80,  width, DANGER_BAR_RATE)
        stream.drawImage(img, 475f-width/2, y- DANGER_CONTENT_RATE+22,  width, DANGER_BAR_RATE)

        img = if(dto.first.name == "폐암" || dto.second.name == "폐암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
        var height = if(dto.first.name == "폐암" || dto.second.name == "폐암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.폐암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
        var value = if(dto.first.name == "폐암" || dto.second.name == "폐암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.폐암, age, dto.sex!!)}%" else "> ${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.폐암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
        style =  if(dto.first.name == "폐암" || dto.second.name == "폐암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
        stream.paragraph(195f-width/2, y- DANGER_CONTENT_RATE+202+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
        stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+195, width, height!!.toFloat())

        img = if(dto.first.name == "대장암" || dto.second.name == "대장암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
        height = if(dto.first.name == "대장암" || dto.second.name == "대장암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.대장암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
        value = if(dto.first.name == "대장암" || dto.second.name == "대장암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.대장암, age, dto.sex!!)}%" else "> ${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.대장암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
        style =  if(dto.first.name == "대장암" || dto.second.name == "대장암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
        stream.paragraph(195f-width/2, y- DANGER_CONTENT_RATE+145+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
        stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+138, width, height!!.toFloat())

        img = if(dto.first.name == "간암" || dto.second.name == "간암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
        height = if(dto.first.name == "간암" || dto.second.name == "간암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.간암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
        value = if(dto.first.name == "간암" || dto.second.name == "간암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.간암, age, dto.sex!!)}%" else "> ${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.간암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
        style =  if(dto.first.name == "간암" || dto.second.name == "간암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
        stream.paragraph(195f-width/2, y- DANGER_CONTENT_RATE+87+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
        stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+80, width, height!!.toFloat())

        img = if(dto.first.name == "췌장암" || dto.second.name == "췌장암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
        height = if(dto.first.name == "췌장암" || dto.second.name == "췌장암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.췌장암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
        value = if(dto.first.name == "췌장암" || dto.second.name == "췌장암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.췌장암, age, dto.sex!!)}%" else "> ${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.췌장암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
        style =  if(dto.first.name == "췌장암" || dto.second.name == "췌장암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
        stream.paragraph(195f-width/2, y- DANGER_CONTENT_RATE+29+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
        stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+22, width,  height!!.toFloat())

        img = if(dto.first.name == "식도암" || dto.second.name == "식도암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
        height = if(dto.first.name == "식도암" || dto.second.name == "식도암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.식도암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
        value = if(dto.first.name == "식도암" || dto.second.name == "식도암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.식도암, age, dto.sex!!)}%" else "> ${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.식도암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
        style =  if(dto.first.name == "식도암" || dto.second.name == "식도암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
        stream.paragraph(513f-width/2, y- DANGER_CONTENT_RATE+202+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
        stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+195, width, height!!.toFloat())

        img = if(dto.first.name == "유방암" || dto.second.name == "유방암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
        height = if(dto.first.name == "유방암" || dto.second.name == "유방암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.유방암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
        value = if(dto.first.name == "유방암" || dto.second.name == "유방암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.유방암, age, dto.sex!!)}%" else "> ${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.유방암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
        style =  if(dto.first.name == "유방암" || dto.second.name == "유방암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
        stream.paragraph(513f-width/2, y- DANGER_CONTENT_RATE+145+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
        stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+138, width, height!!.toFloat())

        img = if(dto.first.name == "난소암" || dto.second.name == "난소암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
        height = if(dto.first.name == "난소암" || dto.second.name == "난소암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.난소암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
        value = if(dto.first.name == "난소암" || dto.second.name == "난소암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.난소암, age, dto.sex!!)}%" else "> ${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.난소암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
        style =  if(dto.first.name == "난소암" || dto.second.name == "난소암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
        stream.paragraph(513f-width/2, y- DANGER_CONTENT_RATE+87+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
        stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+80, width, height!!.toFloat())

        img = if(dto.first.name == "기타암종" || dto.second.name == "기타암종") template.resource().imgBarDanger() else template.resource().imgBarNormal()
        height = if(dto.first.name == "기타암종" || dto.second.name == "기타암종") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.모든암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
        value = if(dto.first.name == "기타암종" || dto.second.name == "기타암종") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.모든암, age, dto.sex!!)}%" else "> ${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.모든암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
        style =  if(dto.first.name == "기타암종" || dto.second.name == "기타암종") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
        stream.paragraph(513f-width/2, y- DANGER_CONTENT_RATE+29+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
        stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+22, width, height!!.toFloat())

        img = template.resource().imgLine("폐암")
        width = img.width*80f / img.height
        stream.drawImage(img, 252f-width/2, y- DANGER_CONTENT_RATE+132, width, 78f)
        style = template.resource().styleContentRegualar().clone().fontSize(7f).color(Color(159,160,160))

        img = template.resource().imgLine("대장암")
        width = img.width*88f / img.height
        stream.drawImage(img, 254f-width/2, y- DANGER_CONTENT_RATE+72, width, 78f)

        img = template.resource().imgLine("간암")
        width = img.width*10f / img.height
        stream.drawImage(img, 253f-width/2, y- DANGER_CONTENT_RATE+88, width, 10f)

        img = template.resource().imgLine("췌장암")
        width = img.width*55f / img.height
        stream.drawImage(img, 264f-width/2, y- DANGER_CONTENT_RATE+33, width, 55f)

        img = template.resource().imgLine("식도암")
        width = img.width*41f / img.height
        stream.drawImage(img, 353f-width/2, y- DANGER_CONTENT_RATE+168, width, 43f)

        img = template.resource().imgLine("유방암")
        width = img.width*20f / img.height
        stream.drawImage(img, 373f-width/2, y- DANGER_CONTENT_RATE+132, width, 20f)

        img = template.resource().imgLine("난소암")
        width = img.width*18f / img.height
        stream.drawImage(img, 378f-width/2, y- DANGER_CONTENT_RATE+73, width, 20f)

        stream.paragraph(85f-width/2, y-DANGER_CONTENT_RATE-8, 400f, AlignHorizontal.LEFT, TextBlock(style, template.lblDangerTMI()))

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val DANGER_TITLE_RATE = 20.9f
        private const val DANGER_CONTENT_RATE = 291f
        private const val DANGER_HUMAN_RATE = 200f
        private const val DANGER_GUIDE_RATE = 15f
        private const val DANGER_ICON_RATE = 40f
        private const val DANGER_BAR_RATE = 4f
    }
}