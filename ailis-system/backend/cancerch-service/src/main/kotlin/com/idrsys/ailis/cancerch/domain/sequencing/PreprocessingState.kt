package com.idrsys.ailis.cancerch.domain.sequencing

/**
 * Preprocessing 상태
 */
enum class PreprocessingState {
    PENDING,      // A 대기
    HOLDING,      // A 보류
    PENDING_B,    // B 대기
    HOLDING_B,    // B 보류
    COMPLETE;     // 완료

    /**
     * 특정 상태로 전이 가능한지 확인
     */
    fun canTransitionTo(newState: PreprocessingState): Boolean {
        return when (this) {
            PENDING -> newState in setOf(HOLDING, PENDING_B)
            HOLDING -> newState in setOf(PENDING, PENDING_B)
            PENDING_B -> newState in setOf(HOLDING_B, COMPLETE)
            HOLDING_B -> newState in setOf(PENDING_B, COMPLETE)
            COMPLETE -> false
        }
    }
}
