package com.idrsys.ailis.cancerch.domain.model

import java.time.Instant

/**
 * Worklist 도메인 모델 (Entity)
 *
 * 검체들을 그룹화하여 처리하는 작업 목록
 */
data class Worklist(
    val id: WorklistId,
    val uuid: WorklistUuid,
    val name: String,
    val batchPrefix: String,
    val batchIndex: Int,
    val status: WorklistStatus,
    val samples: List<WorkSample>,
    val createdAt: Instant,
    val createdBy: String,
    val updatedAt: Instant,
    val updatedBy: String,
    val version: Int = 1
) {
    init {
        require(name.isNotBlank()) { "Worklist name cannot be blank" }
        require(name.length <= 100) { "Worklist name too long" }
        require(batchPrefix.isNotBlank()) { "Batch prefix cannot be blank" }
        require(batchIndex > 0) { "Batch index must be positive" }
        require(createdBy.isNotBlank()) { "Creator cannot be blank" }
        require(updatedBy.isNotBlank()) { "Updater cannot be blank" }
    }

    /**
     * 배치 번호 (prefix + index)
     */
    fun batchNumber(): String = "$batchPrefix$batchIndex"

    /**
     * 상태 변경
     */
    fun changeStatus(newStatus: WorklistStatus, updatedBy: String): Worklist {
        require(status.canTransitionTo(newStatus)) {
            "Cannot transition from $status to $newStatus"
        }

        return copy(
            status = newStatus,
            updatedBy = updatedBy,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * 샘플 추가
     */
    fun addSample(sample: WorkSample, updatedBy: String): Worklist {
        require(status == WorklistStatus.PENDING) {
            "Cannot add sample to worklist in $status status"
        }

        require(!samples.any { it.sampleId == sample.sampleId }) {
            "Sample ${sample.sampleId} already exists in worklist"
        }

        return copy(
            samples = samples + sample,
            updatedBy = updatedBy,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * 샘플 제거
     */
    fun removeSample(sampleId: SampleId, updatedBy: String): Worklist {
        require(status == WorklistStatus.PENDING) {
            "Cannot remove sample from worklist in $status status"
        }

        return copy(
            samples = samples.filterNot { it.sampleId == sampleId },
            updatedBy = updatedBy,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    companion object {
        /**
         * 새로운 Worklist 생성
         */
        fun create(
            name: String,
            batchPrefix: String,
            batchIndex: Int,
            createdBy: String
        ): Worklist {
            val now = Instant.now()
            return Worklist(
                id = WorklistId.generate(),
                uuid = WorklistUuid.generate(),
                name = name,
                batchPrefix = batchPrefix,
                batchIndex = batchIndex,
                status = WorklistStatus.PENDING,
                samples = emptyList(),
                createdAt = now,
                createdBy = createdBy,
                updatedAt = now,
                updatedBy = createdBy,
                version = 1
            )
        }
    }
}
