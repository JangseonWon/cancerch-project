package com.greencross.lims.report.cancerch.kokr

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.cancerch.CancerchTemplate

abstract class CancerchTemplateKoKr<R: CancerchResourceKoKr>(
    testInfo: TestInfo
) : CancerchTemplate<R> {
    val testInfo: TestInfo

    init{
        this.testInfo = testInfo
    }
}
