package com.greencross.lims.report.avoid

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.builder.AbstractReportTemplate
import com.greencross.lims.report.builder.Sex

interface AvoidTemplate<R: AvoidResource>: AbstractReportTemplate<R> {
    fun testInfo(): TestInfo
    fun lblTitleSmallLogo(): String
    fun lblIntroHeader(): String
    fun lblIntroContent(): String

    fun lblOverviewTitle(): String
    fun lblOverviewCommon(): String
    fun lblOverviewBridgeWord(): String
    fun lblOverviewLowLisk(): String

    fun lblOverviewRiskBridge(): String
    fun lblOverViewRisk(): String
    fun lblOverViewMiddleRisk(): String
    fun lblOverViewHighRisk(): String
    fun lblOverViewHighRiskEnd(): String
    fun lblOverviewMidHighEnd(): String

    fun lblDoubtSquareTitle(): String
    fun lblDoubtSquareContent(result: AvoidDto.Results): String
    fun lblDoubtContentTitle(): String
    fun lblDoubtContentLarge(result: AvoidDto.Results, name: String): String
    fun lblDoubtContentSmall(result: AvoidDto.Results): String

    fun lblDangerTitle(): String
    fun lblDangerIntroStart(): String
    fun lblDangerIntroBridge(): String
    fun lblDangerIntroEnd(): String
    fun lblDangerCancerName(code: Int): String
    fun lblDangerGraphGuide(): String
    fun lblDangerTMI(): String

    fun lblDetailResultAnalysisHeader(name: String): String
    fun lblDetailResultAnalysis(patient: String): String
    fun lblDetailResultAnalysisTableHeaderTop(): String
    fun lblDetailResultAnalysisTableHedaerBot(): String
    fun lblDetailResultAnalysisTableNone1(): String
    fun lblDetailResultAnalysisTableNone2(): String
    fun lblDetailResultAnalysisTableMidRisk1(): String
    fun lblDetailResultAnalysisTableMidRisk2(): String
    fun lblDetailResultAnlaysisTableContentCom(): String
    fun lblDetailResultAnalysisTableContentMID(): String
    fun lblDetailResultAnalysisTableContentHIG(): String
    fun lblDetailResultAnalysisContentLine1(): String
    fun lblDetailResultAnalysisContentLine2NRM(): String
    fun lblDetailResultAnalysisContentLine2MID(): String
    fun lblDetailResultAnalysisContentLine2HIG(): String
    fun lblDetailResultAnalysisContentLine2END(): String
    fun lblDetailResultAnalysisContentLine3NRM(): String
    fun lblDetailResultAnalysisContentLine3MID_1(): String
    fun lblDetailResultAnalysisContentLine3MID_2(): String
    fun lblDetailResultAnalysisContentLine3MID_3(): String
    fun lblDetailResultAnalysisContentLine3MID_4(): String
    fun lblDetailResultAnalysisContentLine3HIG_1(): String
    fun lblDetailResultAnalysisContentLine3HIG_2(): String
    fun lblDetailResultAnalysisContentLine3HIG_3(): String
    fun lblDetailResultAnalysisContentLine3HIG_4(): String
    fun lblDetailResultAnalysisContentLine3ASR(asr:String): String
    fun lblDetailResultAnalysisContentLine3HIG_5(): String
    fun lblDetailResultAnalysisContentLine3HIG_6(): String
    fun lblDetailResultAnalysisContentLine3HIG_7(): String
    fun lblDetailResultAnalysisContentLine3HIG_8(): String
    fun lblDetailResultAnalysisContentLine3HIG_9(): String
    fun lblDetailResultAnalysisContentLine3HIG_10(): String
    fun lblDetailResultAnalysisContentLine3HIG_11(): String
    fun lblDetailResultAnalysisContentLine3HIG_12(): String
    fun lblDetailResultAnalysisContentLine3MIDHIG(): String

    fun lblGuideLineHeader(type: AvoidDto.Results, name: String): String
    fun lblGuideLineTop(type: AvoidDto.Results, cancer: String): String
    fun lblGuideLineTableHeader1(): String
    fun lblGuideLineTableHeader2(): String
    fun lblGuideLineNormalHeader(code: Int): String
    fun lblGuideLineNormalCancer(code: Int): String
    fun lblGuideLineNormalTarget(code: Float): String
    fun lblGuideLineNormalFrequency(code: Int): String
    fun lblGuideLineNormalTest(code: Float): String
    fun lblGuideLineCaption(): String
    fun lblGuideLineDetection(cancer: String): String
    fun lblGuideLineTime(cancer: String): String
    fun lblGuideLineComment(cancer: String): String
    fun lblDetailProcessHeader(): String
    fun lblDetailProcessTitle(): String
    fun lblDetailProcessContentCode(code: Int): String
    fun lblLBx(): String
    fun lblNGS(): String

    fun lblLimitationHeader(): String
    fun lblLimitationTableHeader(col: Int): String
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

    fun lblLimitation(row: Int): String
    fun lblLimitationDescription(row: Int) : String
    fun lblPerformance(): String
    fun lblReferenceTitle() : String
    fun lblReferenceLeft(): String
    fun lblReferenceRight(): String
    fun lblReferenceDescription(col: Int) : String
    fun lblReferenceDescriptionBold() : String
    fun lblReferenceSequences(): String { return "Reference Sequence" }
    fun lblReference(): String { return "References" }
    fun lblResultToWord(result: AvoidDto.Results): String
    fun lblPatientInfo(age: String, sex: Sex) : String
    fun lblPatientSir(name: String) : String
}