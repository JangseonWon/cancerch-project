package com.greencross.lims.report.cancerch.enus

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.cancerch.CancerchTemplate

abstract class CancerchTemplateEnUs<R: CancerchResourceEnUs>(
    testInfo: TestInfo
) : CancerchTemplate<R> {
    val testInfo: TestInfo

    init{
        this.testInfo = testInfo
    }

}
