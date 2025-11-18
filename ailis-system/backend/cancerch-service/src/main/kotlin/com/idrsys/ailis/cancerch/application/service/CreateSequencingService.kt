package com.idrsys.ailis.cancerch.application.service

import com.idrsys.ailis.cancerch.application.dto.request.CreateSequencingCommand
import com.idrsys.ailis.cancerch.application.dto.response.SequencingResponse
import com.idrsys.ailis.cancerch.application.usecase.CreateSequencingUseCase
import com.idrsys.ailis.cancerch.domain.exception.DomainException
import com.idrsys.ailis.cancerch.domain.model.SampleId
import com.idrsys.ailis.cancerch.domain.model.WorklistId
import com.idrsys.ailis.cancerch.domain.sequencing.Sequencing
import com.idrsys.ailis.cancerch.domain.sequencing.SequencingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateSequencingService(
    private val sequencingRepository: SequencingRepository
) : CreateSequencingUseCase {

    @Transactional
    override suspend fun execute(command: CreateSequencingCommand): SequencingResponse {
        // 1. 중복 체크
        if (sequencingRepository.existsByBarcode(command.barcode)) {
            throw DomainException("Sequencing with barcode ${command.barcode} already exists")
        }

        // 2. 도메인 모델 생성
        val sequencing = Sequencing.create(
            worklistId = WorklistId.from(command.worklistId),
            index = command.index,
            sampleId = SampleId.from(command.sampleId),
            barcode = command.barcode,
            indexName = command.indexName,
            qc = command.qc
        )

        // 3. 저장
        val saved = sequencingRepository.save(sequencing)

        // 4. DTO 변환
        return SequencingResponse.from(saved)
    }
}
