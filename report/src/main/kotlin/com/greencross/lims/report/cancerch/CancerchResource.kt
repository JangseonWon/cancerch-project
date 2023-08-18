package com.greencross.lims.report.cancerch

import com.greencross.lims.report.HasSign
import com.greencross.lims.report.TextStyle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File
import java.io.IOException

interface CancerchResource  : HasSign {
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
    fun imgHeaderBox(): PDImageXObject
    fun imgContentTitle(): PDImageXObject
    fun imgIntroContent(): PDImageXObject
    fun imgTotalResultContent(): PDImageXObject
    fun imgTotalResultLowRisk(): PDImageXObject
    fun imgTotalResultMiddleRisk(): PDImageXObject
    fun imgTotalResultHighRisk():PDImageXObject
    fun imgDoubtSquare(): PDImageXObject
    fun imgDoubtContentBox(): PDImageXObject
    fun imgDoubtCenterLine(): PDImageXObject
    fun imgDoubtCancerPercentages(score: Double): PDImageXObject
    fun imgDoubtCancer(name: String): PDImageXObject
    fun imgRankFirst(): PDImageXObject
    fun imgRankSecond(): PDImageXObject
    fun imgCancerTypeTitle(tf: Boolean): PDImageXObject
    fun imgCancerTypeContent(): PDImageXObject
    fun imgCancerTypeImage(cancer: String): PDImageXObject
    fun imgCancerReadingGuide(): PDImageXObject
    fun imgCancerTypeDetect(): PDImageXObject
    fun imgCancerTypeDetectArrow(): PDImageXObject
    fun imgUnderBar(): PDImageXObject
    fun imgHuman(): PDImageXObject
    fun imgEsop(first: String): PDImageXObject
    fun imgLung(first: String): PDImageXObject
    fun imgLiver(first: String): PDImageXObject
    fun imgPanc(first: String): PDImageXObject
    fun imgColon(first: String): PDImageXObject
    fun imgBreast(first: String): PDImageXObject
    fun imgOvary(first: String): PDImageXObject
    fun imgLine(cancer: String): PDImageXObject
    fun imgBarNormal(): PDImageXObject
    fun imgBarGray(): PDImageXObject
    fun imgBarMiddle(): PDImageXObject
    fun imgBarDanger(): PDImageXObject
    fun imgDetailResultOverview(): PDImageXObject
    fun imgDetailResultRisk(risk : String) : PDImageXObject
    fun imgDetailResultTable(result: String): PDImageXObject
    fun imgSmallSquareAverage() : PDImageXObject
    fun imgSmallSquarePatient() : PDImageXObject
    fun imgAnalysisContentBox() : PDImageXObject
    fun imgBackgroundCancer(cancer: String) : PDImageXObject
    fun imgGuideLineTable(risk: String) : PDImageXObject
    fun imgGuideLineCancer(cancer: String) : PDImageXObject
    fun imgGuideLineTotalCancer() : PDImageXObject
    fun imgProcess() : PDImageXObject
    fun imgAiBox() : PDImageXObject
    fun imgAi() : PDImageXObject
    fun imgLBx() : PDImageXObject
    fun imgLBxBox() : PDImageXObject
    fun imgNGS() : PDImageXObject
    fun imgMiniSquare() : PDImageXObject
    fun imgLimitationTableTitle() : PDImageXObject
    fun imgLimitationTable1() : PDImageXObject
    fun imgLimitationTable2() : PDImageXObject
    fun imgReferenceVertical() : PDImageXObject
    fun imgReferenceHorizontal(): PDImageXObject
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