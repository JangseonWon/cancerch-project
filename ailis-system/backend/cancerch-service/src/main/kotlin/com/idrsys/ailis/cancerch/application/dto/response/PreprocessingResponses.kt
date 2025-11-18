package com.idrsys.ailis.cancerch.application.dto.response

import com.idrsys.ailis.cancerch.domain.sequencing.Preprocessing
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingState
import java.time.Instant

/**
 * Preprocessing 응답 DTO
 */
data class PreprocessingResponse(
    val id: Long,
    val uuid: String,
    val worklistId: Long,
    val index: Int,
    val sequencingBatch: String,
    val state: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int
) {
    companion object {
        fun from(preprocessing: Preprocessing): PreprocessingResponse {
            return PreprocessingResponse(
                id = preprocessing.id.value,
                uuid = preprocessing.uuid.value,
                worklistId = preprocessing.worklistId.value,
                index = preprocessing.index,
                sequencingBatch = preprocessing.sequencingBatch,
                state = preprocessing.state.name,
                createdAt = preprocessing.createdAt,
                updatedAt = preprocessing.updatedAt,
                version = preprocessing.version
            )
        }
    }
}

/**
 * Preprocessing 목록 응답 DTO
 */
data class PagedPreprocessingResponse(
    val items: List<PreprocessingResponse>,
    val total: Long,
    val page: Int,
    val size: Int,
    val totalPages: Int
) {
    companion object {
        fun from(
            items: List<Preprocessing>,
            total: Long,
            page: Int,
            size: Int
        ): PagedPreprocessingResponse {
            return PagedPreprocessingResponse(
                items = items.map { PreprocessingResponse.from(it) },
                total = total,
                page = page,
                size = size,
                totalPages = ((total + size - 1) / size).toInt()
            )
        }
    }
}
