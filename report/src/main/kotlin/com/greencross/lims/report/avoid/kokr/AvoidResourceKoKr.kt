package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.HasSign
import com.greencross.lims.report.HasSign.Person
import com.greencross.lims.report.HasSign.SignLabel
import com.greencross.lims.report.Template
import com.greencross.lims.report.avoid.AvoidResource
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.kokr.HasHeaderKoKr
import com.greencross.lims.report.kokr.HasSignKoKr
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color
import java.io.File

abstract class AvoidResourceKoKr(doc: PDDocument): AvoidResource, HasSignKoKr, HasHeaderKoKr {
    var doc: PDDocument = PDDocument()
    val fontHeader: PDFont
    val fontTitle: PDFont
    val fontValue: PDFont
    val fontText: PDFont
    val fontScientific: PDFont

    init {
        this.doc = doc
        initialize()
        fontHeader =     font(File(AvoidResource.resource, "font/SDGothicNeoRound06.ttf"))
        fontTitle =      font(File(AvoidResource.resource, "/font/GC140.ttf"))
        fontValue =      font(File(AvoidResource.resource, "font/SdGothicNeoRound04.ttf"))
        fontText =       font(File(AvoidResource.resource, "font/NanumBarunGothic.ttf"))
        fontScientific = font(File(AvoidResource.resource, "font/OpenSans-Regular.ttf"))
    }

    override fun colorGray(): Color { return Color.decode("0xEFEFEF") }

    override fun fontHeaderValue(): PDFont { return fontValue }

    override fun colorText(): Color {
        return super.colorText()
    }

    override fun labels(): Array<SignLabel> {
        return arrayOf(
            object : SignLabel {
                override fun label(): String { return "검사자:" }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<Person?> {
                    return arrayOf(person("이명근"))
                }
            },
            object : SignLabel {
                override fun label(): String { return "확인자:" }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<Person?> {
                    return arrayOf(person("김영곤"), person("설창안"))
                }
            }
        )
    }
}