package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.avoid.repository.CancerRepo
import com.greencross.lims.report.builder.Sex
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

        val repo = CancerRepo()
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
        var bold = template.resource().styleContentBold().clone().fontSize(7.3f)
        stream.drawImage(img, 307f-width/2, y-DANGER_TITLE_RATE-25f, width, DANGER_GUIDE_RATE)
        stream.paragraph(456.5f-width/2, y-DANGER_TITLE_RATE-20f, 300f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDangerIntroStart()),
            TextBlock(bold, template.lblDangerIntroBridge()),
            TextBlock(style, template.lblDangerIntroEnd())
        )

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

        img = template.resource().imgOvary(dto.first.name, dto.second.name)
        width = img.width * DANGER_ICON_RATE / img.height
        stream.drawImage(img, 345f - width/2, y- DANGER_ICON_RATE-200, width, DANGER_ICON_RATE)

        style = template.resource().styleContentRegualar().clone().fontSize(9f).color(Color(35,24,15))
        stream.paragraph(132f-width/2, y- DANGER_CONTENT_RATE+160, 100f, AlignHorizontal.CENTER,TextBlock(style, template.lblDangerCancerName(1)))
        stream.paragraph(132f-width/2, y- DANGER_CONTENT_RATE+103, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerCancerName(2)))
        stream.paragraph(132f-width/2, y- DANGER_CONTENT_RATE+46, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerCancerName(3)))
        stream.paragraph(450f-width/2, y- DANGER_CONTENT_RATE+190, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerCancerName(4)))
        stream.paragraph(450f-width/2, y- DANGER_CONTENT_RATE+131, 100f, AlignHorizontal.CENTER,TextBlock(style, template.lblDangerCancerName(5)))
        stream.paragraph(450f-width/2, y- DANGER_CONTENT_RATE+75, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerCancerName(6)))
        stream.paragraph(450f-width/2, y- DANGER_CONTENT_RATE+17, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerCancerName(7)))

        style = template.resource().styleContentRegualar().clone().fontSize(6f).color(Color(114,113,113))
        stream.paragraph(189f-width/2, y- DANGER_CONTENT_RATE+157, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))
        stream.paragraph(189f-width/2, y- DANGER_CONTENT_RATE+100, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))
        stream.paragraph(189f-width/2, y- DANGER_CONTENT_RATE+43,  100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))
        stream.paragraph(508f-width/2, y- DANGER_CONTENT_RATE+187,  100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))
        stream.paragraph(508f-width/2, y- DANGER_CONTENT_RATE+128, 100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))
        if(dto.sex == Sex.F) {
            stream.paragraph(508f-width/2, y- DANGER_CONTENT_RATE+72,  100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))
        }
        stream.paragraph(508f-width/2, y- DANGER_CONTENT_RATE+14,  100f, AlignHorizontal.CENTER, TextBlock(style, template.lblDangerGraphGuide()))

        style = template.resource().styleContentRegualar().clone().fontSize(7f).color(Color(114,113,113))
        var age : Int = dto.age!!.toInt()
        stream.paragraph(177f-width/2, y- DANGER_CONTENT_RATE+175,  100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.폐암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(177f-width/2, y- DANGER_CONTENT_RATE+117,  100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.대장암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(177f-width/2, y- DANGER_CONTENT_RATE+59,   100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.간암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(495f-width/2, y- DANGER_CONTENT_RATE+207,   100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.췌장암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        stream.paragraph(495f-width/2, y- DANGER_CONTENT_RATE+146,  100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.식도암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        if(dto.sex == Sex.F) {
            stream.paragraph(495f-width/2, y- DANGER_CONTENT_RATE+89,   100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.난소암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))
        }
        stream.paragraph(495f-width/2, y- DANGER_CONTENT_RATE+31,   100f, AlignHorizontal.CENTER, TextBlock(style, "${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.기타암종, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"))

        img = template.resource().imgBarGray()
        width = img.width * DANGER_BAR_RATE / img.height
        stream.drawImage(img, 157f-width/2, y- DANGER_CONTENT_RATE+166, width, DANGER_BAR_RATE)
        stream.drawImage(img, 157f-width/2, y- DANGER_CONTENT_RATE+108, width, DANGER_BAR_RATE)
        stream.drawImage(img, 157f-width/2, y- DANGER_CONTENT_RATE+50,  width, DANGER_BAR_RATE)
        stream.drawImage(img, 475f-width/2, y- DANGER_CONTENT_RATE+196,  width, DANGER_BAR_RATE)
        stream.drawImage(img, 475f-width/2, y- DANGER_CONTENT_RATE+137, width, DANGER_BAR_RATE)
        if(dto.sex == Sex.F) {
            stream.drawImage(img, 475f - width / 2, y - DANGER_CONTENT_RATE + 80, width, DANGER_BAR_RATE)
        }
        stream.drawImage(img, 475f-width/2, y- DANGER_CONTENT_RATE+22,  width, DANGER_BAR_RATE)

        when(dto.result){
            AvoidDto.Results.CONCENT ->{
                img = if(dto.first.name == "폐암" || dto.second.name == "폐암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
                var height = if(dto.first.name == "폐암" || dto.second.name == "폐암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.폐암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
                var value = if(dto.first.name == "폐암" || dto.second.name == "폐암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.폐암, age, dto.sex!!)}%" else ">${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.폐암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
                style =  if(dto.first.name == "폐암" || dto.second.name == "폐암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
                stream.paragraph(195f-width/2, y- DANGER_CONTENT_RATE+173+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+166, width, height)

                img = if(dto.first.name == "대장암" || dto.second.name == "대장암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
                height = if(dto.first.name == "대장암" || dto.second.name == "대장암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.대장암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
                value = if(dto.first.name == "대장암" || dto.second.name == "대장암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.대장암, age, dto.sex!!)}%" else ">${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.대장암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
                style =  if(dto.first.name == "대장암" || dto.second.name == "대장암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
                stream.paragraph(195f-width/2, y- DANGER_CONTENT_RATE+116+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+109, width, height)

                img = if(dto.first.name == "간암" || dto.second.name == "간암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
                height = if(dto.first.name == "간암" || dto.second.name == "간암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.간암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
                value = if(dto.first.name == "간암" || dto.second.name == "간암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.간암, age, dto.sex!!)}%" else ">${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.간암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
                style =  if(dto.first.name == "간암" || dto.second.name == "간암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
                stream.paragraph(195f-width/2, y- DANGER_CONTENT_RATE+58+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+51, width, height)

                img = if(dto.first.name == "췌장암" || dto.second.name == "췌장암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
                height = if(dto.first.name == "췌장암" || dto.second.name == "췌장암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.췌장암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
                value = if(dto.first.name == "췌장암" || dto.second.name == "췌장암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT,CancerRepo.암종.췌장암, age, dto.sex!!)}%" else ">${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.췌장암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
                style =  if(dto.first.name == "췌장암" || dto.second.name == "췌장암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
                stream.paragraph(513f-width/2, y- DANGER_CONTENT_RATE+203+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+196, width,  height)

                img = if(dto.first.name == "식도암" || dto.second.name == "식도암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
                height = if(dto.first.name == "식도암" || dto.second.name == "식도암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.식도암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
                value = if(dto.first.name == "식도암" || dto.second.name == "식도암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT,CancerRepo.암종.식도암, age, dto.sex!!)}%" else ">${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.식도암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
                style =  if(dto.first.name == "식도암" || dto.second.name == "식도암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
                stream.paragraph(513f-width/2, y- DANGER_CONTENT_RATE+143+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+137, width, height)

                if(dto.sex == Sex.F) {
                    img = if(dto.first.name == "난소암" || dto.second.name == "난소암") template.resource().imgBarDanger() else template.resource().imgBarNormal()
                    height = if(dto.first.name == "난소암" || dto.second.name == "난소암") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.난소암, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
                    value = if(dto.first.name == "난소암" || dto.second.name == "난소암") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.난소암, age, dto.sex!!)}%" else ">${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.난소암, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
                    style =  if(dto.first.name == "난소암" || dto.second.name == "난소암") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
                    stream.paragraph(513f-width/2, y- DANGER_CONTENT_RATE+87+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                    stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+80, width, height)
                }else{
                    val styleBold    = template.resource().styleContentBold().clone().fontSize(26f)
                    stream.paragraph(488f, y- DANGER_CONTENT_RATE+87+height, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), "해당 없음"))
                }
                img = if(dto.first.name == "기타암종" || dto.second.name == "기타암종") template.resource().imgBarDanger() else template.resource().imgBarNormal()
                height = if(dto.first.name == "기타암종" || dto.second.name == "기타암종") if(repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.기타암종, age, dto.sex!!)!! >= 50) DANGER_BAR_RATE*5 else DANGER_BAR_RATE*4 else DANGER_BAR_RATE
                value = if(dto.first.name == "기타암종" || dto.second.name == "기타암종") "${repo.findPPVbyAgeAndCancerAndSex(CancerRepo.결과.CONCENT, CancerRepo.암종.기타암종, age, dto.sex!!)}%" else ">${round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.기타암종, age, dto.sex!!)!!.div(1000)*100)/100}"+"%"
                style =  if(dto.first.name == "기타암종" || dto.second.name == "기타암종") template.resource().styleContentBold().clone().fontSize(8f).color(Color(217, 52, 29)) else template.resource().styleContentBold().clone().fontSize(7f)
                stream.paragraph(513f-width/2, y- DANGER_CONTENT_RATE+29+height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+22, width, height)
            }
            else -> {
                img = template.resource().imgBarNormal()
                style =  template.resource().styleContentBold().clone().fontSize(7f)

                var asr = round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.폐암, age, dto.sex!!)!!.div(1000)*100)/100
                var value = if(dto.result == AvoidDto.Results.ATTENTION) ">${asr}%"  else "≤${asr}"+"%"
                var height = if(dto.result == AvoidDto.Results.ATTENTION) y- DANGER_CONTENT_RATE+173+DANGER_BAR_RATE else y- DANGER_CONTENT_RATE+173
                stream.paragraph(195f-width/2, height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+166, width, DANGER_BAR_RATE)

                asr = round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.대장암, age, dto.sex!!)!!.div(1000)*100)/100
                value = if(dto.result == AvoidDto.Results.ATTENTION) ">${asr}%"  else "≤${asr}"+"%"
                height = if(dto.result == AvoidDto.Results.ATTENTION) y- DANGER_CONTENT_RATE+116+DANGER_BAR_RATE else  y- DANGER_CONTENT_RATE+116
                stream.paragraph(195f-width/2, height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+109, width, DANGER_BAR_RATE)

                asr = round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.간암, age, dto.sex!!)!!.div(1000)*100)/100
                value = if(dto.result == AvoidDto.Results.ATTENTION) ">${asr}%"  else "≤${asr}"+"%"
                height = if(dto.result == AvoidDto.Results.ATTENTION) y- DANGER_CONTENT_RATE+58+DANGER_BAR_RATE else y- DANGER_CONTENT_RATE+58
                stream.paragraph(195f-width/2, height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 187f-width/2, y- DANGER_CONTENT_RATE+51, width, DANGER_BAR_RATE)

                asr = round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.췌장암, age, dto.sex!!)!!.div(1000)*100)/100
                value =if(dto.result == AvoidDto.Results.ATTENTION) ">${asr}%" else "≤${asr}"+"%"
                height = if(dto.result == AvoidDto.Results.ATTENTION) y- DANGER_CONTENT_RATE+203+DANGER_BAR_RATE else y- DANGER_CONTENT_RATE+203
                stream.paragraph(513f-width/2, height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+196, width,  DANGER_BAR_RATE)

                asr = round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.식도암, age, dto.sex!!)!!.div(1000)*100)/100
                value = if(dto.result == AvoidDto.Results.ATTENTION) ">${asr}%" else "≤${asr}"+"%"
                height = if(dto.result == AvoidDto.Results.ATTENTION) y- DANGER_CONTENT_RATE+143+DANGER_BAR_RATE else y- DANGER_CONTENT_RATE+143
                stream.paragraph(513f-width/2, height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+137, width, DANGER_BAR_RATE)

                if(dto.sex == Sex.F) {
                    asr = round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.난소암, age, dto.sex!!)!!.div(1000)*100)/100
                    value = if(dto.result == AvoidDto.Results.ATTENTION) ">${asr}%" else "≤${asr}"+"%"
                    height = if(dto.result == AvoidDto.Results.ATTENTION) y- DANGER_CONTENT_RATE+87+DANGER_BAR_RATE else y- DANGER_CONTENT_RATE+87
                    stream.paragraph(513f-width/2, height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                    stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+80, width, DANGER_BAR_RATE)
                }else{
                    val styleBold    = template.resource().styleContentBold().clone().fontSize(26f)
                    stream.paragraph(488f, y- DANGER_CONTENT_RATE+87+DANGER_BAR_RATE, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), "해당 없음"))
                }
                asr = round(repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.기타암종, age, dto.sex!!)!!.div(1000)*100)/100
                value = if(dto.result == AvoidDto.Results.ATTENTION) ">${asr}%" else "≤${asr}"+"%"
                height = if(dto.result == AvoidDto.Results.ATTENTION) y- DANGER_CONTENT_RATE+29+DANGER_BAR_RATE else y- DANGER_CONTENT_RATE+29
                stream.paragraph(513f-width/2, height, 100f, AlignHorizontal.CENTER, TextBlock(style, value))
                stream.drawImage(img, 505f-width/2, y- DANGER_CONTENT_RATE+22, width, DANGER_BAR_RATE)
            }
        }
        img = template.resource().imgLine("폐암")
        width = img.width*50f / img.height
        stream.drawImage(img, 253f-width/2, y- DANGER_CONTENT_RATE+129, width, 50f)
        style = template.resource().styleContentRegualar().clone().fontSize(7f).color(Color(159,160,160))

        img = template.resource().imgLine("대장암")
        width = img.width*60f / img.height
        stream.drawImage(img, 255f-width/2, y- DANGER_CONTENT_RATE+62, width, 60f)

        img = template.resource().imgLine("간암")
        width = img.width*35f / img.height
        stream.drawImage(img, 256f-width/2, y- DANGER_CONTENT_RATE+62, width, 35f)

        img = template.resource().imgLine("췌장암")
        width = img.width*115f / img.height
        stream.drawImage(img, 359f-width/2, y- DANGER_CONTENT_RATE+93, width, 115f)

        img = template.resource().imgLine("식도암")
        width = img.width*19f / img.height
        stream.drawImage(img, 353f-width/2, y- DANGER_CONTENT_RATE+149, width, 19f)

        img = template.resource().imgLine("난소암")
        width = img.width*14f / img.height
        stream.drawImage(img, 381f-width/2, y- DANGER_CONTENT_RATE+79, width, 14f)

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