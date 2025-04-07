package com.greencross.lims.report.kokr

import com.greencross.lims.report.HasSign
import com.gcgenome.lims.report.Template
import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.builder.Util
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import com.gcgenome.lims.report.func.AlignVertical.MIDDLE

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
        val style: TextStyle = template.resource().stylePerson()
        var widthTotal: Float = 0f
        for(label in template.resource().labels()){
            var block = TextBlock(style, label.label())
            widthTotal += block.width()
            widthTotal += 5;
            for(person in label.persons(template, dto)){
                block = TextBlock(style, person!!.name + "(" + person.license+")")
                if(!signs.containsKey(person)) signs.put(person, PDImageXObject.createFromFileByContent(person.sign, template.resource().doc()))
                widthTotal += block.width() + SIGN_WIDTH_MAX +10
            }
            widthTotal += 20
        }

        stream!!.saveGraphicsState()
        var x: Float = 297.5f - widthTotal/2
        for(label in template.resource().labels()){
            var block = TextBlock(style, label.label())
            stream.paragraph(x, y+SIGN_HEIGHT_MAX/2, 120f, MIDDLE, block)
            x += block.width()
            x += 5
            for(person in label.persons(template, dto)){
                block = TextBlock(style, person!!.name + "(" + person.license + ")")
                val sign = signs.get(person)
                Util.icon(stream, sign!!, x+block.width() + 2, y+SIGN_HEIGHT_MAX, SIGN_WIDTH_MAX, SIGN_HEIGHT_MAX)
                stream.paragraph(x, y+SIGN_HEIGHT_MAX/2, block.width(), MIDDLE, block)
                x += block.width() + SIGN_WIDTH_MAX + 10
            }
            x += 20
        }
        stream.restoreGraphicsState()
        return stream
    }
}
