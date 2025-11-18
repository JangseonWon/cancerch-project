package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.ListPreprocessingsQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedPreprocessingResponse
import com.idrsys.ailis.cancerch.application.usecase.ListPreprocessingsUseCase
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingRepository
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingState
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListPreprocessingsService(
    private val preprocessingRepository: PreprocessingRepository
) : ListPreprocessingsUseCase {

    @Transactional(readOnly = true)
    override suspend fun execute(query: ListPreprocessingsQuery): PagedPreprocessingResponse {
        val offset = query.page * query.size

        // 상태 필터링이 있는 경우
        val items = if (query.state != null) {
            val state = PreprocessingState.valueOf(query.state)
            preprocessingRepository.findByState(state, offset, query.size)
        } else {
            preprocessingRepository.findAll(offset, query.size)
        }

        val total = preprocessingRepository.count()

        return PagedPreprocessingResponse.from(
            items = items,
            total = total,
            page = query.page,
            size = query.size
        )
    }
}
