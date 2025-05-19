package com.greencross.lims.report.ON204.enus.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON204.enus.resource.DNACXResourceON204EnUs
import com.greencross.lims.report.ON204.template.DNACXTemplateON204

class DNACXTemplateON204EnUs(
    resource: DNACXResourceON204EnUs,
    testInfo: TestInfo
) : DNACXTemplateEnUs<DNACXResourceON204EnUs>(testInfo),
    DNACXTemplateON204<DNACXResourceON204EnUs> {
    private val resource: DNACXResourceON204EnUs = resource

    override fun resource(): DNACXResourceON204EnUs {
        return resource
    }

}

