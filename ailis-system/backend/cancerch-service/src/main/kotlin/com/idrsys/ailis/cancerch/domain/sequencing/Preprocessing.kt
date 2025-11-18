package com.idrsys.ailis.cancerch.domain.sequencing

import com.idrsys.ailis.cancerch.domain.model.WorklistId
import java.time.Instant

/**
 * Preprocessing 도메인 모델 (Entity)
 *
 * Sequencing 전처리 프로세스를 나타내는 엔티티
 */
data class Preprocessing(
    val id: PreprocessingId,
    val uuid: PreprocessingUuid,
    val worklistId: WorklistId,
    val index: Int,
    val sequencingBatch: String,
    val state: PreprocessingState,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int = 1,
    val startedBy: String? = null,
    val startedAt: Instant? = null,
    val completedBy: String? = null,
    val completedAt: Instant? = null
) {
    init {
        require(index > 0) { "Index must be positive" }
        require(sequencingBatch.isNotBlank()) { "Sequencing batch cannot be blank" }
    }

    /**
     * A 프로세스 시작
     */
    fun startA(startedBy: String? = null): Preprocessing {
        require(state == PreprocessingState.PENDING || state == PreprocessingState.HOLDING) {
            "Cannot start A process from $state state"
        }

        val now = Instant.now()
        return copy(
            state = PreprocessingState.PENDING,
            updatedAt = now,
            version = version + 1,
            startedBy = startedBy ?: this.startedBy,
            startedAt = if (startedBy != null || this.startedAt == null) now else this.startedAt
        )
    }

    /**
     * A 프로세스 완료
     */
    fun completeA(completedBy: String? = null): Preprocessing {
        require(state == PreprocessingState.PENDING) {
            "Cannot complete A process from $state state"
        }

        val now = Instant.now()
        return copy(
            state = PreprocessingState.PENDING_B,
            updatedAt = now,
            version = version + 1,
            completedBy = completedBy ?: this.completedBy,
            completedAt = if (completedBy != null || this.completedAt == null) now else this.completedAt
        )
    }

    /**
     * A 프로세스 보류
     */
    fun holdA(startedBy: String? = null): Preprocessing {
        require(state == PreprocessingState.PENDING) {
            "Cannot hold A process from $state state"
        }

        return copy(
            state = PreprocessingState.HOLDING,
            updatedAt = Instant.now(),
            version = version + 1,
            startedBy = startedBy ?: this.startedBy
        )
    }

    /**
     * B 프로세스 시작
     */
    fun startB(startedBy: String? = null): Preprocessing {
        require(state == PreprocessingState.PENDING_B || state == PreprocessingState.HOLDING_B) {
            "Cannot start B process from $state state"
        }

        val now = Instant.now()
        return copy(
            state = PreprocessingState.PENDING_B,
            updatedAt = now,
            version = version + 1,
            startedBy = startedBy ?: this.startedBy,
            startedAt = if (startedBy != null || this.startedAt == null) now else this.startedAt
        )
    }

    /**
     * B 프로세스 완료
     */
    fun completeB(completedBy: String? = null): Preprocessing {
        require(state == PreprocessingState.PENDING_B) {
            "Cannot complete B process from $state state"
        }

        val now = Instant.now()
        return copy(
            state = PreprocessingState.COMPLETE,
            updatedAt = now,
            version = version + 1,
            completedBy = completedBy ?: this.completedBy,
            completedAt = if (completedBy != null || this.completedAt == null) now else this.completedAt
        )
    }

    /**
     * B 프로세스 보류
     */
    fun holdB(startedBy: String? = null): Preprocessing {
        require(state == PreprocessingState.PENDING_B) {
            "Cannot hold B process from $state state"
        }

        return copy(
            state = PreprocessingState.HOLDING_B,
            updatedAt = Instant.now(),
            version = version + 1,
            startedBy = startedBy ?: this.startedBy
        )
    }

    companion object {
        /**
         * 새로운 Preprocessing 생성
         */
        fun create(
            worklistId: WorklistId,
            index: Int,
            sequencingBatch: String
        ): Preprocessing {
            val now = Instant.now()
            return Preprocessing(
                id = PreprocessingId.generate(),
                uuid = PreprocessingUuid.generate(),
                worklistId = worklistId,
                index = index,
                sequencingBatch = sequencingBatch,
                state = PreprocessingState.PENDING,
                createdAt = now,
                updatedAt = now,
                version = 1
            )
        }
    }
}
