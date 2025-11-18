package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.SearchAnalysisQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedAnalysisResultResponse
import com.idrsys.ailis.cancerch.application.usecase.SearchAnalysisUseCase
import com.idrsys.ailis.cancerch.domain.repository.AnalysisRepository
import org.springframework.stereotype.Service

/**
 * 분석 결과 검색 Service
 */
@Service
class SearchAnalysisService(
    private val analysisRepository: AnalysisRepository
) : SearchAnalysisUseCase {

    override suspend fun execute(query: SearchAnalysisQuery): PagedAnalysisResultResponse {
        // search 파라미터가 있으면 sampleId 또는 serviceCode로 검색
        val sampleIdParam = query.sampleId ?: query.search
        val serviceCodeParam = query.serviceCode ?: query.search

        val allResults = analysisRepository.search(
            sampleId = sampleIdParam,
            serviceCode = if (query.sampleId == null && query.search != null) serviceCodeParam else query.serviceCode,
            batch = query.batch,
            result = query.result
        )

        val totalCount = allResults.size
        val startIndex = query.page * query.size
        val endIndex = minOf(startIndex + query.size, totalCount)

        val pagedResults = if (startIndex < totalCount) {
            allResults.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        return PagedAnalysisResultResponse.from(
            results = pagedResults,
            totalCount = totalCount,
            page = query.page,
            size = query.size
        )
    }
}
