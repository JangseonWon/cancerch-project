package com.greencross.lims.report.avoid

import com.greencross.lims.report.HasSign
import com.gcgenome.lims.report.Template
import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.builder.Util
import com.gcgenome.lims.report.func.AlignVertical
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color

class SectionSign<T: Template<out HasSign>,D: AbstractReportDto>(
    private var y: Float
) : Painter<T, D> {
    private var signs: HashMap<HasSign.Person, PDImageXObject> = HashMap()
    private val SIGN_WIDTH_MAX: Float = 40f
    private val SIGN_HEIGHT_MAX: Float = 15f
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: T,
        dto: D
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        val style: TextStyle = template.resource().stylePerson()//.color(Color(72,71,71)).fonts()
        var widthTotal: Float = 0f
        for(label in template.resource().labels()){
            var block = TextBlock(style, label.label())
            widthTotal += block.width()
            widthTotal += 5;
            for(person in label.persons(template, dto)){
                block = TextBlock(style, person!!.name + "(" + person.license+")")
                if(!signs.containsKey(person)) signs.put(person, PDImageXObject.createFromFileByContent(person.sign, template.resource().doc()))
                widthTotal += block.width() + SIGN_WIDTH_MAX + 10
            }
            widthTotal += 20
        }

        var x: Float = 347.5f - widthTotal/2
        for(label in template.resource().labels()){
            var block = TextBlock(style, label.label())
            stream.paragraph(x, y+SIGN_HEIGHT_MAX/2, 120f, AlignVertical.MIDDLE, block)
            x += block.width()
            x += 5
            for(person in label.persons(template, dto)){
                block = TextBlock(style, person!!.name + "(" + person.license + ")")
                val sign = signs.get(person)
                Util.icon(stream, sign!!, x+block.width() + 2, y+SIGN_HEIGHT_MAX, SIGN_WIDTH_MAX, SIGN_HEIGHT_MAX)
                stream.paragraph(x, y+SIGN_HEIGHT_MAX/2, block.width(), AlignVertical.MIDDLE, block)
                x += block.width() + SIGN_WIDTH_MAX + 10
            }
            x += 20
        }
        stream.setLineWidth(1f)
            .setNonStrokingColor(Color(72,71,71)).moveTo(30f,64f).lineTo(570f,64f).fill()
        stream.restoreGraphicsState()
        return stream
    }
}
