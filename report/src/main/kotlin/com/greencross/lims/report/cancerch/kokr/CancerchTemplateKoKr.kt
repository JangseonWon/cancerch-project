package com.greencross.lims.report.cancerch.kokr

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

abstract class CancerchTemplateKoKr<R: CancerchResourceKoKr>(
    testInfo: TestInfo
) : CancerchTemplate<R> {
    val testInfo: TestInfo

    init{
        this.testInfo = testInfo
    }
}
