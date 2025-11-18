package com.idrsys.ailis.cancerch.adapter.web

import com.idrsys.ailis.cancerch.application.dto.request.CreateSequencingCommand
import com.idrsys.ailis.cancerch.application.dto.request.GetSequencingQuery
import com.idrsys.ailis.cancerch.application.dto.request.ListSequencingsQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedSequencingResponse
import com.idrsys.ailis.cancerch.application.dto.response.SequencingResponse
import com.idrsys.ailis.cancerch.application.usecase.CreateSequencingUseCase
import com.idrsys.ailis.cancerch.application.usecase.GetSequencingUseCase
import com.idrsys.ailis.cancerch.application.usecase.ListSequencingsUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Sequencing REST API Controller
 */
@RestController
@RequestMapping("/api/sequencings")
class SequencingController(
    private val createSequencingUseCase: CreateSequencingUseCase,
    private val getSequencingUseCase: GetSequencingUseCase,
    private val listSequencingsUseCase: ListSequencingsUseCase
) {

    /**
     * Sequencing 목록 조회
     */
    @GetMapping
    suspend fun listSequencings(
        @RequestParam(required = false) worklistId: Long?,
        @RequestParam(required = false) state: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): PagedSequencingResponse {
        val query = ListSequencingsQuery(
            worklistId = worklistId,
            state = state,
            page = page,
            size = size
        )
        return listSequencingsUseCase.execute(query)
    }

    /**
     * Sequencing 상세 조회
     */
    @GetMapping("/{id}")
    suspend fun getSequencing(@PathVariable id: Long): SequencingResponse {
        val query = GetSequencingQuery(id = id)
        return getSequencingUseCase.execute(query)
    }

    /**
     * Sequencing 생성
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createSequencing(
        @Valid @RequestBody request: CreateSequencingRequest
    ): SequencingResponse {
        val command = CreateSequencingCommand(
            worklistId = request.worklistId,
            index = request.index,
            sampleId = request.sampleId,
            barcode = request.barcode,
            indexName = request.indexName,
            qc = request.qc
        )
        return createSequencingUseCase.execute(command)
    }
}

/**
 * Sequencing 생성 요청
 */
data class CreateSequencingRequest(
    val worklistId: Long,
    val index: Int,
    val sampleId: String,
    val barcode: String,
    val indexName: String,
    val qc: String? = null
)
