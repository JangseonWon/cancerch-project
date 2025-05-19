package com.greencross.lims.report.ON204.jajp.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON204.jajp.resource.DNACXResourceON204JaJp
import com.greencross.lims.report.ON204.template.DNACXTemplateON204

class DNACXTemplateON204JaJp(
    resource: DNACXResourceON204JaJp,
    testInfo: TestInfo
) : DNACXTemplateJaJp<DNACXResourceON204JaJp>(testInfo),
    DNACXTemplateON204<DNACXResourceON204JaJp> {
    private val resource: DNACXResourceON204JaJp = resource

    override fun resource(): DNACXResourceON204JaJp {
        return resource
    }

}

