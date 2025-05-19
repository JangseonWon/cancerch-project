package com.greencross.lims.report.ON206.resource

import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.HasSign
import com.greencross.lims.report.ON206.DNACTDto
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File

interface DNACTResource: HasSign {
    fun colorPrimary(): Color
    fun colorPrimaryStroke(): Color
    fun colorGray(): Color { return Color.decode("0xEFEFEF") }
    fun colorDarkGray(): Color { return Color.decode("0xB5B5B5") }
    fun colorText(): Color { return Color.decode("0x484848") }
    fun fontDefault(): PDFont
    fun fontTitle(): PDFont
    fun fontHeader(): PDFont
    fun fontText(): PDFont
    fun fontScientific(): PDFont
    fun fontContentRegular(): PDFont
    fun fontContentBold(): PDFont
    fun fontContentBoldItalic(): PDFont
    fun styleContentRegualar() : TextStyle
    fun styleContentBold(): TextStyle
    fun styleContentBoldIt(): TextStyle
    fun imgTitle(): PDImageXObject
    fun imgSummaryOfResultsTable(): PDImageXObject
    fun lblInterpretatioon(dto: DNACTDto): String
    fun imgGenomicInstabilityExample(): PDImageXObject
    fun imgFEMSExample(): PDImageXObject
    fun imgGenomicInstability(path: String): PDImageXObject
    fun imgFEMS(path: String): PDImageXObject
    fun imgTrackingResultsTable(): PDImageXObject
    fun imgTrackingGraph(type: String): PDImageXObject

    companion object {
        @JvmStatic
        var resource = File("/data/lims/resources")
    }
}
