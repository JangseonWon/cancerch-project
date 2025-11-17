package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.CreateWorklistCommand
import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import com.idrsys.ailis.cancerch.domain.service.WorklistDomainService
import com.idrsys.ailis.cancerch.domain.valueobject.WorklistStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CreateWorklistServiceTest {

    private lateinit var worklistRepository: WorklistRepository
    private lateinit var worklistDomainService: WorklistDomainService
    private lateinit var createWorklistService: CreateWorklistService

    @BeforeEach
    fun setup() {
        worklistRepository = mockk()
        worklistDomainService = WorklistDomainService()
        createWorklistService = CreateWorklistService(worklistRepository, worklistDomainService)
    }

    @Test
    fun `execute should create and save worklist`() = runTest {
        // given
        val command = CreateWorklistCommand(
            name = "Test Worklist",
            batchPrefix = "WL",
            batchIndex = 20250117001,
            createdBy = "admin"
        )

        val savedWorklist = Worklist.create(
            name = command.name,
            batchPrefix = command.batchPrefix,
            batchIndex = command.batchIndex,
            createdBy = command.createdBy
        )

        coEvery { worklistRepository.save(any()) } returns savedWorklist

        // when
        val response = createWorklistService.execute(command)

        // then
        assertEquals(command.name, response.name)
        assertEquals("WL-20250117001", response.batchNumber)
        assertEquals("PENDING", response.status)
        assertEquals(0, response.sampleCount)

        coVerify(exactly = 1) { worklistRepository.save(any()) }
    }

    @Test
    fun `execute should validate worklist before saving`() = runTest {
        // given
        val command = CreateWorklistCommand(
            name = "Test Worklist",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )

        val savedWorklist = Worklist.create(
            name = command.name,
            batchPrefix = command.batchPrefix,
            batchIndex = command.batchIndex,
            createdBy = command.createdBy
        )

        coEvery { worklistRepository.save(any()) } returns savedWorklist

        // when
        val response = createWorklistService.execute(command)

        // then
        assertEquals(command.name, response.name)
        coVerify(exactly = 1) { worklistRepository.save(match { worklist ->
            worklist.name == command.name &&
            worklist.batchPrefix == command.batchPrefix &&
            worklist.status == WorklistStatus.PENDING
        }) }
    }
}
