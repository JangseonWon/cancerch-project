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
    val version: Int = 1
) {
    init {
        require(index > 0) { "Index must be positive" }
        require(sequencingBatch.isNotBlank()) { "Sequencing batch cannot be blank" }
    }

    /**
     * A 프로세스 시작
     */
    fun startA(): Preprocessing {
        require(state == PreprocessingState.PENDING || state == PreprocessingState.HOLDING) {
            "Cannot start A process from $state state"
        }

        return copy(
            state = PreprocessingState.PENDING,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * A 프로세스 완료
     */
    fun completeA(): Preprocessing {
        require(state == PreprocessingState.PENDING) {
            "Cannot complete A process from $state state"
        }

        return copy(
            state = PreprocessingState.PENDING_B,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * A 프로세스 보류
     */
    fun holdA(): Preprocessing {
        require(state == PreprocessingState.PENDING) {
            "Cannot hold A process from $state state"
        }

        return copy(
            state = PreprocessingState.HOLDING,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * B 프로세스 시작
     */
    fun startB(): Preprocessing {
        require(state == PreprocessingState.PENDING_B || state == PreprocessingState.HOLDING_B) {
            "Cannot start B process from $state state"
        }

        return copy(
            state = PreprocessingState.PENDING_B,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * B 프로세스 완료
     */
    fun completeB(): Preprocessing {
        require(state == PreprocessingState.PENDING_B) {
            "Cannot complete B process from $state state"
        }

        return copy(
            state = PreprocessingState.COMPLETE,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * B 프로세스 보류
     */
    fun holdB(): Preprocessing {
        require(state == PreprocessingState.PENDING_B) {
            "Cannot hold B process from $state state"
        }

        return copy(
            state = PreprocessingState.HOLDING_B,
            updatedAt = Instant.now(),
            version = version + 1
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
