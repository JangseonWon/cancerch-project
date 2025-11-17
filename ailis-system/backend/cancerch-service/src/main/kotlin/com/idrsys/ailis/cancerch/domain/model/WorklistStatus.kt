package com.idrsys.ailis.cancerch.domain.model

/**
 * Worklist 상태
 */
enum class WorklistStatus {
    /**
     * 대기 중
     */
    PENDING,

    /**
     * 진행 중
     */
    IN_PROGRESS,

    /**
     * 완료
     */
    COMPLETED,

    /**
     * 취소됨
     */
    CANCELLED;

    fun canTransitionTo(newStatus: WorklistStatus): Boolean {
        return when (this) {
            PENDING -> newStatus in setOf(IN_PROGRESS, CANCELLED)
            IN_PROGRESS -> newStatus in setOf(COMPLETED, CANCELLED)
            COMPLETED -> false
            CANCELLED -> false
        }
    }
}
