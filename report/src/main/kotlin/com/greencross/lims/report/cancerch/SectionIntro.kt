package com.greencross.lims.report.cancerch

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class SectionIntro(private val y: Float = 685f): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f), "인공지능 액체생검 주요 6종 암 선별검사"))

        img = template.resource().imgIntroContent()
        width = img.width * INTRO_CONTENT_RATE / img.height
        stream.drawImage(img, 298f-width/2, y-CONTENT_TITLE_RATE-29, width, INTRO_CONTENT_RATE)
        stream.paragraph(51f, y-32, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().fontSize(9f),
            "아이캔서치 검사는 약 5,000명의 암 환자 및 건강인에서 특징적으로 나타나는 DNA 패턴을 학습한 인공지능으로 수검자의 DNA\n" +
                "패턴을 분석하여 주요 6종 암의 존재 가능성을 예측합니다. 본 검사의 결과는 암의 진단 혹은 완전한 배제를 의미하지 않습니다."))

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val INTRO_CONTENT_RATE = 43f
    }
}
