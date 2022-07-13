package com.gcgenome.alis

import java.io.Serializable
import java.time.LocalDate

data class Request(
    val requestDate: LocalDate,
    val requestNo: Int,
    val service: String
) : Serializable
