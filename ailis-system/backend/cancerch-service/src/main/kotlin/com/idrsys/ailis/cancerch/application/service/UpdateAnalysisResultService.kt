package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.UpdateAnalysisResultCommand
import com.idrsys.ailis.cancerch.application.dto.response.AnalysisResultResponse
import com.idrsys.ailis.cancerch.application.usecase.UpdateAnalysisResultUseCase
import com.idrsys.ailis.cancerch.domain.exception.DomainException
import com.idrsys.ailis.cancerch.domain.model.AnalysisId
import com.idrsys.ailis.cancerch.domain.repository.AnalysisRepository
import org.springframework.stereotype.Service

/**
 * 분석 결과 업데이트 Service
 */
@Service
class UpdateAnalysisResultService(
    private val analysisRepository: AnalysisRepository
) : UpdateAnalysisResultUseCase {

    override suspend fun execute(command: UpdateAnalysisResultCommand): AnalysisResultResponse {
        val id = AnalysisId.from(
            command.sampleId,
            command.serviceCode,
            command.batch,
            command.rowNumber
        )

        val existing = analysisRepository.findById(id)
            ?: throw DomainException.NotFound("AnalysisResult not found: $id")

        val updated = existing.updateResult(
            cadEnsembleProb = command.cadEnsembleProb,
            too5Pred = command.too5Pred,
            too6Pred = command.too6Pred,
            iscore = command.iscore,
            result = command.result,
            comment = command.comment,
            femsCovBc = command.femsCovBc,
            cfdnaConcentration = command.cfdnaConcentration,
            status = command.status ?: existing.status,
            analyzedAt = command.analyzedAt ?: existing.analyzedAt,
            analyzedBy = command.analyzedBy ?: existing.analyzedBy
        )

        val saved = analysisRepository.save(updated)
        return AnalysisResultResponse.from(saved)
    }
}
