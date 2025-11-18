package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.CreateSequencingCommand
import com.idrsys.ailis.cancerch.application.dto.response.SequencingResponse

/**
 * Sequencing 생성 UseCase
 */
interface CreateSequencingUseCase {
    suspend fun execute(command: CreateSequencingCommand): SequencingResponse
}
