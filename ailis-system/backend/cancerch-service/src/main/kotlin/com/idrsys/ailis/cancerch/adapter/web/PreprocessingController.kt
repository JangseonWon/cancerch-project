package com.idrsys.ailis.cancerch.adapter.web

import com.idrsys.ailis.cancerch.application.dto.request.CreatePreprocessingCommand
import com.idrsys.ailis.cancerch.application.dto.request.GetPreprocessingQuery
import com.idrsys.ailis.cancerch.application.dto.request.PreprocessingAction
import com.idrsys.ailis.cancerch.application.dto.request.UpdatePreprocessingStateCommand
import com.idrsys.ailis.cancerch.application.dto.response.PreprocessingResponse
import com.idrsys.ailis.cancerch.application.usecase.CreatePreprocessingUseCase
import com.idrsys.ailis.cancerch.application.usecase.GetPreprocessingUseCase
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
    private val updatePreprocessingStateUseCase: UpdatePreprocessingStateUseCase
) {

    /**
     * Worklist의 Preprocessing 조회
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
     * A 프로세스 시작
     */
    @PostMapping("/worklists/{worklistId}/start-a")
    suspend fun startA(@PathVariable worklistId: Long): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.START_A
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * A 프로세스 완료
     */
    @PostMapping("/worklists/{worklistId}/complete-a")
    suspend fun completeA(@PathVariable worklistId: Long): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.COMPLETE_A
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * A 프로세스 보류
     */
    @PostMapping("/worklists/{worklistId}/hold-a")
    suspend fun holdA(@PathVariable worklistId: Long): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.HOLD_A
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * B 프로세스 시작
     */
    @PostMapping("/worklists/{worklistId}/start-b")
    suspend fun startB(@PathVariable worklistId: Long): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.START_B
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * B 프로세스 완료
     */
    @PostMapping("/worklists/{worklistId}/complete-b")
    suspend fun completeB(@PathVariable worklistId: Long): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.COMPLETE_B
        )
        return updatePreprocessingStateUseCase.execute(command)
    }

    /**
     * B 프로세스 보류
     */
    @PostMapping("/worklists/{worklistId}/hold-b")
    suspend fun holdB(@PathVariable worklistId: Long): PreprocessingResponse {
        val command = UpdatePreprocessingStateCommand(
            worklistId = worklistId,
            action = PreprocessingAction.HOLD_B
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
