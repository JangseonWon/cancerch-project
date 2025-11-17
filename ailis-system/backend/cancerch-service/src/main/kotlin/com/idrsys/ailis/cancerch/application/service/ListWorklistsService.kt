package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.ListWorklistsQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedWorklistResponse
import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse
import com.idrsys.ailis.cancerch.application.usecase.ListWorklistsUseCase
import com.idrsys.ailis.cancerch.domain.model.WorklistStatus
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListWorklistsService(
    private val worklistRepository: WorklistRepository
) : ListWorklistsUseCase {

    @Transactional(readOnly = true)
    override suspend fun execute(query: ListWorklistsQuery): PagedWorklistResponse {
        val worklists = if (query.status != null) {
            val status = WorklistStatus.valueOf(query.status)
            worklistRepository.findByStatus(status, query.size)
        } else {
            worklistRepository.findAll(query.offset, query.size)
        }

        val total = worklistRepository.count()
        val totalPages = ((total + query.size - 1) / query.size).toInt()

        return PagedWorklistResponse(
            content = worklists.map { WorklistResponse.from(it) },
            page = query.page,
            size = query.size,
            totalElements = total,
            totalPages = totalPages
        )
    }
}
