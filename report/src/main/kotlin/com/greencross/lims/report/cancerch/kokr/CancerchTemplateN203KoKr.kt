package com.greencross.lims.report.cancerch.kokr

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.cancerch.CancerchTemplateN203
import java.time.format.DateTimeFormatter

class CancerchTemplateN203KoKr(
    resource: CancerchResourceN203KoKr,
    testInfo: TestInfo,
) : CancerchTemplateKoKr<CancerchResourceN203KoKr>(testInfo),
    CancerchTemplateN203<CancerchResourceN203KoKr> {
    private val resource: CancerchResourceN203KoKr = resource
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun resource(): CancerchResourceN203KoKr {
        return resource
    }
}
