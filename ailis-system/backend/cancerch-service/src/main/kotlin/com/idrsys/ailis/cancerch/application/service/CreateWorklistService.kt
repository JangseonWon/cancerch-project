package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.CreateWorklistCommand
import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse
import com.idrsys.ailis.cancerch.application.usecase.CreateWorklistUseCase
import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import com.idrsys.ailis.cancerch.domain.service.WorklistDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateWorklistService(
    private val worklistRepository: WorklistRepository,
    private val worklistDomainService: WorklistDomainService
) : CreateWorklistUseCase {

    @Transactional
    override suspend fun execute(command: CreateWorklistCommand): WorklistResponse {
        // 1. 도메인 모델 생성
        val worklist = Worklist.create(
            name = command.name,
            batchPrefix = command.batchPrefix,
            batchIndex = command.batchIndex,
            createdBy = command.createdBy
        )

        // 2. 도메인 검증
        val validation = worklistDomainService.validate(worklist)
        validation.throwIfInvalid()

        // 3. 저장
        val saved = worklistRepository.save(worklist)

        // 4. DTO 변환
        return WorklistResponse.from(saved)
    }
}
