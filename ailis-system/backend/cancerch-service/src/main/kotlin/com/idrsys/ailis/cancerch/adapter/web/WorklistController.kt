package com.idrsys.ailis.cancerch.adapter.web

import com.idrsys.ailis.cancerch.application.dto.request.CreateWorklistCommand
import com.idrsys.ailis.cancerch.application.dto.request.GetWorklistQuery
import com.idrsys.ailis.cancerch.application.dto.request.ListWorklistsQuery
import com.idrsys.ailis.cancerch.application.dto.request.UpdateWorklistCommand
import com.idrsys.ailis.cancerch.application.dto.response.PagedWorklistResponse
import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse
import com.idrsys.ailis.cancerch.application.usecase.CreateWorklistUseCase
import com.idrsys.ailis.cancerch.application.usecase.GetWorklistUseCase
import com.idrsys.ailis.cancerch.application.usecase.ListWorklistsUseCase
import com.idrsys.ailis.cancerch.application.usecase.UpdateWorklistUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Worklist REST API Controller
 */
@RestController
@RequestMapping("/api/worklists")
class WorklistController(
    private val createWorklistUseCase: CreateWorklistUseCase,
    private val getWorklistUseCase: GetWorklistUseCase,
    private val listWorklistsUseCase: ListWorklistsUseCase,
    private val updateWorklistUseCase: UpdateWorklistUseCase
) {

    /**
     * Worklist 목록 조회
     */
    @GetMapping
    suspend fun listWorklists(
        @RequestParam(required = false) status: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): PagedWorklistResponse {
        val query = ListWorklistsQuery(status = status, page = page, size = size)
        return listWorklistsUseCase.execute(query)
    }

    /**
     * Worklist 상세 조회
     */
    @GetMapping("/{id}")
    suspend fun getWorklist(@PathVariable id: Long): WorklistResponse {
        val query = GetWorklistQuery(id = id)
        return getWorklistUseCase.execute(query)
    }

    /**
     * Worklist 생성
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createWorklist(
        @Valid @RequestBody request: CreateWorklistRequest
    ): WorklistResponse {
        val command = CreateWorklistCommand(
            name = request.name,
            batchPrefix = request.batchPrefix,
            batchIndex = request.batchIndex,
            createdBy = request.createdBy
        )
        return createWorklistUseCase.execute(command)
    }

    /**
     * Worklist 수정
     */
    @PatchMapping("/{id}")
    suspend fun updateWorklist(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateWorklistRequest
    ): WorklistResponse {
        val command = UpdateWorklistCommand(
            id = id,
            name = request.name,
            updatedBy = request.updatedBy
        )
        return updateWorklistUseCase.execute(command)
    }
}

/**
 * Worklist 생성 요청
 */
data class CreateWorklistRequest(
    val name: String,
    val batchPrefix: String,
    val batchIndex: Int,
    val createdBy: String
)

/**
 * Worklist 수정 요청
 */
data class UpdateWorklistRequest(
    val name: String,
    val updatedBy: String
)
