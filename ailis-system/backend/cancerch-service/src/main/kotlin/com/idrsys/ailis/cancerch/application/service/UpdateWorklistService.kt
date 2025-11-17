package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.UpdateWorklistCommand
import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse
import com.idrsys.ailis.cancerch.application.usecase.UpdateWorklistUseCase
import com.idrsys.ailis.cancerch.domain.exception.EntityNotFoundException
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateWorklistService(
    private val worklistRepository: WorklistRepository
) : UpdateWorklistUseCase {

    @Transactional
    override suspend fun execute(command: UpdateWorklistCommand): WorklistResponse {
        val worklist = worklistRepository.findById(WorklistId(command.id))
            ?: throw EntityNotFoundException("Worklist", command.id)

        val updated = worklist.copy(
            name = command.name,
            updatedBy = command.updatedBy,
            version = worklist.version + 1
        )

        val saved = worklistRepository.save(updated)

        return WorklistResponse.from(saved)
    }
}
