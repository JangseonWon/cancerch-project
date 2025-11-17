package com.idrsys.ailis.cancerch.adapter.repository

import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.model.WorklistStatus
import com.idrsys.ailis.cancerch.domain.model.WorklistUuid
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * In-Memory Worklist Repository 구현
 *
 * 개발/테스트용 간단한 구현
 * 실제 운영에서는 R2DBC + jOOQ 구현으로 교체
 */
@Repository
class InMemoryWorklistRepository : WorklistRepository {

    private val storage = ConcurrentHashMap<Long, Worklist>()
    private val idGenerator = AtomicLong(1)

    override suspend fun findById(id: WorklistId): Worklist? {
        return storage[id.value]
    }

    override suspend fun findByUuid(uuid: WorklistUuid): Worklist? {
        return storage.values.find { it.uuid == uuid }
    }

    override suspend fun save(worklist: Worklist): Worklist {
        val id = if (worklist.id.value == 0L) {
            WorklistId(idGenerator.getAndIncrement())
        } else {
            worklist.id
        }

        val saved = worklist.copy(id = id)
        storage[id.value] = saved
        return saved
    }

    override suspend fun delete(id: WorklistId) {
        storage.remove(id.value)
    }

    override suspend fun findByStatus(status: WorklistStatus, limit: Int): List<Worklist> {
        return storage.values
            .filter { it.status == status }
            .take(limit)
            .sortedByDescending { it.createdAt }
    }

    override suspend fun findAll(offset: Int, limit: Int): List<Worklist> {
        return storage.values
            .sortedByDescending { it.createdAt }
            .drop(offset)
            .take(limit)
    }

    override suspend fun count(): Long {
        return storage.size.toLong()
    }

    override suspend fun existsById(id: WorklistId): Boolean {
        return storage.containsKey(id.value)
    }
}
