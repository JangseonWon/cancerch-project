package com.greencross.lims.report.cancerch

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.builder.AbstractReportTemplate
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible

interface CancerchTemplate<R: CancerchResource>: AbstractReportTemplate<R> {
    fun testInfo(): TestInfo

    fun lblTitleSmallLogo(): String

    fun lblMedicalInstitution(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblMedicalRecordNumber(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblRequestNumber(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblPatientName(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblAgeSex(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblSpecimenType(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblSpecimenDate(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblReceiptReportDate(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)

    fun lblIntroHeader(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblIntroContent(stream: PDPageContentStreamPageAccessible, y: Float)

    fun lblOverviewTitle(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblDoubtSquareTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float)
    fun lblDoubtSquareContent(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto)
    fun lblOverviewResultImg(stream: PDPageContentStreamPageAccessible, y: Float, TITLE_RATE: Float, dto: CancerchDto)
    fun lblOverviewResult(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, TITLE_RATE: Float, dto: CancerchDto)

    fun lblDoubtContentTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float)
    fun lblDoubtContentLarge(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto)
    fun lblDoubtContentSmall(stream: PDPageContentStreamPageAccessible, y: Float, width: Float, SQUARE_RATE: Float, dto: CancerchDto)

    fun lblDetailResultAnalysisTableHeaderTop(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblDetailResultAnalysisTableContentTop(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblDetailResultAnalysisTableHeaderBot(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblDetailResultAnalysisTableContentBot(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)

    fun lblResultToWord(result: CancerchDto.Results): String

    fun lblDangerTitle(stream: PDPageContentStreamPageAccessible, y: Float, width: Float)
    fun lblDangerIntro(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblDangerCancerName(stream: PDPageContentStreamPageAccessible, x: Float, y: Float, cancer: String)
    fun lblDangerGraphGuide(stream: PDPageContentStreamPageAccessible, x: Float, y: Float)
    fun lblDangerTMI(stream: PDPageContentStreamPageAccessible, width: Float, y: Float, dto: CancerchDto)

    fun lblDetailResultAnalysisGeneral(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblDetailResultAnalysisConcern(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblDetailResultAnalysisCancer(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblDetailResultAnalysisOthers(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)

    fun lblGuideLineHeader(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblGuideLineTop(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblGuideLineTableHedaer0(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto)
    fun lblGuideLineTableHeader1(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float)
    fun lblGuideLineTableHeader2(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto)

    fun lblGuideLineNormalHeader(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblGuideLineNormalCancer(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblGuideLineNormalTarget(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblGuideLineNormalFrequency(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblGuideLineNormalTest(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblGuideLineCaption(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblGuideLineDetection(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float)
    fun lblGuideLineTime(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto)
    fun lblGuideLineComment(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto)
    fun lblGuideLineConcern(stream: PDPageContentStreamPageAccessible, y: Float)

    fun lblLimitationHeader(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblLimitationTableHeader(stream: PDPageContentStreamPageAccessible, y: Float, col: Int)
    fun lblLimitationTableTotalCancer(col: Int): String
    fun lblLimitationTableLungCancer(col: Int): String
    fun lblLimitationTableColorCancer(col: Int): String
    fun lblLimitationTableLiverCancer(col: Int): String
    fun lblLimitationTablePanCancer(col: Int): String
    fun lblLimitationTableEsopCancer(col: Int): String
    fun lblLimitationTableOverCancer(col: Int): String
    fun lblLimitationTable2Header(col: Int): String
    fun lblLimitationTable2Row1(col: Int): String
    fun lblLimitationTable2Row2(col: Int): String
    fun lblLimitationTable2Row3(col: Int): String
    fun lblLimitationTable2Row4(col: Int): String
    fun lblLimitationTable2Row5(col: Int): String
    fun lblLimitationTable2Row6(col: Int): String
    fun lblLimitationTable2Row7(col: Int): String

    fun lblLimitation(stream: PDPageContentStreamPageAccessible, y: Float, row: Int)
    fun lblLimitationDescription(stream: PDPageContentStreamPageAccessible, y: Float, row: Int)
    fun lblPerformance(): String
    fun lblPerformanceBasic(): String
    fun lblReferenceTitle() : String
    fun lblReferenceLeft(): String
    fun lblReferenceRight(): String
    fun lblReferenceDescription(stream: PDPageContentStreamPageAccessible, y: Float)
    fun lblReferenceSequences(): String { return "Reference Sequence" }
    fun lblReference(): String { return "References" }
    fun lblPatientInfo(age: String, sex: Sex) : String
    fun lblPatientInfoWithCancer(age: String, sex: Sex, cancer: String) : String
    fun lblPatientSir(name: String) : String
    fun lblCancerForOthers(): String
    fun lblDetailResultAnalysisContentOther(patient: String?): String
    fun lblHighRisk(): String
    fun lblMiddleRisk(): String
    fun lblNormalRisk(): String
}