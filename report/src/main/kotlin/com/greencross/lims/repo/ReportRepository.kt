package com.greencross.lims.repo

import com.greencross.lims.entity.Report
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository

interface ReportRepository : QuerydslR2dbcRepository<Report, Report.Companion.ReportPK>{
}