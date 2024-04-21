package com.greencross.lims.report.cancerch.enus

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.cancerch.*
import java.time.format.DateTimeFormatter

class CancerchTemplateON203EnUs(
    resource: CancerchResourceON203EnUs,
    testInfo: TestInfo
) : CancerchTemplateEnUs<CancerchResourceON203EnUs>(testInfo),
    CancerchTemplateON203<CancerchResourceON203EnUs> {
    private val resource: CancerchResourceON203EnUs = resource
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun resource(): CancerchResourceON203EnUs {
        return resource
    }
}
