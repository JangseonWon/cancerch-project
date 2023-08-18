package com.greencross.lims.report.cancerch

import com.gcgenome.lims.avoid.TestInfo

interface CancerchTemplateN203<R: CancerchResource> : CancerchTemplate<R> {
    override fun testInfo(): TestInfo {
        return TestInfo.N203
    }
}