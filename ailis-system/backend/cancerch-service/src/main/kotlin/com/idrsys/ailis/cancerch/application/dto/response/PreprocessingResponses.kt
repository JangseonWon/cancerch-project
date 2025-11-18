package com.idrsys.ailis.cancerch.application.dto.response

import com.idrsys.ailis.cancerch.domain.sequencing.Preprocessing
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingState
import java.time.Instant

import com.idrsys.ailis.cancerch.domain.model.Worklist

/**
 * Preprocessing 응답 DTO
 */
data class PreprocessingResponse(
    val id: Long,
    val uuid: String,
    val worklistId: Long,
    val worklistName: String,
    val batchNumber: String,
    val index: Int,
    val sequencingBatch: String,
    val state: String,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int,
    val startedBy: String? = null,
    val startedAt: Instant? = null,
    val completedBy: String? = null,
    val completedAt: Instant? = null
) {
    companion object {
        fun from(preprocessing: Preprocessing): PreprocessingResponse {
            return PreprocessingResponse(
                id = preprocessing.id.value,
                uuid = preprocessing.uuid.value,
                worklistId = preprocessing.worklistId.value,
                worklistName = "",  // Worklist 정보 필요 - 별도의 from() 메서드 사용
                batchNumber = "",   // Worklist 정보 필요 - 별도의 from() 메서드 사용
                index = preprocessing.index,
                sequencingBatch = preprocessing.sequencingBatch,
                state = preprocessing.state.name,
                status = preprocessing.state.name,
                createdAt = preprocessing.createdAt,
                updatedAt = preprocessing.updatedAt,
                version = preprocessing.version,
                startedBy = preprocessing.startedBy,
                startedAt = preprocessing.startedAt,
                completedBy = preprocessing.completedBy,
                completedAt = preprocessing.completedAt
            )
        }

        /**
         * Preprocessing과 Worklist 정보를 함께 받아 응답 생성
         */
        fun from(preprocessing: Preprocessing, worklist: Worklist): PreprocessingResponse {
            return PreprocessingResponse(
                id = preprocessing.id.value,
                uuid = preprocessing.uuid.value,
                worklistId = preprocessing.worklistId.value,
                worklistName = worklist.name,
                batchNumber = worklist.batchNumber(),
                index = preprocessing.index,
                sequencingBatch = preprocessing.sequencingBatch,
                state = preprocessing.state.name,
                status = preprocessing.state.name,
                createdAt = preprocessing.createdAt,
                updatedAt = preprocessing.updatedAt,
                version = preprocessing.version,
                startedBy = preprocessing.startedBy,
                startedAt = preprocessing.startedAt,
                completedBy = preprocessing.completedBy,
                completedAt = preprocessing.completedAt
            )
        }
    }
}

/**
 * Preprocessing 목록 응답 DTO
 */
data class PagedPreprocessingResponse(
    val items: List<PreprocessingResponse>,
    val totalCount: Long,
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
                totalCount = total,
                page = page,
                size = size,
                totalPages = ((total + size - 1) / size).toInt()
            )
        }

        /**
         * Preprocessing과 Worklist 정보 맵을 함께 받아 응답 생성
         */
        fun from(
            items: List<Preprocessing>,
            worklistMap: Map<Long, Worklist>,
            total: Long,
            page: Int,
            size: Int
        ): PagedPreprocessingResponse {
            return PagedPreprocessingResponse(
                items = items.map { preprocessing ->
                    val worklist = worklistMap[preprocessing.worklistId.value]
                    if (worklist != null) {
                        PreprocessingResponse.from(preprocessing, worklist)
                    } else {
                        PreprocessingResponse.from(preprocessing)
                    }
                },
                totalCount = total,
                page = page,
                size = size,
                totalPages = ((total + size - 1) / size).toInt()
            )
        }
    }
}
