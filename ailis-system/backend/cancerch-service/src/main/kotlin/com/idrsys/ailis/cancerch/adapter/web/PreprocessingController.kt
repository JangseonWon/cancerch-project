package com.idrsys.ailis.cancerch.adapter.web

import com.idrsys.ailis.cancerch.application.dto.request.CreatePreprocessingCommand
import com.idrsys.ailis.cancerch.application.dto.request.GetPreprocessingQuery
import com.idrsys.ailis.cancerch.application.dto.request.ListPreprocessingsQuery
import com.idrsys.ailis.cancerch.application.dto.request.PreprocessingAction
import com.idrsys.ailis.cancerch.application.dto.request.UpdatePreprocessingCommand
import com.idrsys.ailis.cancerch.application.dto.request.UpdatePreprocessingStateCommand
import com.idrsys.ailis.cancerch.application.dto.response.PagedPreprocessingResponse
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse
import com.idrsys.ailis.cancerch.application.usecase.CreatePreprocessingUseCase
import com.idrsys.ailis.cancerch.application.usecase.GetPreprocessingUseCase
import com.idrsys.ailis.cancerch.application.usecase.ListPreprocessingsUseCase
import com.idrsys.ailis.cancerch.application.usecase.UpdatePreprocessingUseCase
import com.idrsys.ailis.cancerch.application.usecase.UpdatePreprocessingStateUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Preprocessing REST API Controller
 */
@RestController
@RequestMapping("/api/preprocessing")
class PreprocessingController(
    private val createPreprocessingUseCase: CreatePreprocessingUseCase,
    private val getPreprocessingUseCase: GetPreprocessingUseCase,
    private val listPreprocessingsUseCase: ListPreprocessingsUseCase,
    private val updatePreprocessingUseCase: UpdatePreprocessingUseCase,
    private val updatePreprocessingStateUseCase: UpdatePreprocessingStateUseCase
) {

    /**
     * Preprocessing 목록 조회 (프론트엔드 호환)
     */
    @GetMapping
    suspend fun listPreprocessings(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) status: String?
    ): PagedPreprocessingResponse {
        val query = ListPreprocessingsQuery(
            state = status,
            page = page,
            size = size
        )
        return listPreprocessingsUseCase.execute(query)
    }

    /**
     * Preprocessing 단일 조회 (프론트엔드 호환)
     * {id}는 실제로 worklistId를 의미
     */
    @GetMapping("/{id}")
    suspend fun getPreprocessingById(@PathVariable id: Long): PreprocessingResponse {
        val query = GetPreprocessingQuery(worklistId = id)
        return getPreprocessingUseCase.execute(query)
    }

    /**
     * Worklist의 Preprocessing 조회 (기존 엔드포인트 - 하위 호환성)
     */
    @GetMapping("/worklists/{worklistId}")
    suspend fun getPreprocessing(@PathVariable worklistId: Long): PreprocessingResponse {
        val query = GetPreprocessingQuery(worklistId = worklistId)
        return getPreprocessingUseCase.execute(query)
    }

    /**
     * Preprocessing 생성
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createPreprocessing(
        @Valid @RequestBody request: CreatePreprocessingRequest
    ): PreprocessingResponse {
        val command = CreatePreprocessingCommand(
            worklistId = request.worklistId,
            index = request.index,
            sequencingBatch = request.sequencingBatch
        )
        return createPreprocessingUseCase.execute(command)
    }

    /**
     * Preprocessing 시작 (프론트엔드 호환 - A 프로세스 시작)
     */
    @PostMapping("/{worklistId}/start")
    suspend fun start(
        @PathVariable worklistId: Long,
        @RequestBody(required = false) request: StartPreprocessingRequest?
    ): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.START_A,
            startedBy = request?.startedBy
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * Preprocessing 완료 (프론트엔드 호환 - A 프로세스 완료)
     */
    @PostMapping("/{worklistId}/complete")
    suspend fun complete(
        @PathVariable worklistId: Long,
        @RequestBody(required = false) request: CompletePreprocessingRequest?
    ): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.COMPLETE_A,
            completedBy = request?.completedBy
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * Preprocessing 업데이트 (프론트엔드 호환)
     */
    @PatchMapping("/{worklistId}")
    suspend fun updatePreprocessing(
        @PathVariable worklistId: Long,
        @Valid @RequestBody request: UpdatePreprocessingRequest
    ): PreprocessingResponse {
        val command = UpdatePreprocessingCommand(
            worklistId = worklistId,
            index = request.index,
            sequencingBatch = request.sequencingBatch
        )
        return updatePreprocessingUseCase.execute(command)
    }

    /**
     * A 프로세스 시작 (기존 엔드포인트 - 하위 호환성)
     */
    @PostMapping("/worklists/{worklistId}/start-a")
    suspend fun startA(
        @PathVariable worklistId: Long,
        @RequestBody(required = false) request: StartPreprocessingRequest?
    ): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.START_A,
            startedBy = request?.startedBy
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * A 프로세스 완료
     */
    @PostMapping("/worklists/{worklistId}/complete-a")
    suspend fun completeA(
        @PathVariable worklistId: Long,
        @RequestBody(required = false) request: CompletePreprocessingRequest?
    ): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.COMPLETE_A,
            completedBy = request?.completedBy
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * A 프로세스 보류
     */
    @PostMapping("/worklists/{worklistId}/hold-a")
    suspend fun holdA(
        @PathVariable worklistId: Long,
        @RequestBody(required = false) request: StartPreprocessingRequest?
    ): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.HOLD_A,
            startedBy = request?.startedBy
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * B 프로세스 시작
     */
    @PostMapping("/worklists/{worklistId}/start-b")
    suspend fun startB(
        @PathVariable worklistId: Long,
        @RequestBody(required = false) request: StartPreprocessingRequest?
    ): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.START_B,
            startedBy = request?.startedBy
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * B 프로세스 완료
     */
    @PostMapping("/worklists/{worklistId}/complete-b")
    suspend fun completeB(
        @PathVariable worklistId: Long,
        @RequestBody(required = false) request: CompletePreprocessingRequest?
    ): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.COMPLETE_B,
            completedBy = request?.completedBy
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * B 프로세스 보류
     */
    @PostMapping("/worklists/{worklistId}/hold-b")
    suspend fun holdB(
        @PathVariable worklistId: Long,
        @RequestBody(required = false) request: StartPreprocessingRequest?
    ): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.HOLD_B,
            startedBy = request?.startedBy
        )
        return updatePreprocessingStateUseCase.execute(command)
    }
}

/**
 * Preprocessing 생성 요청
 */
data class CreatePreprocessingRequest(
    val worklistId: Long,
    val index: Int,
    val sequencingBatch: String
)

/**
 * Preprocessing 업데이트 요청
 */
data class UpdatePreprocessingRequest(
    val index: Int? = null,
    val sequencingBatch: String? = null
)

/**
 * Preprocessing 시작 요청
 */
data class StartPreprocessingRequest(
    val startedBy: String? = null
)

/**
 * Preprocessing 완료 요청
 */
data class CompletePreprocessingRequest(
    val completedBy: String? = null
)
