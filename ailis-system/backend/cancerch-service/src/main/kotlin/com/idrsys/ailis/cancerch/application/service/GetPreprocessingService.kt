package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.GetPreprocessingQuery
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse
import com.idrsys.ailis.cancerch.application.usecase.GetPreprocessingUseCase
import com.idrsys.ailis.cancerch.domain.exception.DomainException
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetPreprocessingService(
    private val preprocessingRepository: PreprocessingRepository,
    private val worklistRepository: WorklistRepository
) : GetPreprocessingUseCase {

    @Transactional(readOnly = true)
    override suspend fun execute(query: GetPreprocessingQuery): PreprocessingResponse {
        val worklistId = WorklistId.from(query.worklistId)
        val preprocessing = preprocessingRepository.findByWorklistId(worklistId)
            ?: throw DomainException("Preprocessing not found for worklist ${query.worklistId}")

        // Worklist 정보 조회
        val worklist = worklistRepository.findById(preprocessing.worklistId)
            ?: throw DomainException("Worklist not found for id ${preprocessing.worklistId.value}")

        return PreprocessingResponse.from(preprocessing, worklist)
    }
}
