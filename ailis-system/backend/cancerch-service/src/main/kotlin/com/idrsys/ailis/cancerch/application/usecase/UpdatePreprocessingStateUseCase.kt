package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.UpdatePreprocessingStateCommand
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse

/**
 * Preprocessing 상태 업데이트 UseCase
 */
interface UpdatePreprocessingStateUseCase {
    suspend fun execute(command: UpdatePreprocessingStateCommand): PreprocessingResponse
}
