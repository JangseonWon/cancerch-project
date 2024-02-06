package com.greencross.lims.report.enus

import com.greencross.lims.report.HasSign
import com.greencross.lims.report.TextStyle
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color
import java.io.File

interface HasSignEnUs: HasSign {
    companion object {
        val resource = File("/data/lims/resources")

        val MTS: Array<HasSign.Person> = arrayOf(
            HasSign.Person("이명근 M.T", "20058", File(resource, "/img/sign/enus/이명근.png")),
            HasSign.Person("방성희 M.T", "33995", File(resource, "/img/sign/enus/방성희.png")),
            HasSign.Person("김민정 M.T", "37668", File(resource, "/img/sign/enus/김민정.png")),
            HasSign.Person("전수옥 M.T", "49141", File(resource, "/img/sign/enus/전수옥.png")),
            HasSign.Person("Dasom Kim MT", "45102", File(resource, "/img/sign/enus/김다솜.png"))
        )

        val MDS: Array<HasSign.Person> = arrayOf(
            HasSign.Person("기창석 M.D", "547", File(resource, "/img/sign/enus/기창석.png")),
            HasSign.Person("Jo Eun-hae MD", "690", File(resource, "/img/sign/enus/조은해.png")),
            HasSign.Person("김동일 M.D", "762", File(resource, "/img/sign/enus/김동일.png")),
            HasSign.Person("송주선 M.D", "997", File(resource, "/img/sign/enus/송주선.png")),
            HasSign.Person("설창안 M.D", "1037", File(resource, "/img/sign/enus/설창안.png")),
            HasSign.Person("이새미 M.D", "1067", File(resource, "/img/sign/enus/이새미.png")),
            HasSign.Person("김영곤 M.D", "1139", File(resource, "/img/sign/enus/김영곤.png"))
        )
    }

    fun fontDefault(): PDFont?
    fun colorText(): Color?
    override fun stylePerson(): TextStyle {
        return TextStyle().fonts(fontDefault()).color(colorText()).fontSize(8f).paragraph(false)
    }

    override fun person(name: String): HasSign.Person? {
        if ("이명근" == name) return MTS[0]
        if ("방성희" == name) return MTS[1]
        if ("김민정" == name) return MTS[2]
        if ("전수옥" == name) return MTS[3]
        if ("김다솜" == name) return MTS[4]
        if ("기창석" == name) return MDS[0]
        if ("조은해" == name) return MDS[1]
        if ("김동일" == name) return MDS[2]
        if ("송주선" == name) return MDS[3]
        if ("설창안" == name) return MDS[4]
        if ("이새미" == name) return MDS[5]
        return if ("김영곤" == name) MDS[6] else null
    }
}