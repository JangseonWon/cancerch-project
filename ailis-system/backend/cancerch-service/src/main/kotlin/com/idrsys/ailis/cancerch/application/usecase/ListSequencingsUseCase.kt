package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.ListSequencingsQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedSequencingResponse

/**
 * Sequencing 목록 조회 UseCase
 */
interface ListSequencingsUseCase {
    suspend fun execute(query: ListSequencingsQuery): PagedSequencingResponse
}
