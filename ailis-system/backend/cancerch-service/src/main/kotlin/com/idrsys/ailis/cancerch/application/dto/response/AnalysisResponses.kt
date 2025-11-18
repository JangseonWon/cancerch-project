package com.idrsys.ailis.cancerch.application.dto.response

import com.idrsys.ailis.cancerch.domain.model.AnalysisResult
import java.time.Instant

/**
 * 분석 결과 응답
 */
data class AnalysisResultResponse(
    val id: Long? = null,  // 단순 숫자 ID (프론트엔드 호환용)
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val cadEnsembleProb: Double?,
    val too5Pred: String?,
    val too6Pred: String?,
    val iscore: Double?,
    val result: String?,
    val comment: String?,
    val femsCovBc: Double?,
    val cfdnaConcentration: Double?,
    val qc: AnalysisQCResponse?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int
) {
    companion object {
        fun from(analysisResult: AnalysisResult, numericId: Long? = null): AnalysisResultResponse {
            return AnalysisResultResponse(
                id = numericId,
                sampleId = analysisResult.id.sampleId,
                serviceCode = analysisResult.id.serviceCode,
                batch = analysisResult.id.batch,
                rowNumber = analysisResult.id.rowNumber,
                cadEnsembleProb = analysisResult.cadEnsembleProb,
                too5Pred = analysisResult.too5Pred,
                too6Pred = analysisResult.too6Pred,
                iscore = analysisResult.iscore,
                result = analysisResult.result,
                comment = analysisResult.comment,
                femsCovBc = analysisResult.femsCovBc,
                cfdnaConcentration = analysisResult.cfdnaConcentration,
                qc = analysisResult.qc?.let { AnalysisQCResponse.from(it) },
                createdAt = analysisResult.createdAt,
                updatedAt = analysisResult.updatedAt,
                version = analysisResult.version
            )
        }
    }
}

/**
 * QC 정보 응답
 */
data class AnalysisQCResponse(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val freemix: Double?,
    val rawReadsMillions: Double?,
    val dupRate: Double?,
    val gc: Double?,
    val totalReads: Long?,
    val chrxCnt: Long?,
    val chryCnt: Long?,
    val predSex: String?,
    val isPassed: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int
) {
    companion object {
        fun from(qc: com.idrsys.ailis.cancerch.domain.model.AnalysisQC): AnalysisQCResponse {
            return AnalysisQCResponse(
                sampleId = qc.id.sampleId,
                serviceCode = qc.id.serviceCode,
                batch = qc.id.batch,
                rowNumber = qc.id.rowNumber,
                freemix = qc.freemix,
                rawReadsMillions = qc.rawReadsMillions,
                dupRate = qc.dupRate,
                gc = qc.gc,
                totalReads = qc.totalReads,
                chrxCnt = qc.chrxCnt,
                chryCnt = qc.chryCnt,
                predSex = qc.predSex,
                isPassed = qc.isPassed(),
                createdAt = qc.createdAt,
                updatedAt = qc.updatedAt,
                version = qc.version
            )
        }
    }
}

/**
 * 분석 결과 목록 응답 (페이징)
 */
data class PagedAnalysisResultResponse(
    val results: List<AnalysisResultResponse>,
    val totalCount: Int,
    val page: Int,
    val size: Int,
    val totalPages: Int
) {
    companion object {
        fun from(
            results: List<AnalysisResult>,
            totalCount: Int,
            page: Int,
            size: Int
        ): PagedAnalysisResultResponse {
            val totalPages = if (size > 0) (totalCount + size - 1) / size else 0
            return PagedAnalysisResultResponse(
                results = results.map { AnalysisResultResponse.from(it) },
                totalCount = totalCount,
                page = page,
                size = size,
                totalPages = totalPages
            )
        }
    }
}
