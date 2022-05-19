package com.greencross.lims.report.avoid

import com.greencross.lims.report.HasSign
import com.greencross.lims.report.TextStyle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File
import java.io.IOException
import java.util.function.Supplier

interface AvoidResource : HasSign {
    //region # Basic Func
    fun colorPrimary(): Color
    fun colorPrimaryStroke(): Color
    fun colorGray():    Color { return Color.decode("0xEFEFEF") }
    fun colorDarkGray():Color { return Color.decode("0xB5B5B5") }
    fun colorText():    Color { return Color.decode("0x484848") }

    fun fontDefault():          PDFont
    fun fontTitle():            PDFont
    fun fontHeader():           PDFont
    fun fontText():             PDFont
    fun fontScientific():       PDFont
    fun fontHeaderBoxTitle() :  PDFont
    //endregion

    //region Custom Func
    fun fontContentRegular():   PDFont
    fun fontContentBold():      PDFont
    fun fontSpecial():          PDFont

    fun imgTitle(): PDImageXObject
//    fun img(): Supplier<PDImageXObject> {
//        return Supplier<PDImageXObject> { img(File(AvoidResource.resource, "img/avoid/SectionTitle/0_title.png")) }
//    }
    fun imgHeaderBox(): PDImageXObject
    fun imgContentTitle(): PDImageXObject
    fun imgIntroContent(): PDImageXObject
    fun imgTotalResultContent(): PDImageXObject
    fun imgTotalResultLowRisk(): PDImageXObject
    fun imgTotalResultHighRisk():PDImageXObject
    fun imgDoubtSquare(): PDImageXObject
    fun imgDoubtContentBox(): PDImageXObject
    fun imgDoubtCenterLine(): PDImageXObject
    fun imgDoubtCancerPercentages(score: Double): PDImageXObject
    fun imgDoubtCancer(name: String): PDImageXObject
    fun imgRankFirst(): PDImageXObject
    fun imgRankSecond(): PDImageXObject
    fun imgCancerTypeTitle(): PDImageXObject
    fun imgCancerTypeContent(): PDImageXObject
    fun imgCancerReadingGuide(): PDImageXObject
    fun imgHuman(): PDImageXObject
    fun imgEsop(first: String, second: String): PDImageXObject
    fun imgLung(first: String, second: String): PDImageXObject
    fun imgLiver(first: String, second: String): PDImageXObject
    fun imgPanc(first: String, second: String): PDImageXObject
    fun imgColon(first: String, second: String): PDImageXObject
    fun imgBreast(first: String, second: String): PDImageXObject
    fun imgOvary(first: String, second: String): PDImageXObject
    fun imgLine(cancer: String): PDImageXObject
    fun imgBarNormal(): PDImageXObject
    fun imgBarGray(): PDImageXObject
    fun imgBarDanger(): PDImageXObject
    fun imgDetailResultOverview(): PDImageXObject
    fun imgDetailResultTable() : PDImageXObject
    fun imgDetailResultRisk(risk : String) : PDImageXObject
    fun imgSmallSquareAverage() : PDImageXObject
    fun imgSmallSquarePatient() : PDImageXObject
    fun imgAnalysisContentBox() : PDImageXObject
    fun imgBackgroundCancer(cancer: String) : PDImageXObject
    fun imgGuideLineTable() : PDImageXObject
    fun imgGuideLineCancer(cancer: String) : PDImageXObject
    fun imgGuideLineTotalCancer() : PDImageXObject
    fun imgProcess() : PDImageXObject
    fun imgAiBox() : PDImageXObject
    fun imgAi() : PDImageXObject
    fun imgLBx() : PDImageXObject
    fun imgLBxBox() : PDImageXObject
    fun imgNGS() : PDImageXObject
    fun styleContentRegualar() : TextStyle
    fun styleContentBold(): TextStyle
    fun styleContentSpecial(): TextStyle
    //endregion
    @Throws(IOException::class)
    fun initialize() {

    }

    companion object {
        @JvmStatic
        var resource = File("/data/lims/resources")
    }
}