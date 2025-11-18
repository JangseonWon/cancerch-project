package com.idrsys.ailis.cancerch.domain.sequencing

import com.idrsys.ailis.cancerch.domain.model.SampleId
import com.idrsys.ailis.cancerch.domain.model.WorklistId

/**
 * Sequencing Repository 인터페이스 (Inbound Port)
 *
 * Domain Layer에서 정의하고, Adapter Layer에서 구현
 */
interface SequencingRepository {
    /**
     * ID로 Sequencing 조회
     */
    suspend fun findById(id: SequencingId): Sequencing?

    /**
     * UUID로 Sequencing 조회
     */
    suspend fun findByUuid(uuid: SequencingUuid): Sequencing?

    /**
     * Worklist ID로 Sequencing 목록 조회
     */
    suspend fun findByWorklistId(worklistId: WorklistId): List<Sequencing>

    /**
     * Sample ID로 Sequencing 조회
     */
    suspend fun findBySampleId(sampleId: SampleId): Sequencing?

    /**
     * Barcode로 Sequencing 조회
     */
    suspend fun findByBarcode(barcode: String): Sequencing?

    /**
     * Sequencing 저장 (생성 또는 업데이트)
     */
    suspend fun save(sequencing: Sequencing): Sequencing

    /**
     * Sequencing 삭제
     */
    suspend fun delete(id: SequencingId)

    /**
     * 상태별 Sequencing 목록 조회
     */
    suspend fun findByState(state: SequencingState, limit: Int): List<Sequencing>

    /**
     * 모든 Sequencing 조회 (페이징)
     */
    suspend fun findAll(offset: Int, limit: Int): List<Sequencing>

    /**
     * 전체 개수 조회
     */
    suspend fun count(): Long

    /**
     * Sequencing 존재 여부 확인
     */
    suspend fun existsById(id: SequencingId): Boolean

    /**
     * Barcode 존재 여부 확인
     */
    suspend fun existsByBarcode(barcode: String): Boolean
}
