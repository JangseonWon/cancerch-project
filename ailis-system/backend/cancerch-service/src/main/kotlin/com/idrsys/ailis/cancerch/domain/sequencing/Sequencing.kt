package com.idrsys.ailis.cancerch.domain.sequencing

import com.idrsys.ailis.cancerch.domain.model.SampleId
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import java.time.Instant

/**
 * Sequencing 도메인 모델 (Entity)
 *
 * 개별 샘플의 시퀀싱 정보를 나타내는 엔티티
 */
data class Sequencing(
    val id: SequencingId,
    val uuid: SequencingUuid,
    val worklistId: WorklistId,
    val index: Int,
    val sampleId: SampleId,
    val barcode: String,
    val indexName: String,
    val qc: String?,
    val state: SequencingState,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int = 1
) {
    init {
        require(index > 0) { "Index must be positive" }
        require(barcode.isNotBlank()) { "Barcode cannot be blank" }
        require(indexName.isNotBlank()) { "Index name cannot be blank" }
    }

    /**
     * QC 정보 업데이트
     */
    fun updateQc(qc: String): Sequencing {
        return copy(
            qc = qc,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * 상태 변경
     */
    fun changeState(newState: SequencingState): Sequencing {
        require(state.canTransitionTo(newState)) {
            "Cannot transition from $state to $newState"
        }

        return copy(
            state = newState,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    companion object {
        /**
         * 새로운 Sequencing 생성
         */
        fun create(
            worklistId: WorklistId,
            index: Int,
            sampleId: SampleId,
            barcode: String,
            indexName: String,
            qc: String? = null
        ): Sequencing {
            val now = Instant.now()
            return Sequencing(
                id = SequencingId.generate(),
                uuid = SequencingUuid.generate(),
                worklistId = worklistId,
                index = index,
                sampleId = sampleId,
                barcode = barcode,
                indexName = indexName,
                qc = qc,
                state = SequencingState.PENDING,
                createdAt = now,
                updatedAt = now,
                version = 1
            )
        }
    }
}

/**
 * Sequencing 상태
 */
enum class SequencingState {
    PENDING,      // 대기
    PROCESSING,   // 처리중
    COMPLETE,     // 완료
    FAILED;       // 실패

    /**
     * 특정 상태로 전이 가능한지 확인
     */
    fun canTransitionTo(newState: SequencingState): Boolean {
        return when (this) {
            PENDING -> newState in setOf(PROCESSING, FAILED)
            PROCESSING -> newState in setOf(COMPLETE, FAILED)
            COMPLETE -> false
            FAILED -> newState == PENDING
        }
    }
}
