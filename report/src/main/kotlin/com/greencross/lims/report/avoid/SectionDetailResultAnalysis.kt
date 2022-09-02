package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

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
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, template.lblDetailResultAnalysisHeader(dto!!.patientName!!)))

        y = y-RESULT_CONTENT_RATE-10
        img = template.resource().imgDetailResultTable(dto.result)
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(img, 305f - width / 2, y, width, RESULT_CONTENT_RATE)

        img = when (dto.result) {
            AvoidDto.Results.CONCERN -> template.resource().imgDetailResultRisk("관심관리")
            AvoidDto.Results.RISK -> template.resource().imgDetailResultRisk("위험관리")
            else -> template.resource().imgDetailResultRisk("일반관리")
        }
        width = img.width * RESULT_IMAGE_LOW_RATE / img.height
        stream.drawImage(img, 165 - width/2, y+40, width, RESULT_IMAGE_LOW_RATE)

        val styleRegular = template.resource().styleContentRegualar().clone().fontSize(6f)
        val styleBold    = template.resource().styleContentBold().clone().fontSize(26f)

        val colors = when(dto.result){
            AvoidDto.Results.GENERAL     -> Color(141, 197, 86)
            AvoidDto.Results.CONCERN  -> Color(239, 167, 24)
            else                        -> Color(217,  52, 29)
        }

        stream.paragraph(147f,y+28, 80f, AlignHorizontal.RIGHT, TextBlock(styleRegular, template.lblDetailResultAnalysis(dto.patientName!!)))
        stream.paragraph(151f, y+15, 80f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().color(colors), template.lblResultToWord(dto.result)))

        stream.paragraph(312f, y+79, 50f, AlignHorizontal.CENTER,
            TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(8f), template.lblDetailResultAnalysisTableHeaderTop()))
        if(dto.result == AvoidDto.Results.RISK){
            stream.paragraph(403f, y+79, 100f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(9f), template.lblDetailResultAnalysisTableConcent(dto.first.name)))
            stream.paragraph(498f, y+79, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(9f), template.lblDetailResultAnalysisTableConcent()))
        } else {
            val results = when(dto.result){
                AvoidDto.Results.GENERAL -> template.lblDetailResultAnalysisTableNone1()
                else                    -> template.lblDetailResultAnalysisTableMidRisk1()
            }
            stream.paragraph(441f, y+79, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(8f), results))
        }


        if(dto.result != AvoidDto.Results.GENERAL){
            stream.paragraph(291f, y+24, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(6f), template.lblPatientSir(dto.patientName!!)))
            stream.paragraph(291f, y+14, 60f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(6f), template.lblPatientInfo(dto.age!!, dto.sex!!)))
            stream.paragraph(313f, y+48, 50f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(8f), template.lblDetailResultAnalysisTableHedaerBot()))
        }

        if(dto.result == AvoidDto.Results.GENERAL) {
            stream.paragraph(400f, y+35, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(8f), template.lblDetailResultAnalysisTableNone1()))
            stream.paragraph(495f, y+35, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128,128,128)).clone().fontSize(8f), template.lblDetailResultAnalysisTableNone2()))
            stream.paragraph(313f, y+35, 200f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0,0,0)).fontSize(8f), template.lblDetailResultAnalysisTableHedaerBot()))
        }
        else if(dto.result == AvoidDto.Results.CONCERN){
            img = template.resource().imgBackgroundCancer(dto.first.name)
            width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 400 - width/2, y+7, width, RESULT_IMAGE_LOW_RATE)

            img = template.resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE *2f
            stream.drawImage(img, 385 - width / 2, y, width, height)
            stream.paragraph(
                384f,
                y + height + 5,
                60f,
                AlignHorizontal.CENTER,
                TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), template.lblDetailResultAnlaysisTableContentCom())
            )

            img = template.resource().imgBarMiddle()
            height = CONTENT_SQUARE_RATE *4f
            stream.drawImage(img, 415 - width / 2, y, width, height)
            stream.paragraph(
                415f,
                y + height + 5,
                60f,
                AlignHorizontal.CENTER,
                TextBlock(styleBold.clone().color(colors).fontSize(8f), template.lblDetailResultAnalysisTableContentMID())
            )
            stream.paragraph(495f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11,11,11)).clone().fontSize(6f), template.lblDetailResultAnalysisTableMidRisk2()))
        }
        else{
            img = template.resource().imgBackgroundCancer(dto.first.name)
            width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 403 - width/2, y+7, width, RESULT_IMAGE_LOW_RATE)

            img = template.resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 388 - width / 2, y, width, height)
            stream.paragraph(
                387f,
                y + height + 5,
                60f,
                AlignHorizontal.CENTER,
                TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), template.lblDetailResultAnlaysisTableContentCom())
            )

            img = template.resource().imgBarDanger()
            height = CONTENT_SQUARE_RATE * 6f
            stream.drawImage(img, 418 - width / 2, y, width, height)
            stream.paragraph(
                418f,
                y + height + 5,
                60f,
                AlignHorizontal.CENTER,
                TextBlock(styleBold.clone().color(colors).fontSize(8f), template.lblDetailResultAnalysisTableContentHIG2())
            )

            img = template.resource().imgBackgroundCancer("기타암종")
            width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 498 - width/2, y+7, width, RESULT_IMAGE_LOW_RATE)

            img = template.resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 483 - width / 2, y, width, height)
            stream.paragraph(
                483f,
                y + height + 5,
                60f,
                AlignHorizontal.CENTER,
                TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), template.lblDetailResultAnlaysisTableContentCom())
            )

            img = template.resource().imgBarDanger()
            height = CONTENT_SQUARE_RATE * 3f
            stream.drawImage(img, 513 - width / 2, y, width, height)
            stream.paragraph(
                513f,
                y + height + 5,
                60f,
                AlignHorizontal.CENTER,
                TextBlock(styleBold.clone().color(colors).fontSize(8f), template.lblDetailResultAnalysisTableContentHIG1())
            )
        }
        return stream
    }

    companion object {
        private const val CONTENT_SQUARE_RATE = 6f
        private const val CONTENT_TITLE_RATE = 21f
        private const val RESULT_CONTENT_RATE = 94f
        private const val RESULT_IMAGE_LOW_RATE = 43f
    }
}