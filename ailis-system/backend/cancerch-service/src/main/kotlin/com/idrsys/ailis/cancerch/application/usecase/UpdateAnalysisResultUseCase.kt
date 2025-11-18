package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.UpdateAnalysisResultCommand
import com.idrsys.ailis.cancerch.application.dto.response.AnalysisResultResponse

/**
 * 분석 결과 업데이트 UseCase
 */
interface UpdateAnalysisResultUseCase {
    suspend fun execute(command: UpdateAnalysisResultCommand): AnalysisResultResponse
}
