package com.greencross.lims.report.ON206.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionDisclaimers(private val y: Float = 708f): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACTTemplate<DNACTResource>?,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var style = template!!.resource().styleContentRegualar().clone().color(Color(0, 0, 0)).fontSize(12f)
        stream.paragraph(35f, y - 14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(Color(0,80,109)).fontSize(15f), "検査限界"))
        stream.circle(36f, y - 34f, 1.5f).fill()
        stream.paragraph(42f, y - 38f, 700f, AlignHorizontal.LEFT, TextBlock(style, "このテストは確診検査に代わるものではありません。"))
        stream.circle(36f, y - 57f, 1.5f).fill()
        stream.paragraph(42f, y - 61f, 700f, AlignHorizontal.LEFT, TextBlock(style, "このテストは、従来の検診・検査に代わるものではありません。"))
        stream.circle(36f, y - 81f, 1.5f).fill()
        stream.paragraph(42f, y - 84f, 700f, AlignHorizontal.LEFT, TextBlock(style, "テストの性能は、がんのステージや種類によって異なる場合があります。"))
        stream.circle(36f, y - 103f, 1.5f).fill()
        stream.paragraph(42f, y - 107f, 700f, AlignHorizontal.LEFT, TextBlock(style, "感度はがんの位置や遺伝的特性によって異なる場合があります。"))
        stream.circle(36f, y - 126f, 1.5f).fill()
        stream.paragraph(42f, y - 130f, 700f, AlignHorizontal.LEFT, TextBlock(style, "cfDNA濃度ががん治療などで低い場合、分析の感度が低下する可能性があります。"))
        stream.circle(36f, y - 149f, 1.5f).fill()
        stream.paragraph(42f, y - 153f, 700f, AlignHorizontal.LEFT, TextBlock(style, "良性疾患または自己免疫疾患の患者では、がん関連パターンを正確に検出することが難しい場合\nがあります。"))
        stream.restoreGraphicsState()

        style = template.resource().styleContentRegualar().clone().color(Color(87, 90, 88)).fontSize(9f)
        stream.rect(57f, y - 232f, 480f, 45f).setStrokingColor(Color(242, 242, 242))
            .setNonStrokingColor(Color(242, 242, 242)).fillAndStroke()
        stream.paragraph(70f, y - 245f, 100f, AlignHorizontal.LEFT, TextBlock(style, "*\n\n*"))
        stream.paragraph(
            75f, y - 245f, 495f, AlignHorizontal.LEFT, TextBlock(
                style,
                "このテストは、結果の臨床的意義を確立しておらず、この結果に基づく治療関連の行動の有用性についての証拠\n" +
                        "はまだ不十分です。\n"
            ), TextBlock(style, "このテストはGC Genome社が開発し(Laboratory-developed Test，LDT）、十分な性能評価を行いました。")
        )
        return stream

    }
}
