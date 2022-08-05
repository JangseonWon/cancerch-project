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
        stream.drawImage(img, 140f - width/2, y-DOUBT_SQUARE_RATE, width, DOUBT_SQUARE_RATE)
        stream.paragraph(220f - width/2,y - DOUBT_SQUARE_RATE + 63, 300f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDoubtSquareTitle()))

        img = template.resource().imgDoubtContentBox()
        width = img.width * DOUBT_SQUARE_RATE / img.height
        style = template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(12f)
        stream.drawImage(img, 390f - width/2, y-DOUBT_SQUARE_RATE, width, DOUBT_SQUARE_RATE )
        stream.paragraph(552f - width/2, y- DOUBT_SQUARE_RATE+74, 300f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDoubtContentTitle()))
        //endregion

        //region □ Doubt Square Box, Title, Content's Text
        val colors = when(dto!!.result){
            AvoidDto.Results.GENERAL     -> Color(141, 197, 86)
            AvoidDto.Results.CONCERN  -> Color(239, 167, 24)
            else                        -> Color(217,  52, 29)
        }
        val horizontal = when(dto.result){
            AvoidDto.Results.RISK    -> AlignHorizontal.LEFT
            else                        -> AlignHorizontal.CENTER
        }
        var x = 303f - width / 2
        style = template.resource().styleContentSpecial().clone().color(colors).fontSize(17f)
        stream.paragraph(x,y - DOUBT_SQUARE_RATE + 22, 80f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDoubtSquareContent(dto.result)))
        x = when(dto.result){
            AvoidDto.Results.GENERAL     -> 423f - width / 2
            AvoidDto.Results.CONCERN  -> 423f - width / 2
            else                        -> 373f - width / 2
        }
        val ys = when(dto.result){
            AvoidDto.Results.GENERAL    -> y - DOUBT_SQUARE_RATE + 35
            else                       -> y - DOUBT_SQUARE_RATE + 45
        }
        style = template.resource().styleContentSpecial().clone().color(Color(67, 72, 142)).fontSize(9f)
        stream.paragraph(x+130, ys, 300f, horizontal,
            TextBlock(style, template.lblDoubtContentLarge(dto.result, dto.first.name)))
        style = template.resource().styleContentRegualar().clone().color(Color(0, 0, 0)).fontSize(6f)
        stream.paragraph(x+130, ys-17, 300f, horizontal,
            TextBlock(style, template.lblDoubtContentSmall(dto.result)))

        if(dto.result == AvoidDto.Results.RISK) {
            img = template.resource().imgDoubtCancer(dto.first.name)
            width = img.width * DOUBT_CANCER_RATE / img.height
            stream.drawImage(img, 285f - width/2f, y- DOUBT_CANCER_RATE-22, width, DOUBT_CANCER_RATE)
        }

        //endregion

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val DOUBT_SQUARE_RATE = 87f
        private const val DOUBT_CANCER_RATE = 66f
        private const val DOUBT_ICON_RATE = 22f
    }
}