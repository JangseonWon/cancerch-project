package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter

import com.greencross.lims.report.func.AlignHorizontal.CENTER


class SectionTitle (private val y: Float = 745f) : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: AvoidTemplate<AvoidResource>,
        dto: AvoidDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()

        //region □ Header 좌측 로고, 아래 설명
        var img = template.resource().imgTitle()
        var width = img.width * TITLE_HEIGHT / img.height
        stream.drawImage(img, 127f - width/2, y, width, TITLE_HEIGHT)

        var style = template.resource().styleTitle()
        val block = TextBlock(style, "[Pan-cancer : 주요 암]")
        stream.paragraph(127f, y-img.height/10, 200f, CENTER, block)
        //endregion

        //region □ HeaderBox, 내부 내용
        img = template.resource().imgHeaderBox()
        width = img.width * TITLE_HEADERBOX_HEIGHT / img.height
        stream.drawImage(img, 400f - width/2, y-30, width, TITLE_HEADERBOX_HEIGHT)

        style = template.resource().styleHeaderTitle()
        
        //endregion





        stream.restoreGraphicsState()
        return stream.cursorY(y - TITLE_HEIGHT)
    }

    companion object {
        private const val TITLE_HEIGHT = 45f
        private const val TITLE_HEADERBOX_HEIGHT = 80f
    }
}
