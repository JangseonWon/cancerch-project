package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.UpdateWorklistCommand
import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse

/**
 * Worklist 업데이트 UseCase
 */
interface UpdateWorklistUseCase {
    suspend fun execute(command: UpdateWorklistCommand): WorklistResponse
}
