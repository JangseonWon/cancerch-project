package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.GetSequencingQuery
import com.idrsys.ailis.cancerch.application.dto.response.SequencingResponse
import com.idrsys.ailis.cancerch.application.usecase.GetSequencingUseCase
import com.idrsys.ailis.cancerch.domain.exception.DomainException
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingId
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetSequencingService(
    private val sequencingRepository: SequencingRepository
) : GetSequencingUseCase {

    @Transactional(readOnly = true)
    override suspend fun execute(query: GetSequencingQuery): SequencingResponse {
        val sequencing = sequencingRepository.findById(SequencingId.from(query.id))
            ?: throw DomainException("Sequencing not found: ${query.id}")

        return SequencingResponse.from(sequencing)
    }
}
