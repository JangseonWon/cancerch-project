package com.idrsys.ailis.cancerch.domain.sequencing

import java.util.UUID

/**
 * Sequencing ID (Value Object)
 */
data class SequencingId(val value: Long) {
    init {
        require(value >= 0) { "Sequencing ID must be non-negative" }
    }

    companion object {
        fun generate(): SequencingId = SequencingId(0L)
        fun from(value: Long): SequencingId = SequencingId(value)
    }
}

/**
 * Sequencing UUID (Value Object)
 */
data class SequencingUuid(val value: String) {
    init {
        require(value.isNotBlank()) { "Sequencing UUID cannot be blank" }
    }

    companion object {
        fun generate(): SequencingUuid = SequencingUuid(UUID.randomUUID().toString())
        fun from(value: String): SequencingUuid = SequencingUuid(value)
    }
}
