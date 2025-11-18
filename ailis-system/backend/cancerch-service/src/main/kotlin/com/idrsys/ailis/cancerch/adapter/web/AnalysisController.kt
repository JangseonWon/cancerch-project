package com.idrsys.ailis.cancerch.adapter.web

import com.idrsys.ailis.cancerch.application.dto.request.CreateAnalysisResultCommand
import com.idrsys.ailis.cancerch.application.dto.request.GetAnalysisResultQuery
import com.idrsys.ailis.cancerch.application.dto.request.SearchAnalysisQuery
import com.idrsys.ailis.cancerch.application.dto.request.UpdateAnalysisQCCommand
import com.idrsys.ailis.cancerch.application.dto.request.UpdateAnalysisResultCommand
import com.idrsys.ailis.cancerch.application.dto.response.AnalysisResultResponse
import com.idrsys.ailis.cancerch.application.dto.response.PagedAnalysisResultResponse
import com.idrsys.ailis.cancerch.application.usecase.CreateAnalysisResultUseCase
import com.idrsys.ailis.cancerch.application.usecase.GetAnalysisResultUseCase
import com.idrsys.ailis.cancerch.application.usecase.SearchAnalysisUseCase
import com.idrsys.ailis.cancerch.application.usecase.UpdateAnalysisResultUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Analysis REST API Controller
 */
@RestController
@RequestMapping("/api/analysis")
class AnalysisController(
    private val createAnalysisResultUseCase: CreateAnalysisResultUseCase,
    private val updateAnalysisResultUseCase: UpdateAnalysisResultUseCase,
    private val searchAnalysisUseCase: SearchAnalysisUseCase,
    private val getAnalysisResultUseCase: GetAnalysisResultUseCase
) {

    /**
     * 분석 결과 검색
     * GET /api/analysis/search
     */
    @GetMapping("/search")
    suspend fun searchAnalysis(
        @RequestParam(required = false) sampleId: String?,
        @RequestParam(required = false) serviceCode: String?,
        @RequestParam(required = false) batch: String?,
        @RequestParam(required = false) result: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): PagedAnalysisResultResponse {
        val query = SearchAnalysisQuery(
            sampleId = sampleId,
            serviceCode = serviceCode,
            batch = batch,
            result = result,
            page = page,
            size = size
        )
        return searchAnalysisUseCase.execute(query)
    }

    /**
     * 분석 결과 상세 조회
     * GET /api/analysis/{sampleId}/{serviceCode}/{batch}/{rowNumber}
     */
    @GetMapping("/{sampleId}/{serviceCode}/{batch}/{rowNumber}")
    suspend fun getAnalysisResult(
        @PathVariable sampleId: String,
        @PathVariable serviceCode: String,
        @PathVariable batch: String,
        @PathVariable rowNumber: Int
    ): AnalysisResultResponse {
        val query = GetAnalysisResultQuery(
            sampleId = sampleId,
            serviceCode = serviceCode,
            batch = batch,
            rowNumber = rowNumber
        )
        return getAnalysisResultUseCase.execute(query)
    }

    /**
     * 분석 결과 생성
     * POST /api/analysis
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createAnalysisResult(
        @Valid @RequestBody request: CreateAnalysisResultRequest
    ): AnalysisResultResponse {
        val command = CreateAnalysisResultCommand(
            sampleId = request.sampleId,
            serviceCode = request.serviceCode,
            batch = request.batch,
            rowNumber = request.rowNumber,
            cadEnsembleProb = request.cadEnsembleProb,
            too5Pred = request.too5Pred,
            too6Pred = request.too6Pred,
            iscore = request.iscore,
            result = request.result,
            comment = request.comment,
            femsCovBc = request.femsCovBc,
            cfdnaConcentration = request.cfdnaConcentration,
            qc = request.qc
        )
        return createAnalysisResultUseCase.execute(command)
    }

    /**
     * 분석 결과 업데이트
     * PATCH /api/analysis/{sampleId}/{serviceCode}/{batch}/{rowNumber}
     */
    @PatchMapping("/{sampleId}/{serviceCode}/{batch}/{rowNumber}")
    suspend fun updateAnalysisResult(
        @PathVariable sampleId: String,
        @PathVariable serviceCode: String,
        @PathVariable batch: String,
        @PathVariable rowNumber: Int,
        @Valid @RequestBody request: UpdateAnalysisResultRequest
    ): AnalysisResultResponse {
        val command = UpdateAnalysisResultCommand(
            sampleId = sampleId,
            serviceCode = serviceCode,
            batch = batch,
            rowNumber = rowNumber,
            cadEnsembleProb = request.cadEnsembleProb,
            too5Pred = request.too5Pred,
            too6Pred = request.too6Pred,
            iscore = request.iscore,
            result = request.result,
            comment = request.comment,
            femsCovBc = request.femsCovBc,
            cfdnaConcentration = request.cfdnaConcentration
        )
        return updateAnalysisResultUseCase.execute(command)
    }
}

/**
 * 분석 결과 생성 요청
 */
data class CreateAnalysisResultRequest(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val cadEnsembleProb: Double? = null,
    val too5Pred: String? = null,
    val too6Pred: String? = null,
    val iscore: Double? = null,
    val result: String? = null,
    val comment: String? = null,
    val femsCovBc: Double? = null,
    val cfdnaConcentration: Double? = null,
    val qc: UpdateAnalysisQCCommand? = null
)

/**
 * 분석 결과 업데이트 요청
 */
data class UpdateAnalysisResultRequest(
    val cadEnsembleProb: Double? = null,
    val too5Pred: String? = null,
    val too6Pred: String? = null,
    val iscore: Double? = null,
    val result: String? = null,
    val comment: String? = null,
    val femsCovBc: Double? = null,
    val cfdnaConcentration: Double? = null
)
