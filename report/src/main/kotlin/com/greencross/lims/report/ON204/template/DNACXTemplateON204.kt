package com.greencross.lims.report.ON204.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON204.resource.DNACXResource

interface DNACXTemplateON204<R: DNACXResource> : DNACXTemplate<R> {
    override fun testInfo(): TestInfo {
        return TestInfo.ON204
    }
}
