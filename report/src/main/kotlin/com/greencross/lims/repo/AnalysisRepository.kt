package com.greencross.lims.repo

import com.greencross.lims.entity.Analysis
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface AnalysisRepository : QuerydslR2dbcRepository<Analysis, Analysis.Companion.AnalysisPK> {

}