package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.GetWorklistQuery
import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse

/**
 * Worklist 조회 UseCase
 */
interface GetWorklistUseCase {
    suspend fun execute(query: GetWorklistQuery): WorklistResponse
}
