package com.greencross.lims.report.ON206.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.builder.AbstractReportTemplate

interface DNACTTemplate<R: DNACTResource>: AbstractReportTemplate<R> {
    fun testInfo(): TestInfo
}
