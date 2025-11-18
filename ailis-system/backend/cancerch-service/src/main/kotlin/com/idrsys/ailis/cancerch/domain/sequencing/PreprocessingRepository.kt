package com.idrsys.ailis.cancerch.domain.sequencing

import com.idrsys.ailis.cancerch.domain.model.WorklistId

/**
 * Preprocessing Repository 인터페이스 (Inbound Port)
 *
 * Domain Layer에서 정의하고, Adapter Layer에서 구현
 */
interface PreprocessingRepository {
    /**
     * ID로 Preprocessing 조회
     */
    suspend fun findById(id: PreprocessingId): Preprocessing?

    /**
     * UUID로 Preprocessing 조회
     */
    suspend fun findByUuid(uuid: PreprocessingUuid): Preprocessing?

    /**
     * Worklist ID로 Preprocessing 조회
     */
    suspend fun findByWorklistId(worklistId: WorklistId): Preprocessing?

    /**
     * Preprocessing 저장 (생성 또는 업데이트)
     */
    suspend fun save(preprocessing: Preprocessing): Preprocessing

    /**
     * Preprocessing 삭제
     */
    suspend fun delete(id: PreprocessingId)

    /**
     * 상태별 Preprocessing 목록 조회
     */
    suspend fun findByState(state: PreprocessingState, offset: Int, limit: Int): List<Preprocessing>

    /**
     * 모든 Preprocessing 조회 (페이징)
     */
    suspend fun findAll(offset: Int, limit: Int): List<Preprocessing>

    /**
     * 전체 개수 조회
     */
    suspend fun count(): Long

    /**
     * Preprocessing 존재 여부 확인
     */
    suspend fun existsById(id: PreprocessingId): Boolean

    /**
     * Worklist ID로 존재 여부 확인
     */
    suspend fun existsByWorklistId(worklistId: WorklistId): Boolean
}
