package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.GetAnalysisResultQuery
import com.idrsys.ailis.cancerch.application.dto.response.AnalysisResultResponse
import com.idrsys.ailis.cancerch.application.usecase.GetAnalysisResultUseCase
import com.idrsys.ailis.cancerch.domain.exception.DomainException
import com.idrsys.ailis.cancerch.domain.model.AnalysisId
import com.idrsys.ailis.cancerch.domain.repository.AnalysisRepository
import org.springframework.stereotype.Service

/**
 * 분석 결과 상세 조회 Service
 */
@Service
class GetAnalysisResultService(
    private val analysisRepository: AnalysisRepository
) : GetAnalysisResultUseCase {

    override suspend fun execute(query: GetAnalysisResultQuery): AnalysisResultResponse {
        val id = AnalysisId.from(
            query.sampleId,
            query.serviceCode,
            query.batch,
            query.rowNumber
        )

        val analysisResult = analysisRepository.findById(id)
            ?: throw DomainException.NotFound("AnalysisResult not found: $id")

        return AnalysisResultResponse.from(analysisResult)
    }
}
