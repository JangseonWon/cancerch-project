package com.greencross.lims.report.avoid.kokr

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.avoid.AvoidTemplate

abstract class AvoidTemplateKoKr<R : AvoidResourceKoKr>(
    testInfo: TestInfo
) : AvoidTemplate<R> {
    val testInfo: TestInfo

    init {
        this.testInfo = testInfo
    }
}
