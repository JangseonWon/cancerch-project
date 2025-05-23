package com.gcgenome.lims.service.analysisRS

import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface AnalysisRSRepository: PersistQuerydslR2dbcRepo<AnalysisResult, AnalysisResult.Companion.AnalysisResultPK> {
    fun findBySampleAndBatchAndRow(sample: Long, batch: String, row: Int): Flux<AnalysisResult>
}
