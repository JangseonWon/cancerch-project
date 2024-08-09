package com.greencross.lims.report.cancerch.kokr

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.cancerch.CancerchTemplateN256
import java.time.format.DateTimeFormatter

class CancerchTemplateN256KoKr(
    resource: CancerchResourceN256KoKr,
    testInfo: TestInfo,
) : CancerchTemplateKoKr<CancerchResourceN256KoKr>(testInfo),
    CancerchTemplateN256<CancerchResourceN256KoKr> {
    private val resource: CancerchResourceN256KoKr = resource
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun resource(): CancerchResourceN256KoKr {
        return resource
    }
}
