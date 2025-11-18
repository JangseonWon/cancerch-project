package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.GetSequencingQuery
import com.idrsys.ailis.cancerch.application.dto.response.SequencingResponse

/**
 * Sequencing 조회 UseCase
 */
interface GetSequencingUseCase {
    suspend fun execute(query: GetSequencingQuery): SequencingResponse
}
