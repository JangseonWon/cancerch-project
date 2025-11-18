package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.SearchAnalysisQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedAnalysisResultResponse

/**
 * 분석 결과 검색 UseCase
 */
interface SearchAnalysisUseCase {
    suspend fun execute(query: SearchAnalysisQuery): PagedAnalysisResultResponse
}
