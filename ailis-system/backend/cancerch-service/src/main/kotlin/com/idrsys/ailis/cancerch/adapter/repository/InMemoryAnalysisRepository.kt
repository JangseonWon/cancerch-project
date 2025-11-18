package com.idrsys.ailis.cancerch.adapter.repository

import com.idrsys.ailis.cancerch.domain.model.AnalysisId
import com.idrsys.ailis.cancerch.domain.model.AnalysisResult
import com.idrsys.ailis.cancerch.domain.repository.AnalysisRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

/**
 * InMemory 기반 AnalysisRepository 구현체
 *
 * 개발 및 테스트 용도의 인메모리 저장소
 */
@Repository
class InMemoryAnalysisRepository : AnalysisRepository {

    private val storage = ConcurrentHashMap<AnalysisId, AnalysisResult>()
    private val mutex = Mutex()

    override suspend fun save(analysisResult: AnalysisResult): AnalysisResult = mutex.withLock {
        storage[analysisResult.id] = analysisResult
        analysisResult
    }

    override suspend fun findById(id: AnalysisId): AnalysisResult? {
        return storage[id]
    }

    override suspend fun findAll(): List<AnalysisResult> {
        return storage.values.toList()
    }

    override suspend fun findBySampleId(sampleId: String): List<AnalysisResult> {
        return storage.values.filter { it.id.sampleId == sampleId }
    }

    override suspend fun findByBatch(batch: String): List<AnalysisResult> {
        return storage.values.filter { it.id.batch == batch }
    }

    override suspend fun findByServiceCode(serviceCode: String): List<AnalysisResult> {
        return storage.values.filter { it.id.serviceCode == serviceCode }
    }

    override suspend fun search(
        sampleId: String?,
        serviceCode: String?,
        batch: String?,
        result: String?
    ): List<AnalysisResult> {
        return storage.values.filter { analysisResult ->
            (sampleId == null || analysisResult.id.sampleId.contains(sampleId, ignoreCase = true)) &&
            (serviceCode == null || analysisResult.id.serviceCode.equals(serviceCode, ignoreCase = true)) &&
            (batch == null || analysisResult.id.batch.contains(batch, ignoreCase = true)) &&
            (result == null || analysisResult.result?.contains(result, ignoreCase = true) == true)
        }.sortedByDescending { it.updatedAt }
    }

    override suspend fun deleteById(id: AnalysisId): Boolean = mutex.withLock {
        storage.remove(id) != null
    }

    override suspend fun existsById(id: AnalysisId): Boolean {
        return storage.containsKey(id)
    }

    /**
     * 테스트 용도: 모든 데이터 삭제
     */
    suspend fun clear() = mutex.withLock {
        storage.clear()
    }

    /**
     * 테스트 용도: 데이터 개수 조회
     */
    fun count(): Int = storage.size
}
