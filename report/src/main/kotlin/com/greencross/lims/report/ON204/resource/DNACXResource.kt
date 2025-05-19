package com.greencross.lims.report.ON204.resource

import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.HasSign
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File
import java.io.IOException

interface DNACXResource: HasSign {
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
    fun fontSpecial(): PDFont
    fun styleContentRegualar() : TextStyle
    fun styleContentBold(): TextStyle
    fun styleContentSpecial(): TextStyle
    fun imgTitle(): PDImageXObject
    fun imgHuman(): PDImageXObject
    fun imgHumanBox(): PDImageXObject
    fun imgSummaryOfResults(): PDImageXObject
    fun imgCangerTypeIcon(cancer: String, type: Boolean): PDImageXObject
    fun imgCancerTypeImage(cancer: String): PDImageXObject
    fun imgCancerBox(): PDImageXObject
    fun lblCancerName(cancer: String): String
    fun lblInterpretationBold(dto: DNACXDto): String
    fun lblInterpretationRegular(dto: DNACXDto): String
    fun imgSignalScoreBackground(): PDImageXObject
    fun imgShortSmallTitle(): PDImageXObject
    fun imgLongSmallTitle(): PDImageXObject
    fun imgCovScoreBackgorund(): PDImageXObject
    fun imgFemsScoreBackgorund(): PDImageXObject
    fun imgGenomicInstabilityScoreBackgorund(): PDImageXObject
    fun imgGenomeCI(): PDImageXObject = img(File(resource, "img/GenomeCI.png"))
    fun imgLympotecCI(): PDImageXObject = img(File(resource, "img/LymphotecCI.png"))
    @Throws(IOException::class)
    fun initialize() {

    }

    companion object {
        @JvmStatic
        var resource = File("/data/lims/resources")
    }
}
