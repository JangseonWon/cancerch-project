package com.gcgenome.lims.service.analysis

import com.gcgenome.lims.data.Analysis
import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.lims.search.PageReactive
import com.gcgenome.lims.search.SearchParam
import com.gcgenome.lims.service.analysisResult.AnalysisResultDao
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AnalysisHandler(
    private val analysisDao:        AnalysisDao,
    private val mapper:             AnalysisMapper,
    private val analysisResultDao:  AnalysisResultDao
) {
    fun search(query: SearchParam): Mono<PageReactive<Analysis>> = analysisDao.search(query).map { it.map(mapper::toDto) }
    fun updateComment(sample: Long, service: String, batch: String, row: Int, comment: String): Flux<AnalysisResult>{
        return analysisResultDao.findBySampleAndService(sample, service, batch, row)
            .map { it.apply { this.comment = comment }}
            .flatMap(analysisResultDao::update)
    }
    fun updateResult(dto: Analysis): Flux<AnalysisResult>{
        return analysisResultDao.findBySampleAndService(dto.sample, dto.service, dto.batch, dto.row)
            .map { it.apply {
                this.result     = dto.result
                this.too5Pred   = dto.too5Pred
                this.too6Pred   = dto.too6Pred
            }}
            .flatMap(analysisResultDao::update)
    }
}