package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.CreatePreprocessingCommand
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse

/**
 * Preprocessing 생성 UseCase
 */
interface CreatePreprocessingUseCase {
    suspend fun execute(command: CreatePreprocessingCommand): PreprocessingResponse
}
