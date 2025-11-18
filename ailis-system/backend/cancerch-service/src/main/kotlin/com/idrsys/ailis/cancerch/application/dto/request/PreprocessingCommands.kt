package com.idrsys.ailis.cancerch.application.dto.request

/**
 * Preprocessing 생성 커맨드
 */
data class CreatePreprocessingCommand(
    val worklistId: Long,
    val index: Int,
    val sequencingBatch: String
)

/**
 * Preprocessing 상태 업데이트 커맨드
 */
data class UpdatePreprocessingStateCommand(
    val worklistId: Long,
    val action: PreprocessingAction,
    val startedBy: String? = null,
    val completedBy: String? = null
)

/**
 * Preprocessing 액션
 */
enum class PreprocessingAction {
    START_A,
    COMPLETE_A,
    HOLD_A,
    START_B,
    COMPLETE_B,
    HOLD_B
}

/**
 * Preprocessing 조회 쿼리
 */
data class GetPreprocessingQuery(
    val worklistId: Long
)

/**
 * Preprocessing 목록 조회 쿼리
 */
data class ListPreprocessingsQuery(
    val state: String? = null,
    val page: Int = 0,
    val size: Int = 20
)

/**
 * Preprocessing 업데이트 커맨드
 */
data class UpdatePreprocessingCommand(
    val worklistId: Long,
    val index: Int? = null,
    val sequencingBatch: String? = null
)
