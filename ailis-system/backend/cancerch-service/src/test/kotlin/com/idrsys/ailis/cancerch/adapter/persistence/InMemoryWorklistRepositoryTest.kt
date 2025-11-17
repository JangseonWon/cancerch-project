package com.idrsys.ailis.cancerch.adapter.persistence

import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.idrsys.ailis.cancerch.domain.valueobject.WorklistId
import com.idrsys.ailis.cancerch.domain.valueobject.WorklistStatus
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*

class InMemoryWorklistRepositoryTest {

    private lateinit var repository: InMemoryWorklistRepository

    @BeforeEach
    fun setup() {
        repository = InMemoryWorklistRepository()
    }

    @Test
    fun `save should persist worklist and assign id`() = runTest {
        // given
        val worklist = Worklist.create(
            name = "Test Worklist",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )

        // when
        val saved = repository.save(worklist)

        // then
        assertTrue(saved.id.value > 0)
        assertEquals(worklist.name, saved.name)
    }

    @Test
    fun `findById should return worklist when exists`() = runTest {
        // given
        val worklist = Worklist.create(
            name = "Test Worklist",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )
        val saved = repository.save(worklist)

        // when
        val found = repository.findById(saved.id)

        // then
        assertNotNull(found)
        assertEquals(saved.id, found.id)
        assertEquals(saved.name, found.name)
    }

    @Test
    fun `findById should return null when not exists`() = runTest {
        // when
        val found = repository.findById(WorklistId(999))

        // then
        assertNull(found)
    }

    @Test
    fun `findByUuid should return worklist when exists`() = runTest {
        // given
        val worklist = Worklist.create(
            name = "Test Worklist",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )
        val saved = repository.save(worklist)

        // when
        val found = repository.findByUuid(saved.uuid)

        // then
        assertNotNull(found)
        assertEquals(saved.uuid, found.uuid)
    }

    @Test
    fun `findByStatus should return worklists with matching status`() = runTest {
        // given
        val worklist1 = Worklist.create("WL1", "WL", 1, "admin")
        val worklist2 = Worklist.create("WL2", "WL", 2, "admin")
        val worklist3 = Worklist.create("WL3", "WL", 3, "admin")
            .copy(status = WorklistStatus.IN_PROGRESS)

        repository.save(worklist1)
        repository.save(worklist2)
        repository.save(worklist3)

        // when
        val pending = repository.findByStatus(WorklistStatus.PENDING, 10)
        val inProgress = repository.findByStatus(WorklistStatus.IN_PROGRESS, 10)

        // then
        assertEquals(2, pending.size)
        assertEquals(1, inProgress.size)
        assertTrue(pending.all { it.status == WorklistStatus.PENDING })
        assertTrue(inProgress.all { it.status == WorklistStatus.IN_PROGRESS })
    }

    @Test
    fun `findByStatus should respect limit parameter`() = runTest {
        // given
        repeat(5) { index ->
            repository.save(Worklist.create("WL$index", "WL", index, "admin"))
        }

        // when
        val result = repository.findByStatus(WorklistStatus.PENDING, 3)

        // then
        assertEquals(3, result.size)
    }

    @Test
    fun `findAll should return all worklists with pagination`() = runTest {
        // given
        repeat(10) { index ->
            repository.save(Worklist.create("WL$index", "WL", index, "admin"))
        }

        // when
        val page1 = repository.findAll(0, 5)
        val page2 = repository.findAll(1, 5)

        // then
        assertEquals(5, page1.size)
        assertEquals(5, page2.size)
    }

    @Test
    fun `update should modify existing worklist`() = runTest {
        // given
        val worklist = Worklist.create("Original", "WL", 1, "admin")
        val saved = repository.save(worklist)
        val updated = saved.copy(name = "Updated")

        // when
        repository.save(updated)
        val found = repository.findById(saved.id)

        // then
        assertNotNull(found)
        assertEquals("Updated", found.name)
    }

    @Test
    fun `delete should remove worklist`() = runTest {
        // given
        val worklist = Worklist.create("Test", "WL", 1, "admin")
        val saved = repository.save(worklist)

        // when
        repository.delete(saved.id)
        val found = repository.findById(saved.id)

        // then
        assertNull(found)
    }
}
