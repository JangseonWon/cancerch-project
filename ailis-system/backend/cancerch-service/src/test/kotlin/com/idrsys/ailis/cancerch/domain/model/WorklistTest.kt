package com.idrsys.ailis.cancerch.domain.model

import com.idrsys.ailis.cancerch.domain.exception.BusinessRuleViolationException
import com.idrsys.ailis.cancerch.domain.valueobject.SampleId
import com.idrsys.ailis.cancerch.domain.valueobject.WorklistId
import com.idrsys.ailis.cancerch.domain.valueobject.WorklistStatus
import com.idrsys.ailis.cancerch.domain.valueobject.WorklistUuid
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WorklistTest {

    @Test
    fun `create should create valid worklist`() {
        // given
        val name = "Test Worklist"
        val batchPrefix = "WL"
        val batchIndex = 20250117001
        val createdBy = "admin"

        // when
        val worklist = Worklist.create(
            name = name,
            batchPrefix = batchPrefix,
            batchIndex = batchIndex,
            createdBy = createdBy
        )

        // then
        assertEquals(name, worklist.name)
        assertEquals(batchPrefix, worklist.batchPrefix)
        assertEquals(batchIndex, worklist.batchIndex)
        assertEquals(WorklistStatus.PENDING, worklist.status)
        assertEquals(createdBy, worklist.createdBy)
        assertEquals(createdBy, worklist.updatedBy)
        assertTrue(worklist.samples.isEmpty())
        assertEquals(0, worklist.version)
    }

    @Test
    fun `getBatchNumber should return formatted batch number`() {
        // given
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 20250117001,
            createdBy = "admin"
        )

        // when
        val batchNumber = worklist.getBatchNumber()

        // then
        assertEquals("WL-20250117001", batchNumber)
    }

    @Test
    fun `changeStatus should update status when valid transition`() {
        // given
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )

        // when
        val updated = worklist.changeStatus(WorklistStatus.IN_PROGRESS, "user1")

        // then
        assertEquals(WorklistStatus.IN_PROGRESS, updated.status)
        assertEquals("user1", updated.updatedBy)
        assertEquals(1, updated.version)
    }

    @Test
    fun `changeStatus should throw exception when invalid transition`() {
        // given
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        ).copy(status = WorklistStatus.COMPLETED)

        // when & then
        assertThrows<BusinessRuleViolationException> {
            worklist.changeStatus(WorklistStatus.PENDING, "user1")
        }
    }

    @Test
    fun `addSample should add sample to worklist`() {
        // given
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )
        val sample = WorkSample(
            sampleId = SampleId("S001"),
            barcode = "BC001",
            serviceCode = "NGS001",
            rowNumber = 0
        )

        // when
        val updated = worklist.addSample(sample, "user1")

        // then
        assertEquals(1, updated.samples.size)
        assertEquals(sample, updated.samples[0])
        assertEquals("user1", updated.updatedBy)
        assertEquals(1, updated.version)
    }

    @Test
    fun `addSample should throw exception when sample already exists`() {
        // given
        val sample = WorkSample(
            sampleId = SampleId("S001"),
            barcode = "BC001",
            serviceCode = "NGS001",
            rowNumber = 0
        )
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        ).copy(samples = listOf(sample))

        // when & then
        assertThrows<BusinessRuleViolationException> {
            worklist.addSample(sample, "user1")
        }
    }

    @Test
    fun `removeSample should remove sample from worklist`() {
        // given
        val sample = WorkSample(
            sampleId = SampleId("S001"),
            barcode = "BC001",
            serviceCode = "NGS001",
            rowNumber = 0
        )
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        ).copy(samples = listOf(sample))

        // when
        val updated = worklist.removeSample(SampleId("S001"), "user1")

        // then
        assertTrue(updated.samples.isEmpty())
        assertEquals("user1", updated.updatedBy)
        assertEquals(1, updated.version)
    }

    @Test
    fun `hasSample should return true when sample exists`() {
        // given
        val sample = WorkSample(
            sampleId = SampleId("S001"),
            barcode = "BC001",
            serviceCode = "NGS001",
            rowNumber = 0
        )
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        ).copy(samples = listOf(sample))

        // when & then
        assertTrue(worklist.hasSample(SampleId("S001")))
        assertFalse(worklist.hasSample(SampleId("S002")))
    }
}
