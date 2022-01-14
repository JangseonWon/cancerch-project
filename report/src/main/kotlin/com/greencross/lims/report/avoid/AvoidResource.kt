package com.greencross.lims.report.avoid

import com.greencross.lims.report.HasSign
import com.greencross.lims.report.TextStyle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File
import java.io.IOException

interface AvoidResource : HasSign {
    fun colorPrimary(): Color
    fun colorPrimaryStroke(): Color
    fun colorGray():    Color { return Color.decode("0xEFEFEF") }
    fun colorDarkGray():Color { return Color.decode("0xB5B5B5") }
    fun colorText():    Color { return Color.decode("0x484848") }

    fun fontDefault(): PDFont
    fun fontTitle(): PDFont
    fun fontHeader(): PDFont
    fun fontText(): PDFont
    fun fontScientific(): PDFont
    fun fontHeaderBoxTitle() : PDFont

    fun imgTitle(): PDImageXObject
    fun imgHeaderBox(): PDImageXObject
    fun imgContentTitle(): PDImageXObject
    fun imgIntroContent(): PDImageXObject
    fun imgTotalResultContent(): PDImageXObject
    fun imgDoubtSquare(): PDImageXObject
    fun imgDoubtContentBox(): PDImageXObject
    fun imgDoubtCenterLine(): PDImageXObject
    fun imgCancerTypeContent(): PDImageXObject
//    fun imgHeaderLogo(): PDImageXObject

    fun styleTitle() : TextStyle
    fun styleHeaderTitle(): TextStyle

    @Throws(IOException::class)
    fun initialize() {
    }

    companion object {
        @JvmStatic
        var resource = File("/data/lims/resources")
    }
}