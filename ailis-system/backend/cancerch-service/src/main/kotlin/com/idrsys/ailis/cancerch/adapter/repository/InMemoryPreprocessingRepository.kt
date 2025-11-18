package com.idrsys.ailis.cancerch.adapter.repository

import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.sequencing.Preprocessing
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingId
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingRepository
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingState
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingUuid
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * In-Memory Preprocessing Repository 구현
 *
 * 개발/테스트용 간단한 구현
 */
@Repository
class InMemoryPreprocessingRepository : PreprocessingRepository {

    private val storage = ConcurrentHashMap<Long, Preprocessing>()
    private val idGenerator = AtomicLong(1)

    override suspend fun findById(id: PreprocessingId): Preprocessing? {
        return storage[id.value]
    }

    override suspend fun findByUuid(uuid: PreprocessingUuid): Preprocessing? {
        return storage.values.find { it.uuid == uuid }
    }

    override suspend fun findByWorklistId(worklistId: WorklistId): Preprocessing? {
        return storage.values.find { it.worklistId == worklistId }
    }

    override suspend fun save(preprocessing: Preprocessing): Preprocessing {
        val id = if (preprocessing.id.value == 0L) {
            PreprocessingId(idGenerator.getAndIncrement())
        } else {
            preprocessing.id
        }

        val saved = preprocessing.copy(id = id)
        storage[id.value] = saved
        return saved
    }

    override suspend fun delete(id: PreprocessingId) {
        storage.remove(id.value)
    }

    override suspend fun findByState(state: PreprocessingState, limit: Int): List<Preprocessing> {
        return storage.values
            .filter { it.state == state }
            .take(limit)
            .sortedByDescending { it.createdAt }
    }

    override suspend fun findAll(offset: Int, limit: Int): List<Preprocessing> {
        return storage.values
            .sortedByDescending { it.createdAt }
            .drop(offset)
            .take(limit)
    }

    override suspend fun count(): Long {
        return storage.size.toLong()
    }

    override suspend fun existsById(id: PreprocessingId): Boolean {
        return storage.containsKey(id.value)
    }

    override suspend fun existsByWorklistId(worklistId: WorklistId): Boolean {
        return storage.values.any { it.worklistId == worklistId }
    }
}
