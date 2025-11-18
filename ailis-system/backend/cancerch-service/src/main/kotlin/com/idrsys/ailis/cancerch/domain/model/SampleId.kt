package com.idrsys.ailis.cancerch.domain.model

/**
 * Sample 식별자 (Value Object)
 */
@JvmInline
value class SampleId(val value: Long) {
    init {
        require(value > 0) { "Sample ID must be positive" }
    }

    companion object {
        fun from(value: Long): SampleId = SampleId(value)
    }
}

/**
 * Sample 바코드
 */
@JvmInline
value class SampleBarcode(val value: String) {
    init {
        require(value.isNotBlank()) { "Sample barcode cannot be blank" }
        require(value.length <= 50) { "Sample barcode too long" }
    }
}
