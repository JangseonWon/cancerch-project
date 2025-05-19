package com.greencross.lims.report.ON206.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON206.resource.DNACTResource

interface DNACTTemplateON206<R: DNACTResource> : DNACTTemplate<R> {
    override fun testInfo(): TestInfo {
        return TestInfo.ON206
    }
}
