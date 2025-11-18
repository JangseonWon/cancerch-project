package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.GetAnalysisResultQuery
import com.idrsys.ailis.cancerch.application.dto.response.AnalysisResultResponse

/**
 * 분석 결과 상세 조회 UseCase
 */
interface GetAnalysisResultUseCase {
    suspend fun execute(query: GetAnalysisResultQuery): AnalysisResultResponse
}
