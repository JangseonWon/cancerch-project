package com.greencross.lims.report.kokr

import com.greencross.lims.report.HasSign
import com.greencross.lims.report.HasSign.Person
import com.gcgenome.lims.report.TextStyle
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color
import java.io.File

interface HasSignKoKr: HasSign {
    companion object {
        val resource = File("/data/lims/resources")

        val MTS: Array<Person> = arrayOf(
            Person("김다솜 M.T.", "45102", File(resource, "/img/sign/enus/김다솜.png"))
        )

        val MDS: Array<Person> = arrayOf(
            Person("조은해 M.D.", "690", File(resource, "/img/sign/enus/조은해.png")),
            Person("설창안 M.D.", "1037", File(resource, "/img/sign/enus/설창안.png")),
        )
    }

    fun fontDefault(): PDFont?
    fun colorText(): Color?
    override fun stylePerson(): TextStyle {
        return TextStyle().fonts(fontDefault()).color(colorText()).fontSize(8f).paragraph(false)
    }

    override fun person(name: String): Person? {
        if ("김다솜" == name) return MTS[0]
        if ("조은해" == name) return MDS[0]
        if ("설창안" == name) return MDS[1]
        throw Exception("등록되지 않은 검사,확인자")
    }
}
