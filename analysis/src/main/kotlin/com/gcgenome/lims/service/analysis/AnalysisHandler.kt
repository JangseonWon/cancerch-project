package com.gcgenome.lims.service.analysis

import com.gcgenome.lims.data.Analysis
import com.gcgenome.lims.data.LinkRequest
import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.lims.search.PageReactive
import com.gcgenome.lims.search.SearchParam
import com.gcgenome.lims.service.analysisQC.AnalysisQCDao
import com.gcgenome.lims.service.analysisResult.AnalysisResultDao
import com.gcgenome.lims.service.request.RequestDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class AnalysisHandler(
    private val analysisDao:        AnalysisDao,
    private val mapper:             AnalysisMapper,
    private val analysisResultDao:  AnalysisResultDao,
    private val analysisQCDao:      AnalysisQCDao,
    private val requestDao:         RequestDao
) {
    fun search(query: SearchParam): Mono<PageReactive<Analysis>> = analysisDao.search(query).map { it.map(mapper::toDto) }
    @Transactional
    fun linkData(request: LinkRequest): Mono<Boolean> {
        return analysisQCDao.findByCompositeKey(request.originSample, request.originService, request.batch, request.row)
            .map { mapper.createAnalysisQCEntity(it, request.linkSample?:0L, request.linkService?:"") }
            .flatMap(analysisQCDao::insert)
            .then(analysisResultDao.findByCompositeKey(request.originSample, request.originService, request.batch, request.row))
            .map { mapper.createAnalysisResultEntity(it, request.linkSample?:0L, request.linkService?:"") }
            .flatMap(analysisResultDao::insertAndUpdate)
            .mapNotNull { true }
    }
    fun chkAnalysis(sample: Long, service: String, batch: String, row: Int): Mono<Boolean> {
        return requestDao.checkExistRequest(sample, service)
            .mapNotNull { analysisQCDao.isExistQCData(sample, service, batch, row) }
            .mapNotNull { analysisResultDao.checkExistResultData(sample, service, batch, row) }
            .flatMap { it }
    }
    fun chkRequest(sample: Long, service: String): Mono<Boolean> {
        return requestDao.checkExistRequest(sample, service)
    }

    fun updateComment(sample: Long, service: String, batch: String, row: Int, comment: String): Mono<AnalysisResult>{
        return analysisResultDao.findByCompositeKey(sample, service, batch, row)
            .map { it.apply { this.comment = comment }}
            .flatMap(analysisResultDao::insertAndUpdate)
    }
    fun updateResult(dto: Analysis): Mono<AnalysisResult>{
        println(dto)
        return analysisResultDao.findByCompositeKey(dto.sample, dto.service, dto.batch, dto.row)
            .map { it.apply {
                this.result     = dto.result
                this.too5Pred   = dto.too5Pred
                this.too6Pred   = dto.too6Pred
                this.cfDnaConcentration = dto.cfDnaConcentration
                this.femsBc = dto.femsBc
                this.covBc = dto.covBc
                this.femsCovBc = dto.femsCovBc
                this.femsCovBernn = dto.femsCovBernn
                this.iscore = dto.iscore
            }}
            .flatMap(analysisResultDao::insertAndUpdate)
    }
}
