package com.greencross.lims.report.avoid

import com.greencross.lims.report.builder.AbstractReportTemplate
import com.greencross.lims.test.avoid.TestInfo

interface AvoidTemplate<R: AvoidResource>: AbstractReportTemplate<R> {
    fun testInfo(): TestInfo
    fun lblReferenceSequences(): String { return "Reference Sequence" }
    fun lblReference(): String { return "References" }
}