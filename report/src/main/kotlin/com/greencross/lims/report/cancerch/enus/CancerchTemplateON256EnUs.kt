package com.greencross.lims.report.cancerch.enus

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.cancerch.*
import java.time.format.DateTimeFormatter

class CancerchTemplateON256EnUs(
    resource: CancerchResourceON256EnUs,
    testInfo: TestInfo
) : CancerchTemplateEnUs<CancerchResourceON256EnUs>(testInfo),
    CancerchTemplateON256<CancerchResourceON256EnUs> {
    private val resource: CancerchResourceON256EnUs = resource
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun resource(): CancerchResourceON256EnUs {
        return resource
    }
}
