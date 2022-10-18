package com.greencross.lims.service.report

import com.greencross.lims.entity.Report
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository

interface ReportRepository : QuerydslR2dbcRepository<Report, Report.Companion.ReportPK>{
}