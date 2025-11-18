package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.CreatePreprocessingCommand
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse
import com.idrsys.ailis.cancerch.application.usecase.CreatePreprocessingUseCase
import com.idrsys.ailis.cancerch.domain.exception.DomainException
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.sequencing.Preprocessing
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreatePreprocessingService(
    private val preprocessingRepository: PreprocessingRepository
) : CreatePreprocessingUseCase {

    @Transactional
    override suspend fun execute(command: CreatePreprocessingCommand): PreprocessingResponse {
        // 1. 중복 체크
        val worklistId = WorklistId.from(command.worklistId)
        if (preprocessingRepository.existsByWorklistId(worklistId)) {
            throw DomainException("Preprocessing already exists for worklist ${command.worklistId}")
        }

        // 2. 도메인 모델 생성
        val preprocessing = Preprocessing.create(
            worklistId = worklistId,
            index = command.index,
            sequencingBatch = command.sequencingBatch
        )

        // 3. 저장
        val saved = preprocessingRepository.save(preprocessing)

        // 4. DTO 변환
        return PreprocessingResponse.from(saved)
    }
}
