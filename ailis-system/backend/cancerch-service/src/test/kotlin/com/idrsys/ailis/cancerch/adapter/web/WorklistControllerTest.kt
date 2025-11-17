package com.idrsys.ailis.cancerch.adapter.web

import com.idrsys.ailis.cancerch.application.dto.response.WorklistResponse
import com.idrsys.ailis.cancerch.application.usecase.CreateWorklistUseCase
import com.idrsys.ailis.cancerch.application.usecase.GetWorklistUseCase
import com.idrsys.ailis.cancerch.application.usecase.ListWorklistsUseCase
import com.idrsys.ailis.cancerch.domain.exception.EntityNotFoundException
import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebFluxTest(WorklistController::class)
class WorklistControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var createWorklistUseCase: CreateWorklistUseCase

    @MockkBean
    private lateinit var getWorklistUseCase: GetWorklistUseCase

    @MockkBean
    private lateinit var listWorklistsUseCase: ListWorklistsUseCase

    @MockkBean
    private lateinit var updateWorklistUseCase: com.idrsys.ailis.cancerch.application.usecase.UpdateWorklistUseCase

    @Test
    fun `POST worklists should create new worklist`() = runTest {
        // given
        val request = CreateWorklistRequest(
            name = "Test Worklist",
            batchPrefix = "WL",
            batchIndex = 1,
            createdBy = "admin"
        )

        val worklist = Worklist.create(
            name = request.name,
            batchPrefix = request.batchPrefix,
            batchIndex = request.batchIndex,
            createdBy = request.createdBy
        )
        val response = WorklistResponse.from(worklist)

        coEvery { createWorklistUseCase.execute(any()) } returns response

        // when & then
        webTestClient.post()
            .uri("/api/worklists")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody<WorklistResponse>()
            .consumeWith { result ->
                assert(result.responseBody?.name == request.name)
                assert(result.responseBody?.status == "PENDING")
            }
    }

    @Test
    fun `GET worklists by id should return worklist when exists`() = runTest {
        // given
        val worklistId = 1L
        val worklist = Worklist.create("Test", "WL", 1, "admin")
        val response = WorklistResponse.from(worklist)

        coEvery { getWorklistUseCase.execute(any()) } returns response

        // when & then
        webTestClient.get()
            .uri("/api/worklists/{id}", worklistId)
            .exchange()
            .expectStatus().isOk
            .expectBody<WorklistResponse>()
            .consumeWith { result ->
                assert(result.responseBody?.name == "Test")
            }
    }

    @Test
    fun `GET worklists by id should return 404 when not found`() = runTest {
        // given
        val worklistId = 999L

        coEvery { getWorklistUseCase.execute(any()) } throws EntityNotFoundException("Worklist", worklistId.toString())

        // when & then
        webTestClient.get()
            .uri("/api/worklists/{id}", worklistId)
            .exchange()
            .expectStatus().isNotFound
            .expectBody<ErrorResponse>()
            .consumeWith { result ->
                assert(result.responseBody?.error == "Not Found")
            }
    }

    @Test
    fun `GET worklists should return paginated list`() = runTest {
        // given
        val worklist1 = Worklist.create("WL1", "WL", 1, "admin")
        val worklist2 = Worklist.create("WL2", "WL", 2, "admin")

        val pagedResponse = com.idrsys.ailis.cancerch.application.dto.response.PagedWorklistResponse(
            items = listOf(
                WorklistResponse.from(worklist1),
                WorklistResponse.from(worklist2)
            ),
            totalCount = 2,
            page = 0,
            size = 20,
            totalPages = 1
        )

        coEvery { listWorklistsUseCase.execute(any()) } returns pagedResponse

        // when & then
        webTestClient.get()
            .uri("/api/worklists?page=0&size=20")
            .exchange()
            .expectStatus().isOk
            .expectBody<com.idrsys.ailis.cancerch.application.dto.response.PagedWorklistResponse>()
            .consumeWith { result ->
                assert(result.responseBody?.items?.size == 2)
                assert(result.responseBody?.totalCount == 2L)
            }
    }

    @Test
    fun `PATCH worklists should update worklist`() = runTest {
        // given
        val worklistId = 1L
        val request = UpdateWorklistRequest(
            name = "Updated Name",
            updatedBy = "user1"
        )

        val updatedWorklist = Worklist.create("Original", "WL", 1, "admin")
            .copy(name = "Updated Name", updatedBy = "user1")
        val response = WorklistResponse.from(updatedWorklist)

        coEvery { updateWorklistUseCase.execute(any()) } returns response

        // when & then
        webTestClient.patch()
            .uri("/api/worklists/{id}", worklistId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody<WorklistResponse>()
            .consumeWith { result ->
                assert(result.responseBody?.name == "Updated Name")
            }
    }
}
