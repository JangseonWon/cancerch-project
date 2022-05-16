package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionDoubtCancer (private val y: Float = 508f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ Doubt Square Box, Title, Content's Image
        var img = template!!.resource().imgDoubtSquare()
        var width = img.width * DOUBT_SQUARE_RATE / img.height
        var style = template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f)
        stream.drawImage(img, 105f - width/2, y-DOUBT_SQUARE_RATE, width, DOUBT_SQUARE_RATE)
        stream.paragraph(150f - width/2,y - DOUBT_SQUARE_RATE + 63, 80f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDoubtSquareTitle()))

        img = template.resource().imgDoubtContentBox()
        width = img.width * DOUBT_SQUARE_RATE / img.height
        style = template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(12f)
        stream.drawImage(img, 355f - width/2, y-DOUBT_SQUARE_RATE, width, DOUBT_SQUARE_RATE )
        stream.paragraph(552f - width/2, y- DOUBT_SQUARE_RATE+73, 80f, AlignHorizontal.CENTER,
            TextBlock(style, "의심 암종"))

        if(dto!!.result != AvoidDto.Results.저위험) {
            img = template.resource().imgDoubtCenterLine()
            width = img.width * DOUBT_CENTER_LINE_RATE / img.height
            stream.drawImage(img, 355f - width / 2, y - DOUBT_CENTER_LINE_RATE - 25, width, DOUBT_CENTER_LINE_RATE)

            img = template.resource().imgDoubtCancer(dto.first.name)
            width = img.width * DOUBT_CANCER_RATE / img.height
            stream.drawImage(img, 195f - width / 2, y - DOUBT_CANCER_RATE - 28, width, DOUBT_CANCER_RATE)

            img = template.resource().imgRankFirst()
            width = img.width * DOUBT_RANK_RATE / img.height
            stream.drawImage(img, 215f - width / 2, y - DOUBT_RANK_RATE - 28, width, DOUBT_RANK_RATE)

            img = template.resource().imgDoubtCancerPercentages(dto.first.score!!)
            width = img.width * DOUBT_PERCENT_RATE / img.height
            stream.drawImage(img, 285f - width / 2, y - DOUBT_PERCENT_RATE - 38, width, DOUBT_PERCENT_RATE)

            if(dto.result == AvoidDto.Results.고위험){
                img = template.resource().imgDoubtCancer(dto.second.name)
                width = img.width * DOUBT_CANCER_RATE / img.height
                stream.drawImage(img, 392f - width / 2, y - DOUBT_CANCER_RATE - 28, width, DOUBT_CANCER_RATE)

                img = template.resource().imgRankSecond()
                width = img.width * DOUBT_RANK_RATE / img.height
                stream.drawImage(img, 410f - width / 2, y - DOUBT_RANK_RATE - 28, width, DOUBT_RANK_RATE)

                img = template.resource().imgDoubtCancerPercentages(dto.second.score!!)
                width = img.width * DOUBT_PERCENT_RATE / img.height
                stream.drawImage(img, 485f - width / 2, y - DOUBT_PERCENT_RATE - 38, width, DOUBT_PERCENT_RATE)
            }
        }

        //endregion

        //region □ Doubt Square Box, Title, Content's Text
        if(dto.result == AvoidDto.Results.저위험) {
            val x = 303f - width / 2
            style = template.resource().styleContentSpecial().clone().color(Color(128,128,128)).fontSize(17f)
            stream.paragraph(x,y - DOUBT_SQUARE_RATE + 22, 80f, AlignHorizontal.CENTER,
                TextBlock(style, "미검출"))
            style = template.resource().styleContentSpecial().clone().color(Color(128,128,128)).fontSize(14f)
            stream.paragraph(x+250, y - DOUBT_SQUARE_RATE+ 30, 80f, AlignHorizontal.CENTER,
                TextBlock(style, "해당없음"))
        }
        else {
            val x = 159f - width / 2
            style = template.resource().styleContentSpecial().clone().color(Color(217, 52, 29)).fontSize(17f)
            stream.paragraph(x, y - DOUBT_SQUARE_RATE + 22, 80f, AlignHorizontal.CENTER,
                TextBlock(style, "검 출"))
            style = template.resource().styleContentRegualar().clone().color(Color(114,113,113)).fontSize(5.3f)
            stream.paragraph(x + 123, y - DOUBT_SQUARE_RATE + 25, 200f, AlignHorizontal.LEFT,
                TextBlock(style,
                    "0                                   50                                 100"))
            if(dto.result == AvoidDto.Results.고위험) {
                stream.paragraph(x + 323, y - DOUBT_SQUARE_RATE + 25, 200f, AlignHorizontal.LEFT,
                    TextBlock(style,
                        "0                                   50                                 100"))
                stream.paragraph(x+80, y-DOUBT_SQUARE_RATE+10, 200f, AlignHorizontal.LEFT,
                    TextBlock(template.resource().styleContentBold().clone().fontSize(9f),
                        "7종 암 중 "+dto.first.name+"일 확률이 "+(dto.first.score!!/10).toInt()*10+"%입니다."))
                stream.paragraph(x+285, y-DOUBT_SQUARE_RATE+10, 200f, AlignHorizontal.LEFT,
                    TextBlock(template.resource().styleContentBold().clone().fontSize(9f),
                        "7종 암 중 "+dto.second.name+"일 확률이 "+(dto.second.score!!/10).toInt()*10+"%입니다."))
            }
            else {
                stream.paragraph(x+68, y- DOUBT_SQUARE_RATE+10, 200f, AlignHorizontal.LEFT,
                    TextBlock(template.resource().styleContentBold().clone().fontSize(9f),
                        "7종 암이 아닌 "+dto.first.name+"일 확률이 "+(dto.first.score!!/10).toInt()*10+"%입니다."))
                style = template.resource().styleContentSpecial().clone().color(Color(128,128,128)).fontSize(12f)
                stream.paragraph(x+325, y - DOUBT_SQUARE_RATE+30, 80f, AlignHorizontal.LEFT,
                    TextBlock(style, "해당없음"))
            }
        }
        //endregion

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val DOUBT_SQUARE_RATE = 87f
        private const val DOUBT_CANCER_RATE = 40f
        private const val DOUBT_PERCENT_RATE = 17f
        private const val DOUBT_RANK_RATE = 20f
        private const val DOUBT_CENTER_LINE_RATE = 59f
    }
}