package com.idrsys.ailis.cancerch.adapter.repository

import com.idrsys.ailis.cancerch.domain.model.SampleId
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.sequencing.Sequencing
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingId
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingRepository
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingState
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingUuid
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * In-Memory Sequencing Repository 구현
 *
 * 개발/테스트용 간단한 구현
 */
@Repository
class InMemorySequencingRepository : SequencingRepository {

    private val storage = ConcurrentHashMap<Long, Sequencing>()
    private val idGenerator = AtomicLong(1)

    override suspend fun findById(id: SequencingId): Sequencing? {
        return storage[id.value]
    }

    override suspend fun findByUuid(uuid: SequencingUuid): Sequencing? {
        return storage.values.find { it.uuid == uuid }
    }

    override suspend fun findByWorklistId(worklistId: WorklistId): List<Sequencing> {
        return storage.values
            .filter { it.worklistId == worklistId }
            .sortedBy { it.index }
    }

    override suspend fun findBySampleId(sampleId: SampleId): Sequencing? {
        return storage.values.find { it.sampleId == sampleId }
    }

    override suspend fun findByBarcode(barcode: String): Sequencing? {
        return storage.values.find { it.barcode == barcode }
    }

    override suspend fun save(sequencing: Sequencing): Sequencing {
        val id = if (sequencing.id.value == 0L) {
            SequencingId(idGenerator.getAndIncrement())
        } else {
            sequencing.id
        }

        val saved = sequencing.copy(id = id)
        storage[id.value] = saved
        return saved
    }

    override suspend fun delete(id: SequencingId) {
        storage.remove(id.value)
    }

    override suspend fun findByState(state: SequencingState, limit: Int): List<Sequencing> {
        return storage.values
            .filter { it.state == state }
            .take(limit)
            .sortedByDescending { it.createdAt }
    }

    override suspend fun findAll(offset: Int, limit: Int): List<Sequencing> {
        return storage.values
            .sortedByDescending { it.createdAt }
            .drop(offset)
            .take(limit)
    }

    override suspend fun count(): Long {
        return storage.size.toLong()
    }

    override suspend fun existsById(id: SequencingId): Boolean {
        return storage.containsKey(id.value)
    }

    override suspend fun existsByBarcode(barcode: String): Boolean {
        return storage.values.any { it.barcode == barcode }
    }
}
