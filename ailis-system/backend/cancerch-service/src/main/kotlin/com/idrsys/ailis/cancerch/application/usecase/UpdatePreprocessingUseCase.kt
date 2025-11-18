package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.UpdatePreprocessingCommand
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse

/**
 * Preprocessing 업데이트 UseCase
 */
interface UpdatePreprocessingUseCase {
    suspend fun execute(command: UpdatePreprocessingCommand): PreprocessingResponse
}
