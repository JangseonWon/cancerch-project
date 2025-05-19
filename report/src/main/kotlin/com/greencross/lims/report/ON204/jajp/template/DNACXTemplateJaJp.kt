package com.greencross.lims.report.ON204.jajp.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON204.jajp.resource.DNACXResourceJaJp
import com.greencross.lims.report.ON204.template.DNACXTemplate

abstract class DNACXTemplateJaJp<R: DNACXResourceJaJp>(
    testInfo: TestInfo
): DNACXTemplate<R> {
    val testInfo: TestInfo

    init {
        this.testInfo = testInfo
    }
}
