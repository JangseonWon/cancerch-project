package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.avoid.AvoidTemplateN201
import com.greencross.lims.test.avoid.TestInfo

class AvoidTemplateN201KoKr(
    resource: AvoidResourceN201KoKr,
    testInfo: TestInfo
) : AvoidTemplateKoKr<AvoidResourceN201KoKr>(testInfo),
    AvoidTemplateN201<AvoidResourceN201KoKr> {
    private val resource: AvoidResourceN201KoKr = resource
    override fun resource(): AvoidResourceN201KoKr {
        return resource
    }
}