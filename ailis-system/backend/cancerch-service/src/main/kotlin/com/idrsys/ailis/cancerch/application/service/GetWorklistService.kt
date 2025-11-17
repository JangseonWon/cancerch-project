package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.GetWorklistQuery
import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse
import com.idrsys.ailis.cancerch.application.usecase.GetWorklistUseCase
import com.idrsys.ailis.cancerch.domain.exception.EntityNotFoundException
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetWorklistService(
    private val worklistRepository: WorklistRepository
) : GetWorklistUseCase {

    @Transactional(readOnly = true)
    override suspend fun execute(query: GetWorklistQuery): WorklistResponse {
        val worklist = worklistRepository.findById(WorklistId(query.id))
            ?: throw EntityNotFoundException("Worklist", query.id)

        return WorklistResponse.from(worklist)
    }
}
