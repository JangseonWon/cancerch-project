package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.CreateAnalysisResultCommand
import com.idrsys.ailis.cancerch.application.dto.response.AnalysisResultResponse
import com.idrsys.ailis.cancerch.application.usecase.CreateAnalysisResultUseCase
import com.idrsys.ailis.cancerch.domain.model.AnalysisQC
import com.idrsys.ailis.cancerch.domain.model.AnalysisResult
import com.idrsys.ailis.cancerch.domain.repository.AnalysisRepository
import org.springframework.stereotype.Service

/**
 * 분석 결과 생성 Service
 */
@Service
class CreateAnalysisResultService(
    private val analysisRepository: AnalysisRepository
) : CreateAnalysisResultUseCase {

    override suspend fun execute(command: CreateAnalysisResultCommand): AnalysisResultResponse {
        val qc = command.qc?.let {
            AnalysisQC.create(
                sampleId = command.sampleId,
                serviceCode = command.serviceCode,
                batch = command.batch,
                rowNumber = command.rowNumber,
                freemix = it.freemix,
                rawReadsMillions = it.rawReadsMillions,
                dupRate = it.dupRate,
                gc = it.gc,
                totalReads = it.totalReads,
                chrxCnt = it.chrxCnt,
                chryCnt = it.chryCnt,
                predSex = it.predSex
            )
        }

        val analysisResult = AnalysisResult.create(
            sampleId = command.sampleId,
            serviceCode = command.serviceCode,
            batch = command.batch,
            rowNumber = command.rowNumber,
            cadEnsembleProb = command.cadEnsembleProb,
            too5Pred = command.too5Pred,
            too6Pred = command.too6Pred,
            iscore = command.iscore,
            result = command.result,
            comment = command.comment,
            femsCovBc = command.femsCovBc,
            cfdnaConcentration = command.cfdnaConcentration,
            qc = qc
        )

        val saved = analysisRepository.save(analysisResult)
        return AnalysisResultResponse.from(saved)
    }
}
