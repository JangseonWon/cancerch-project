package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.CreateAnalysisResultCommand
import com.idrsys.ailis.cancerch.application.dto.response.AnalysisResultResponse

/**
 * 분석 결과 생성 UseCase
 */
interface CreateAnalysisResultUseCase {
    suspend fun execute(command: CreateAnalysisResultCommand): AnalysisResultResponse
}
