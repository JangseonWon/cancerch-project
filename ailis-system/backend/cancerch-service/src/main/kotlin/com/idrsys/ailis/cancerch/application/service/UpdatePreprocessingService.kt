package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.UpdatePreprocessingCommand
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse
import com.idrsys.ailis.cancerch.application.usecase.UpdatePreprocessingUseCase
import com.idrsys.ailis.cancerch.domain.exception.DomainException
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdatePreprocessingService(
    private val preprocessingRepository: PreprocessingRepository
) : UpdatePreprocessingUseCase {

    @Transactional
    override suspend fun execute(command: UpdatePreprocessingCommand): PreprocessingResponse {
        val worklistId = WorklistId.from(command.worklistId)
        val preprocessing = preprocessingRepository.findByWorklistId(worklistId)
            ?: throw DomainException("Preprocessing not found for worklist ${command.worklistId}")

        // 업데이트할 필드들 적용
        val updated = preprocessing.copy(
            index = command.index ?: preprocessing.index,
            sequencingBatch = command.sequencingBatch ?: preprocessing.sequencingBatch
        )

        val saved = preprocessingRepository.save(updated)
        return PreprocessingResponse.from(saved)
    }
}
