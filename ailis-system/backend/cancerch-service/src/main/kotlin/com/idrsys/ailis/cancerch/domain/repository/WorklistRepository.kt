package com.idrsys.ailis.cancerch.domain.repository

import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.model.WorklistStatus
import com.idrsys.ailis.cancerch.domain.model.WorklistUuid

/**
 * Worklist Repository 인터페이스 (Inbound Port)
 *
 * Domain Layer에서 정의하고, Adapter Layer에서 구현
 */
interface WorklistRepository {
    /**
     * ID로 Worklist 조회
     */
    suspend fun findById(id: WorklistId): Worklist?

    /**
     * UUID로 Worklist 조회 (레거시 호환)
     */
    suspend fun findByUuid(uuid: WorklistUuid): Worklist?

    /**
     * Worklist 저장 (생성 또는 업데이트)
     */
    suspend fun save(worklist: Worklist): Worklist

    /**
     * Worklist 삭제
     */
    suspend fun delete(id: WorklistId)

    /**
     * 상태별 Worklist 목록 조회
     */
    suspend fun findByStatus(status: WorklistStatus, limit: Int): List<Worklist>

    /**
     * 모든 Worklist 조회 (페이징)
     */
    suspend fun findAll(offset: Int, limit: Int): List<Worklist>

    /**
     * 전체 개수 조회
     */
    suspend fun count(): Long

    /**
     * Worklist 존재 여부 확인
     */
    suspend fun existsById(id: WorklistId): Boolean
}
