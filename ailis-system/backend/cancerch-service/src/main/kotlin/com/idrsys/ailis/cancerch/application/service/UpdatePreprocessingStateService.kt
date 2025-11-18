package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.PreprocessingAction
import com.idrsys.ailis.cancerch.application.dto.request.UpdatePreprocessingStateCommand
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse
import com.idrsys.ailis.cancerch.application.usecase.UpdatePreprocessingStateUseCase
import com.idrsys.ailis.cancerch.domain.exception.DomainException
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import com.idrsys.ailis.cancerch.domain.sequencing.Preprocessing
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdatePreprocessingStateService(
    private val preprocessingRepository: PreprocessingRepository,
    private val worklistRepository: WorklistRepository
) : UpdatePreprocessingStateUseCase {

    @Transactional
    override suspend fun execute(command: UpdatePreprocessingStateCommand): PreprocessingResponse {
        // 1. 조회
        val worklistId = WorklistId.from(command.worklistId)
        val preprocessing = preprocessingRepository.findByWorklistId(worklistId)
            ?: throw DomainException("Preprocessing not found for worklist ${command.worklistId}")

        // 2. 상태 변경 (startedBy/completedBy 정보와 함께)
        val updated = when (command.action) {
            PreprocessingAction.START_A -> preprocessing.startA(command.startedBy)
            PreprocessingAction.COMPLETE_A -> preprocessing.completeA(command.completedBy)
            PreprocessingAction.HOLD_A -> preprocessing.holdA(command.startedBy)
            PreprocessingAction.START_B -> preprocessing.startB(command.startedBy)
            PreprocessingAction.COMPLETE_B -> preprocessing.completeB(command.completedBy)
            PreprocessingAction.HOLD_B -> preprocessing.holdB(command.startedBy)
        }

        // 3. 저장
        val saved = preprocessingRepository.save(updated)

        // 4. Worklist 정보 조회
        val worklist = worklistRepository.findById(saved.worklistId)
            ?: throw DomainException("Worklist not found for id ${saved.worklistId.value}")

        // 5. DTO 변환
        return PreprocessingResponse.from(saved, worklist)
    }
}
