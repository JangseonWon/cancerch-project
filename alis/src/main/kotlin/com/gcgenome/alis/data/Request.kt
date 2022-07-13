package com.gcgenome.alis.data

import java.io.Serializable
import java.time.LocalDate

data class Request(
    val requestDate: LocalDate = LocalDate.now(),
    val requestNo: Int = 0,
    val itemCode: String = ""
) : Serializable {
}