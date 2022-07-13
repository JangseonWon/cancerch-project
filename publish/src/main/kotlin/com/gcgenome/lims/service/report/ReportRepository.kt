package com.gcgenome.lims.service.report

import com.gcgenome.lims.entity.Report
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: QuerydslR2dbcRepository<Report, Report.Companion.ReportPK>