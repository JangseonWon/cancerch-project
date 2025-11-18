package com.idrsys.ailis.cancerch.adapter.web

import com.idrsys.ailis.cancerch.adapter.repository.InMemoryAnalysisRepository
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
import com.idrsys.ailis.cancerch.domain.repository.AnalysisRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Analysis REST API Controller
 */
@RestController
@RequestMapping("/api/analysis")
class AnalysisController(
    private val createAnalysisResultUseCase: CreateAnalysisResultUseCase,
    private val updateAnalysisResultUseCase: UpdateAnalysisResultUseCase,
    private val searchAnalysisUseCase: SearchAnalysisUseCase,
    private val getAnalysisResultUseCase: GetAnalysisResultUseCase,
    private val analysisRepository: AnalysisRepository
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
 * 프론트엔드 호환 Analysis Results REST API Controller
 * 단순 ID 기반 엔드포인트 제공
 */
@RestController
@RequestMapping("/api/analysis-results")
class AnalysisResultsController(
    private val createAnalysisResultUseCase: CreateAnalysisResultUseCase,
    private val updateAnalysisResultUseCase: UpdateAnalysisResultUseCase,
    private val searchAnalysisUseCase: SearchAnalysisUseCase,
    private val getAnalysisResultUseCase: GetAnalysisResultUseCase,
    private val analysisRepository: AnalysisRepository
) {

    /**
     * 분석 결과 검색 (프론트엔드 호환)
     * GET /api/analysis-results?page={page}&size={size}&search={search}
     */
    @GetMapping
    suspend fun searchAnalysisResults(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) search: String?
    ): PagedAnalysisResultResponseWithId {
        val query = SearchAnalysisQuery(
            search = search,
            page = page,
            size = size
        )
        val response = searchAnalysisUseCase.execute(query)

        // 각 결과에 숫자 ID 추가
        val repo = analysisRepository as? InMemoryAnalysisRepository
            ?: throw IllegalStateException("Repository must be InMemoryAnalysisRepository")

        val resultsWithId = response.results.map { result ->
            val analysisId = com.idrsys.ailis.cancerch.domain.model.AnalysisId.from(
                result.sampleId,
                result.serviceCode,
                result.batch,
                result.rowNumber
            )
            val numericId = repo.getNumericId(analysisId)
            result.copy(id = numericId)
        }

        return PagedAnalysisResultResponseWithId(
            items = resultsWithId,
            totalCount = response.totalCount,
            page = response.page,
            size = response.size,
            totalPages = response.totalPages
        )
    }

    /**
     * 분석 결과 상세 조회 (단순 ID)
     * GET /api/analysis-results/{id}
     */
    @GetMapping("/{id}")
    suspend fun getAnalysisResultById(
        @PathVariable id: Long
    ): AnalysisResultResponse {
        val repo = analysisRepository as? InMemoryAnalysisRepository
            ?: throw IllegalStateException("Repository must be InMemoryAnalysisRepository")

        val analysisResult = repo.findByNumericId(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Analysis result not found with id: $id")

        return AnalysisResultResponse.from(analysisResult, id)
    }

    /**
     * 분석 결과 업데이트 (단순 ID)
     * PATCH /api/analysis-results/{id}
     */
    @PatchMapping("/{id}")
    suspend fun updateAnalysisResultById(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateAnalysisResultRequest
    ): AnalysisResultResponse {
        val repo = analysisRepository as? InMemoryAnalysisRepository
            ?: throw IllegalStateException("Repository must be InMemoryAnalysisRepository")

        val analysisResult = repo.findByNumericId(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Analysis result not found with id: $id")

        val command = UpdateAnalysisResultCommand(
            sampleId = analysisResult.id.sampleId,
            serviceCode = analysisResult.id.serviceCode,
            batch = analysisResult.id.batch,
            rowNumber = analysisResult.id.rowNumber,
            cadEnsembleProb = request.cadEnsembleProb,
            too5Pred = request.too5Pred,
            too6Pred = request.too6Pred,
            iscore = request.iscore,
            result = request.result,
            comment = request.comment,
            femsCovBc = request.femsCovBc,
            cfdnaConcentration = request.cfdnaConcentration
        )

        val updatedResult = updateAnalysisResultUseCase.execute(command)
        return updatedResult.copy(id = id)
    }

    /**
     * 분석 결과 생성 (프론트엔드 호환)
     * POST /api/analysis-results
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
        val createdResult = createAnalysisResultUseCase.execute(command)

        // 생성된 결과의 숫자 ID 조회
        val repo = analysisRepository as? InMemoryAnalysisRepository
            ?: throw IllegalStateException("Repository must be InMemoryAnalysisRepository")

        val analysisId = com.idrsys.ailis.cancerch.domain.model.AnalysisId.from(
            createdResult.sampleId,
            createdResult.serviceCode,
            createdResult.batch,
            createdResult.rowNumber
        )
        val numericId = repo.getNumericId(analysisId)

        return createdResult.copy(id = numericId)
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

/**
 * 분석 결과 목록 응답 (숫자 ID 포함)
 */
data class PagedAnalysisResultResponseWithId(
    val items: List<AnalysisResultResponse>,
    val totalCount: Int,
    val page: Int,
    val size: Int,
    val totalPages: Int
)
