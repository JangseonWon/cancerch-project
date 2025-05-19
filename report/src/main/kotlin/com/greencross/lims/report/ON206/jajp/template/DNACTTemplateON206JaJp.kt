package com.greencross.lims.report.ON206.jajp.template

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.ON206.jajp.resource.DNACTResourceON206JaJp
import com.greencross.lims.report.ON206.template.DNACTTemplateON206

class DNACTTemplateON206JaJp(
    resource: DNACTResourceON206JaJp,
    testInfo: TestInfo
) : DNACTTemplateJaJp<DNACTResourceON206JaJp>(testInfo),
DNACTTemplateON206<DNACTResourceON206JaJp> {
    private val resource: DNACTResourceON206JaJp = resource

    override fun resource(): DNACTResourceON206JaJp {
        return resource
    }
}
