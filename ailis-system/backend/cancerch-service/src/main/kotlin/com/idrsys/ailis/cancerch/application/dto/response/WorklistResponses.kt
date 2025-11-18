package com.idrsys.ailis.cancerch.application.dto.response

import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.idrsys.ailis.cancerch.domain.model.WorkSample
import java.time.Instant

/**
 * Worklist 응답 DTO
 */
data class WorklistResponse(
    val id: Long,
    val uuid: String,
    val name: String,
    val batchNumber: String,
    val status: String,
    val sampleCount: Int,
    val samples: List<WorkSampleResponse>,
    val createdAt: Instant,
    val createdBy: String,
    val updatedAt: Instant,
    val updatedBy: String,
    val version: Int
) {
    companion object {
        fun from(worklist: Worklist): WorklistResponse {
            return WorklistResponse(
                id = worklist.id.value,
                uuid = worklist.uuid.toString(),
                name = worklist.name,
                batchNumber = worklist.batchNumber(),
                status = worklist.status.name,
                sampleCount = worklist.samples.size,
                samples = worklist.samples.map { WorkSampleResponse.from(it) },
                createdAt = worklist.createdAt,
                createdBy = worklist.createdBy,
                updatedAt = worklist.updatedAt,
                updatedBy = worklist.updatedBy,
                version = worklist.version
            )
        }
    }
}

/**
 * WorkSample 응답 DTO
 */
data class WorkSampleResponse(
    val sampleId: Long,
    val barcode: String,
    val serviceCode: String,
    val rowNumber: Int
) {
    companion object {
        fun from(sample: WorkSample): WorkSampleResponse {
            return WorkSampleResponse(
                sampleId = sample.sampleId.value,
                barcode = sample.barcode.value,
                serviceCode = sample.serviceCode,
                rowNumber = sample.rowNumber
            )
        }
    }
}

/**
 * 페이징된 Worklist 목록 응답
 */
data class PagedWorklistResponse(
    val items: List<WorklistResponse>,
    val page: Int,
    val size: Int,
    val totalCount: Long,
    val totalPages: Int
) {
    val hasNext: Boolean get() = page < totalPages - 1
    val hasPrevious: Boolean get() = page > 0
}
