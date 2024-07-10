package com.gcgenome.lims.service.analysisQC

import com.gcgenome.lims.entity.AnalysisQC
import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AnalysisQCRepository: PersistQuerydslR2dbcRepo<AnalysisQC, AnalysisQC.Companion.AnalysisQCPK> {
    fun findBySampleAndServiceAndBatchAndRow(sample: Long, service: String, batch: String, row: Int): Mono<AnalysisQC>
}
