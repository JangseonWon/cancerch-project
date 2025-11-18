package com.idrsys.ailis.cancerch.domain.repository

import com.idrsys.ailis.cancerch.domain.model.AnalysisId
import com.idrsys.ailis.cancerch.domain.model.AnalysisResult

/**
 * AnalysisResult Repository 인터페이스
 *
 * 분석 결과 저장소의 도메인 계층 인터페이스
 */
interface AnalysisRepository {

    /**
     * 분석 결과 저장
     */
    suspend fun save(analysisResult: AnalysisResult): AnalysisResult

    /**
     * 분석 결과 조회
     */
    suspend fun findById(id: AnalysisId): AnalysisResult?

    /**
     * 모든 분석 결과 조회
     */
    suspend fun findAll(): List<AnalysisResult>

    /**
     * Sample ID로 분석 결과 조회
     */
    suspend fun findBySampleId(sampleId: String): List<AnalysisResult>

    /**
     * Batch로 분석 결과 조회
     */
    suspend fun findByBatch(batch: String): List<AnalysisResult>

    /**
     * Service Code로 분석 결과 조회
     */
    suspend fun findByServiceCode(serviceCode: String): List<AnalysisResult>

    /**
     * 검색 조건으로 분석 결과 조회
     */
    suspend fun search(
        sampleId: String? = null,
        serviceCode: String? = null,
        batch: String? = null,
        result: String? = null
    ): List<AnalysisResult>

    /**
     * 분석 결과 삭제
     */
    suspend fun deleteById(id: AnalysisId): Boolean

    /**
     * 분석 결과 존재 여부 확인
     */
    suspend fun existsById(id: AnalysisId): Boolean
}
