package com.idrsys.ailis.cancerch.domain.model

import java.util.UUID

/**
 * Worklist 식별자 (Value Object)
 */
@JvmInline
value class WorklistId(val value: Long) {
    init {
        require(value > 0) { "Worklist ID must be positive" }
    }

    companion object {
        fun generate(): WorklistId {
            // 실제로는 DB 시퀀스에서 생성
            return WorklistId(System.currentTimeMillis())
        }

        fun from(value: Long): WorklistId = WorklistId(value)
    }
}

/**
 * Worklist UUID (레거시 호환용)
 */
@JvmInline
value class WorklistUuid(val value: UUID) {
    companion object {
        fun generate(): WorklistUuid = WorklistUuid(UUID.randomUUID())

        fun fromString(uuid: String): WorklistUuid = WorklistUuid(UUID.fromString(uuid))
    }

    override fun toString(): String = value.toString()
}
