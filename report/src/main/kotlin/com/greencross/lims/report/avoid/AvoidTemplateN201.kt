package com.greencross.lims.report.avoid

import com.greencross.lims.test.avoid.TestInfo


interface AvoidTemplateN201<R: AvoidResource> : AvoidTemplate<R> {
    override fun testInfo(): TestInfo {
        return TestInfo.N201
    }

}