package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.ListPreprocessingsQuery
import com.idrsys.ailis.cancerch.application.dto.response.PagedPreprocessingResponse
import com.idrsys.ailis.cancerch.application.usecase.ListPreprocessingsUseCase
import com.idrsys.ailis.cancerch.domain.repository.WorklistRepository
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingRepository
import com.idrsys.ailis.cancerch.domain.sequencing.PreprocessingState
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListPreprocessingsService(
    private val preprocessingRepository: PreprocessingRepository,
    private val worklistRepository: WorklistRepository
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

        // 각 Preprocessing에 해당하는 Worklist 정보 조회
        val worklistIds = items.map { it.worklistId }.distinct()
        val worklistMap = mutableMapOf<Long, com.idrsys.ailis.cancerch.domain.model.Worklist>()

        for (worklistId in worklistIds) {
            val worklist = worklistRepository.findById(worklistId)
            if (worklist != null) {
                worklistMap[worklistId.value] = worklist
            }
        }

        return PagedPreprocessingResponse.from(
            items = items,
            worklistMap = worklistMap,
            total = total,
            page = query.page,
            size = query.size
        )
    }
}
