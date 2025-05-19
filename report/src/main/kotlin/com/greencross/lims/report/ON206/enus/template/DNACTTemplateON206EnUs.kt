package com.greencross.lims.report.ON206.enus.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON206.enus.resource.DNACTResourceON206EnUs
import com.greencross.lims.report.ON206.template.DNACTTemplateON206

class DNACTTemplateON206EnUs(
    resource: DNACTResourceON206EnUs,
    testInfo: TestInfo
) : DNACTTemplateEnUs<DNACTResourceON206EnUs>(testInfo),
    DNACTTemplateON206<DNACTResourceON206EnUs> {
    private val resource: DNACTResourceON206EnUs = resource

    override fun resource(): DNACTResourceON206EnUs {
        return resource
    }
}
