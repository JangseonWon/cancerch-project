package com.idrsys.ailis.cancerch.application.dto.response

import com.idrsys.ailis.cancerch.domain.sequencing.Sequencing
import java.time.Instant

/**
 * Sequencing 응답 DTO
 */
data class SequencingResponse(
    val id: Long,
    val uuid: String,
    val worklistId: Long,
    val index: Int,
    val sampleId: String,
    val barcode: String,
    val indexName: String,
    val qc: String?,
    val state: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int
) {
    companion object {
        fun from(sequencing: Sequencing): SequencingResponse {
            return SequencingResponse(
                id = sequencing.id.value,
                uuid = sequencing.uuid.value,
                worklistId = sequencing.worklistId.value,
                index = sequencing.index,
                sampleId = sequencing.sampleId.value,
                barcode = sequencing.barcode,
                indexName = sequencing.indexName,
                qc = sequencing.qc,
                state = sequencing.state.name,
                createdAt = sequencing.createdAt,
                updatedAt = sequencing.updatedAt,
                version = sequencing.version
            )
        }
    }
}

/**
 * Sequencing 목록 응답 DTO
 */
data class PagedSequencingResponse(
    val items: List<SequencingResponse>,
    val total: Long,
    val page: Int,
    val size: Int,
    val totalPages: Int
) {
    companion object {
        fun from(
            items: List<Sequencing>,
            total: Long,
            page: Int,
            size: Int
        ): PagedSequencingResponse {
            return PagedSequencingResponse(
                items = items.map { SequencingResponse.from(it) },
                total = total,
                page = page,
                size = size,
                totalPages = ((total + size - 1) / size).toInt()
            )
        }
    }
}
