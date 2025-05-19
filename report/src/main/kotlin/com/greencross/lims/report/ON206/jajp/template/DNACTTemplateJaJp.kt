package com.greencross.lims.report.ON206.jajp.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON206.jajp.resource.DNACTResourceJaJp
import com.greencross.lims.report.ON206.template.DNACTTemplate

abstract class DNACTTemplateJaJp<R: DNACTResourceJaJp>(val testInfo: TestInfo): DNACTTemplate<R>
