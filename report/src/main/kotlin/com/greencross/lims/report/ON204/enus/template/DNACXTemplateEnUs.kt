package com.greencross.lims.report.ON204.enus.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON204.enus.resource.DNACXResourceEnUs
import com.greencross.lims.report.ON204.template.DNACXTemplate

abstract class DNACXTemplateEnUs<R: DNACXResourceEnUs>(
    val testInfo: TestInfo
): DNACXTemplate<R> {
}
