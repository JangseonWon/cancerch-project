package com.idrsys.ailis.cancerch.domain.sequencing

/**
 * PlateIndex Value Object
 *
 * Plate 위치와 Index 정보를 나타내는 값 객체
 */
data class PlateIndex(
    val plate: String,
    val well: String,
    val indexName: String,
    val sequence: String
) {
    init {
        require(plate.isNotBlank()) { "Plate cannot be blank" }
        require(well.isNotBlank()) { "Well cannot be blank" }
        require(indexName.isNotBlank()) { "Index name cannot be blank" }
        require(sequence.isNotBlank()) { "Sequence cannot be blank" }
        require(well.matches(Regex("^[A-H][0-9]{1,2}$"))) {
            "Well must be in format A1-H12 (e.g., A1, B10, H12)"
        }
    }

    /**
     * Well의 Row (A-H)
     */
    fun row(): Char = well[0]

    /**
     * Well의 Column (1-12)
     */
    fun column(): Int = well.substring(1).toInt()

    /**
     * Well 번호 (A1=1, A2=2, ..., H12=96)
     */
    fun wellNumber(): Int {
        val rowNum = row() - 'A' + 1
        val colNum = column()
        return (colNum - 1) * 8 + rowNum
    }

    companion object {
        /**
         * Well 번호로부터 PlateIndex 생성
         */
        fun fromWellNumber(
            plate: String,
            wellNumber: Int,
            indexName: String,
            sequence: String
        ): PlateIndex {
            require(wellNumber in 1..96) { "Well number must be between 1 and 96" }

            val rowNum = ((wellNumber - 1) % 8) + 1
            val colNum = ((wellNumber - 1) / 8) + 1
            val row = ('A'.code + rowNum - 1).toChar()
            val well = "$row$colNum"

            return PlateIndex(
                plate = plate,
                well = well,
                indexName = indexName,
                sequence = sequence
            )
        }

        /**
         * Row와 Column으로부터 PlateIndex 생성
         */
        fun fromRowColumn(
            plate: String,
            row: Char,
            column: Int,
            indexName: String,
            sequence: String
        ): PlateIndex {
            require(row in 'A'..'H') { "Row must be A-H" }
            require(column in 1..12) { "Column must be 1-12" }

            return PlateIndex(
                plate = plate,
                well = "$row$column",
                indexName = indexName,
                sequence = sequence
            )
        }
    }
}
