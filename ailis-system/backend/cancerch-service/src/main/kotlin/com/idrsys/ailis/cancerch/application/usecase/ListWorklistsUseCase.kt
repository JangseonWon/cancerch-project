package com.idrsys.ailis.cancerch.application.usecase

import com.idrsys.ailis.cancerch.application.dto.request.ListWorklistsQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedWorklistResponse

/**
 * Worklist 목록 조회 UseCase
 */
interface ListWorklistsUseCase {
    suspend fun execute(query: ListWorklistsQuery): PagedWorklistResponse
}
