package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.ListPreprocessingsQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedPreprocessingResponse

/**
 * Preprocessing 목록 조회 UseCase
 */
interface ListPreprocessingsUseCase {
    suspend fun execute(query: ListPreprocessingsQuery): PagedPreprocessingResponse
}
