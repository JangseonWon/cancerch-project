package com.greencross.lims.report.enus

import com.greencross.lims.report.HasSign
import com.gcgenome.lims.report.TextStyle
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color
import java.io.File

interface HasSignEnUs: HasSign {
    companion object {
        val resource = File("/data/lims/resources")

        val MTS: Array<HasSign.Person> = arrayOf(
            HasSign.Person("Da-Som Kim M.T.", "45102", File(resource, "/img/sign/enus/김다솜.png"))
        )

        val MDS: Array<HasSign.Person> = arrayOf(
            HasSign.Person("Eun-hae Cho M.D.", "690", File(resource, "/img/sign/enus/조은해.png")),
            HasSign.Person("Chang-ahn Seol M.D.", "1037", File(resource, "/img/sign/enus/설창안.png")),
        )
    }

    fun fontDefault(): PDFont?
    fun colorText(): Color?
    override fun stylePerson(): TextStyle {
        return TextStyle().fonts(fontDefault()).color(colorText()).fontSize(6f).paragraph(false)
    }

    override fun person(name: String): HasSign.Person? {
        if ("김다솜" == name) return MTS[0]
        if ("조은해" == name) return MDS[0]
        if ("설창안" == name) return MDS[1]
        throw Exception("등록되지 않은 검사,확인자")
    }
}
