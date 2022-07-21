package com.greencross.lims.report.avoid

import com.gcgenome.lims.avoid.TestInfo


interface AvoidTemplateN201<R: AvoidResource> : AvoidTemplate<R> {
    override fun testInfo(): TestInfo {
        return TestInfo.N201
    }
}