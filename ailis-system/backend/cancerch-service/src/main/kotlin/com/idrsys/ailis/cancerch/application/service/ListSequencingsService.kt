package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.ListSequencingsQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedSequencingResponse
import com.idrsys.ailis.cancerch.application.usecase.ListSequencingsUseCase
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingRepository
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingState
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListSequencingsService(
    private val sequencingRepository: SequencingRepository
) : ListSequencingsUseCase {

    @Transactional(readOnly = true)
    override suspend fun execute(query: ListSequencingsQuery): PagedSequencingResponse {
        val items = if (query.worklistId != null) {
            // Worklist ID로 필터링
            sequencingRepository.findByWorklistId(WorklistId.from(query.worklistId))
        } else if (query.state != null) {
            // 상태로 필터링
            val state = SequencingState.valueOf(query.state)
            sequencingRepository.findByState(state, query.size)
        } else {
            // 전체 조회
            val offset = query.page * query.size
            sequencingRepository.findAll(offset, query.size)
        }

        val total = sequencingRepository.count()

        return PagedSequencingResponse.from(
            items = items,
            total = total,
            page = query.page,
            size = query.size
        )
    }
}
