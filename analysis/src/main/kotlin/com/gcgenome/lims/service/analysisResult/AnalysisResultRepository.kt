package com.gcgenome.lims.service.analysisResult

import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AnalysisResultRepository: PersistQuerydslR2dbcRepo<AnalysisResult, AnalysisResult.Companion.AnalysisResultPK>{
    fun findBySampleAndServiceAndBatchAndRow(sample: Long, service: String, batch: String, row: Int): Mono<AnalysisResult>
}
