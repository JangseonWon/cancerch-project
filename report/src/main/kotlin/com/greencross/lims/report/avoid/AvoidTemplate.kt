package com.greencross.lims.report.avoid

import com.greencross.lims.report.builder.AbstractReportTemplate
import com.greencross.lims.test.avoid.TestInfo

interface AvoidTemplate<R: AvoidResource>: AbstractReportTemplate<R> {
    fun testInfo(): TestInfo
    fun lblIntroHeader(): String
    fun lblIntroContent(): String
    fun lblOverviewTitle(): String
    fun lblOverviewCommon(): String
    fun lblOverviewLowLisk(): String
    fun lblOverviewHighLisk(): String
    fun lblDoubtSquareTitle(): String
    fun lblDoubtContentTitle(): String
    fun lblDangerTitle(): String
    fun lblDangerIntro(): String
    fun lblDangerTMI(): String
    fun lblResultAnalysis(patient: String): String
    fun lblGuideLineDetection(cancer: String): String
    fun lblGuideLineTime(cancer: String): String
    fun lblGuideLineComment(cancer: String): String
    fun lblLBx(): String
    fun lblNGS(): String
    fun lblLimitation(row: Int): String
    fun lblLimitationDescription(row: Int) : String
    fun lblPerformance(): String
    fun lblReferenceLeft(): String
    fun lblReferenceRight(): String
    fun lblReferenceDescription(col: Int) : String
    fun lblReferenceDescriptionBold() : String
    fun lblReferenceSequences(): String { return "Reference Sequence" }
    fun lblReference(): String { return "References" }
}