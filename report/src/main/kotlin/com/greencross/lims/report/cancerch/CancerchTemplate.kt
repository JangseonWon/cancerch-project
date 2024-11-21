package com.greencross.lims.report.cancerch

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.builder.AbstractReportTemplate

interface CancerchTemplate<R: CancerchResource>: AbstractReportTemplate<R> {
    fun testInfo(): TestInfo
}
