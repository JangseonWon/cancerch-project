package com.idrsys.ailis.cancerch.domain.model

/**
 * Worklist에 포함된 Sample (Value Object)
 */
data class WorkSample(
    val sampleId: SampleId,
    val barcode: SampleBarcode,
    val serviceCode: String,
    val rowNumber: Int
) {
    init {
        require(serviceCode.isNotBlank()) { "Service code cannot be blank" }
        require(rowNumber > 0) { "Row number must be positive" }
    }
}
