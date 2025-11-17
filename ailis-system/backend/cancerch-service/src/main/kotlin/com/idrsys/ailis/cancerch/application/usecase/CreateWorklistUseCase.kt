package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.CreateWorklistCommand
import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse

/**
 * Worklist 생성 UseCase
 */
interface CreateWorklistUseCase {
    suspend fun execute(command: CreateWorklistCommand): WorklistResponse
}
