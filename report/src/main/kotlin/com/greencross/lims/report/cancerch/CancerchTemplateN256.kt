package com.greencross.lims.report.cancerch

import com.gcgenome.lims.avoid.TestInfo

interface CancerchTemplateN256<R: CancerchResource> : CancerchTemplate<R> {
    override fun testInfo(): TestInfo {
        return TestInfo.N256
    }
}
