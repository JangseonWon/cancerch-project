package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.GetPreprocessingQuery
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse

/**
 * Preprocessing 조회 UseCase
 */
interface GetPreprocessingUseCase {
    suspend fun execute(query: GetPreprocessingQuery): PreprocessingResponse
}
