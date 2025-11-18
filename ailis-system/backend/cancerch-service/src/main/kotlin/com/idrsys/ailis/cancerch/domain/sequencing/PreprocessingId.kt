package com.idrsys.ailis.cancerch.domain.sequencing

import java.util.UUID

/**
 * Preprocessing ID (Value Object)
 */
data class PreprocessingId(val value: Long) {
    init {
        require(value >= 0) { "Preprocessing ID must be non-negative" }
    }

    companion object {
        fun generate(): PreprocessingId = PreprocessingId(0L)
        fun from(value: Long): PreprocessingId = PreprocessingId(value)
    }
}

/**
 * Preprocessing UUID (Value Object)
 */
data class PreprocessingUuid(val value: String) {
    init {
        require(value.isNotBlank()) { "Preprocessing UUID cannot be blank" }
    }

    companion object {
        fun generate(): PreprocessingUuid = PreprocessingUuid(UUID.randomUUID().toString())
        fun from(value: String): PreprocessingUuid = PreprocessingUuid(value)
    }
}
