package com.idrsys.ailis.cancerch.domain.service

import com.idrsys.ailis.cancerch.domain.model.WorkSample
import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.idrsys.ailis.cancerch.domain.valueobject.SampleId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WorklistDomainServiceTest {

    private lateinit var domainService: WorklistDomainService

    @BeforeEach
    fun setup() {
        domainService = WorklistDomainService()
    }

    @Test
    fun `validate should return success for valid worklist`() {
        // given
        val worklist = Worklist.create(
            name = "Valid Worklist",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )

        // when
        val result = domainService.validate(worklist)

        // then
        assertTrue(result.isSuccess)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `validate should fail when name is empty`() {
        // given
        val worklist = Worklist.create(
            name = "",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )

        // when
        val result = domainService.validate(worklist)

        // then
        assertFalse(result.isSuccess)
        assertTrue(result.errors.any { it.contains("Worklist name cannot be empty") })
    }

    @Test
    fun `validate should fail when batch prefix is empty`() {
        // given
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "",
            batchIndex = 1,
            createdBy = "admin"
        )

        // when
        val result = domainService.validate(worklist)

        // then
        assertFalse(result.isSuccess)
        assertTrue(result.errors.any { it.contains("Batch prefix cannot be empty") })
    }

    @Test
    fun `validate should fail when batch index is negative`() {
        // given
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = -1,
            createdBy = "admin"
        )

        // when
        val result = domainService.validate(worklist)

        // then
        assertFalse(result.isSuccess)
        assertTrue(result.errors.any { it.contains("Batch index must be non-negative") })
    }

    @Test
    fun `validate should fail when too many samples`() {
        // given
        val samples = (0..96).map { index ->
            WorkSample(
                sampleId = SampleId("S${index.toString().padStart(3, '0')}"),
                barcode = "BC${index.toString().padStart(3, '0')}",
                serviceCode = "NGS001",
                rowNumber = index
            )
        }
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        ).copy(samples = samples)

        // when
        val result = domainService.validate(worklist)

        // then
        assertFalse(result.isSuccess)
        assertTrue(result.errors.any { it.contains("cannot contain more than 96 samples") })
    }

    @Test
    fun `validate should fail when duplicate samples exist`() {
        // given
        val duplicateSample = WorkSample(
            sampleId = SampleId("S001"),
            barcode = "BC001",
            serviceCode = "NGS001",
            rowNumber = 0
        )
        val samples = listOf(duplicateSample, duplicateSample.copy(barcode = "BC002", rowNumber = 1))
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        ).copy(samples = samples)

        // when
        val result = domainService.validate(worklist)

        // then
        assertFalse(result.isSuccess)
        assertTrue(result.errors.any { it.contains("Duplicate sample IDs found") })
    }

    @Test
    fun `validate should accumulate multiple errors`() {
        // given
        val worklist = Worklist.create(
            name = "",
            batchPrefix = "",
            batchIndex = -1,
            createdBy = "admin"
        )

        // when
        val result = domainService.validate(worklist)

        // then
        assertFalse(result.isSuccess)
        assertTrue(result.errors.size >= 3)
    }

    @Test
    fun `canTransitionTo should return true for valid transitions`() {
        // given
        val worklist = Worklist.create(
            name = "Test",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )

        // when & then
        assertTrue(domainService.canTransitionTo(worklist, com.idrsys.ailis.cancerch.domain.valueobject.WorklistStatus.IN_PROGRESS))
        assertTrue(domainService.canTransitionTo(worklist, com.idrsys.ailis.cancerch.domain.valueobject.WorklistStatus.CANCELLED))
        assertFalse(domainService.canTransitionTo(worklist, com.idrsys.ailis.cancerch.domain.valueobject.WorklistStatus.COMPLETED))
    }
}
