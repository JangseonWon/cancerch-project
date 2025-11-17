package com.idrsys.ailis.cancerch.domain.service

import com.idrsys.ailis.cancerch.domain.exception.BusinessRuleViolationException
import com.idrsys.ailis.cancerch.domain.model.Worklist
import com.idrsys.ailis.cancerch.domain.model.WorklistStatus

/**
 * Worklist 도메인 서비스
 *
 * 여러 엔티티에 걸친 비즈니스 로직 처리
 */
class WorklistDomainService {
    /**
     * Worklist 검증
     */
    fun validate(worklist: Worklist): ValidationResult {
        val errors = mutableListOf<String>()

        // 샘플 수 검증 (최대 96개 - 96 well plate)
        if (worklist.samples.size > 96) {
            errors.add("Worklist cannot contain more than 96 samples")
        }

        // 중복 샘플 검증
        val duplicateSamples = worklist.samples
            .groupBy { it.sampleId }
            .filter { it.value.size > 1 }
            .keys

        if (duplicateSamples.isNotEmpty()) {
            errors.add("Duplicate samples found: $duplicateSamples")
        }

        // Row 번호 검증 (1-96)
        val invalidRows = worklist.samples
            .filter { it.rowNumber < 1 || it.rowNumber > 96 }

        if (invalidRows.isNotEmpty()) {
            errors.add("Invalid row numbers: ${invalidRows.map { it.rowNumber }}")
        }

        return if (errors.isEmpty()) {
            ValidationResult.success()
        } else {
            ValidationResult.failure(errors)
        }
    }

    /**
     * Worklist 완료 가능 여부 확인
     */
    fun canComplete(worklist: Worklist): Boolean {
        return worklist.status == WorklistStatus.IN_PROGRESS &&
                worklist.samples.isNotEmpty()
    }

    /**
     * Worklist 시작 가능 여부 확인
     */
    fun canStart(worklist: Worklist): Boolean {
        return worklist.status == WorklistStatus.PENDING &&
                worklist.samples.isNotEmpty()
    }
}

/**
 * 검증 결과
 */
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
) {
    fun throwIfInvalid() {
        if (!isValid) {
            throw BusinessRuleViolationException(errors.joinToString("; "))
        }
    }

    companion object {
        fun success() = ValidationResult(true)
        fun failure(errors: List<String>) = ValidationResult(false, errors)
    }
}
