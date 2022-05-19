package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color
import kotlin.math.round

class SectionDetailResultAnalysis(private var y: Float = 685f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        val style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(12f)
        stream.drawImage(img, 305f - width / 2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, dto!!.patientName+"님의 상세 결과 해석"))

        y = y-RESULT_CONTENT_RATE-10
        img = template.resource().imgDetailResultOverview()
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(img, 160f - width / 2, y, width, RESULT_CONTENT_RATE)

        img = template.resource().imgDetailResultTable()
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(img, 413f - width / 2, y, width, RESULT_CONTENT_RATE)

        img = when{
            dto.result == AvoidDto.Results.고위험 || dto.result == AvoidDto.Results.기타암종 -> template.resource().imgDetailResultRisk("고위험")
            dto.result == AvoidDto.Results.저위험 -> template.resource().imgDetailResultRisk("저위험")
            else ->  template.resource().imgDetailResultRisk("저위험")
        }
        width = img.width * RESULT_IMAGE_LOW_RATE / img.height
        stream.drawImage(img, 165 - width/2, y+40, width, RESULT_IMAGE_LOW_RATE)

        val styleRegular = template.resource().styleContentRegualar().clone().fontSize(6f)
        val styleBold    = template.resource().styleContentBold().clone().fontSize(26f)

        stream.paragraph(154f,y+28, 80f, AlignHorizontal.RIGHT, TextBlock(styleRegular, template.lblResultAnalysis(dto.patientName!!)))
        stream.paragraph(158f, y+15, 80f, AlignHorizontal.LEFT, TextBlock(
            if(dto.result == AvoidDto.Results.고위험 || dto.result == AvoidDto.Results.기타암종) styleBold.clone().color(Color(217, 52, 29)) else styleBold.clone().color(Color(128,128,128)),
            if(dto.result == AvoidDto.Results.고위험 || dto.result == AvoidDto.Results.기타암종) "고위험" else "저위험"))

        stream.paragraph(410f, y+82, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(Color(255,255,255)).fontSize(10f), "1순위"))
        stream.paragraph(505f, y+82, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(Color(255,255,255)).fontSize(10f), "2순위"))
        stream.paragraph(320f, y+66, 50f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(8f), "의심 암종"))
        if(dto.result != AvoidDto.Results.저위험){
            stream.paragraph(410f, y+66, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(8f), dto.first.name))
            stream.paragraph(505f, y+66, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(8f), dto.second.name))
        } else {
            stream.paragraph(410f, y+66, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), "해당 없음"))
            stream.paragraph(505f, y+66, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), "해당 없음"))
        }
        stream.paragraph(320f, y+43, 50f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(8f), "위험도 비교"))
        img = template.resource().imgSmallSquarePatient()
        width = img.width * CONTENT_SQUARE_RATE / img.height
        stream.drawImage(img, 293f - width / 2, y+20, width, CONTENT_SQUARE_RATE)
        img = template.resource().imgSmallSquareAverage()
        stream.drawImage(img, 293f - width / 2, y+10, width, CONTENT_SQUARE_RATE)

        stream.paragraph(300f, y+21, 50f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(6f), "${dto.patientName}님"))
        stream.paragraph(300f, y+11, 60f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(6f), patientInfo(dto.age!!, dto.sex!!)))

        if(dto.result != AvoidDto.Results.저위험) {
            img = template.resource().imgBackgroundCancer(dto.first.name)
            width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 410 - width/2, y+7, width, RESULT_IMAGE_LOW_RATE)

            img = template.resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var asr = round(dto.first.asr.div(1000) * 100) / 100
            var height = if (asr >= 50) CONTENT_SQUARE_RATE * 10 else CONTENT_SQUARE_RATE
            stream.drawImage(img, 395 - width / 2, y, width, height)
            stream.paragraph(
                395f,
                y + height + 5,
                60f,
                AlignHorizontal.CENTER,
                TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), asr.toString() + "%")
            )

            img = template.resource().imgBarDanger()
            height = if (dto.first.ppv >= 50) CONTENT_SQUARE_RATE * 7 else CONTENT_SQUARE_RATE * 3.5f
            stream.drawImage(img, 425 - width / 2, y, width, height)
            stream.paragraph(
                425f,
                y + height + 5,
                60f,
                AlignHorizontal.CENTER,
                TextBlock(styleBold.clone().color(Color(217, 52, 29)).fontSize(8f), dto.first.ppv.toString() + "%")
            )

            if(dto.result != AvoidDto.Results.기타암종) {
                img = template.resource().imgBackgroundCancer(dto.second.name)
                width = img.width * RESULT_IMAGE_LOW_RATE / img.height
                stream.drawImage(img, 505 - width/2, y+7, width, RESULT_IMAGE_LOW_RATE)

                img = template.resource().imgBarGray()
                width = img.width * CONTENT_SQUARE_RATE / img.height

                asr = round(dto.second.asr.div(1000) * 100) / 100
                height = if (asr >= 50) CONTENT_SQUARE_RATE * 10 else CONTENT_SQUARE_RATE
                stream.drawImage(img, 490 - width / 2, y, width, height)
                stream.paragraph(
                    490f,
                    y + height + 5,
                    60f,
                    AlignHorizontal.CENTER,
                    TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), asr.toString() + "%")
                )

                img = template.resource().imgBarDanger()
                height = if (dto.second.ppv >= 50) CONTENT_SQUARE_RATE * 7 else CONTENT_SQUARE_RATE * 3.5f
                stream.drawImage(img, 520 - width / 2, y, width, height)
                stream.paragraph(
                    520f,
                    y + height + 5,
                    60f,
                    AlignHorizontal.CENTER,
                    TextBlock(
                        styleBold.clone().color(Color(217, 52, 29)).fontSize(8f),
                        dto.second.ppv.toString() + "%"
                    )
                )
            }
            else {
                stream.paragraph(505f, y+66, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), "해당 없음"))
                stream.paragraph(505f, y+30, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), "해당 없음"))
            }
        } else {
            stream.paragraph(410f, y+30, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), "해당 없음"))
            stream.paragraph(505f, y+30, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), "해당 없음"))
        }

        return stream
    }
    private fun patientInfo(age: String, sex: Sex) : String{
        val ageStream: String = (age.toInt()/10*10).toString()
        val cut: String = when{
            age.substring(age.length-1, age.length).toInt() >= 5 -> "후반"
            else -> "초반"
        }
        val sexStr: String = when{
            sex == Sex.F -> "여성"
            else -> "남성"
        }
        return ageStream+"대 "+cut+" "+sexStr+" 평균"
    }
    companion object {
        private const val CONTENT_SQUARE_RATE = 6f
        private const val CONTENT_TITLE_RATE = 21f
        private const val RESULT_CONTENT_RATE = 94f
        private const val RESULT_IMAGE_LOW_RATE = 43f
    }
}