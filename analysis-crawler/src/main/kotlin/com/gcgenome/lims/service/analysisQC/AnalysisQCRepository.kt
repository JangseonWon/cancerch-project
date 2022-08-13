package com.gcgenome.lims.service.analysisQC

import com.gcgenome.lims.entity.AnalysisQC
import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import org.springframework.stereotype.Repository

@Repository
interface AnalysisQCRepository: PersistQuerydslR2dbcRepo<AnalysisQC, AnalysisQC.Companion.AnalysisQCPK> {
}