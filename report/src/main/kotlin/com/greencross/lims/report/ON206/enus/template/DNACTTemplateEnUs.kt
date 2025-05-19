package com.greencross.lims.report.ON206.enus.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON206.enus.resource.DNACTResourceEnUs
import com.greencross.lims.report.ON206.template.DNACTTemplate

abstract class DNACTTemplateEnUs<R: DNACTResourceEnUs>(
    val testInfo: TestInfo
): DNACTTemplate<R> {
}
