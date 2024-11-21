package com.greencross.lims.report.builder

import com.gcgenome.lims.report.Resource
import com.gcgenome.lims.report.Template

interface AbstractReportTemplate<R: Resource>: Template<R> {
}
