package com.gcgenome.lims.service.analysisResult


import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface AnalysisResultRepository: PersistQuerydslR2dbcRepo<AnalysisResult, AnalysisResult.Companion.AnalysisResultPK>{
    fun findBySampleAndService(sample: Long, service: String): Flux<AnalysisResult>
}