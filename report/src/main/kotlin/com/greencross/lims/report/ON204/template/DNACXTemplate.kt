package com.greencross.lims.report.ON204.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.builder.AbstractReportTemplate

interface DNACXTemplate<R: DNACXResource>: AbstractReportTemplate<R> {
    fun testInfo(): TestInfo
}
