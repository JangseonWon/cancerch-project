package com.gcgenome.alis

import java.io.Serializable

data class State(
    val request: Request,
    val state: String,
    val member: String?,
    val machine: String?
) : Serializable {
}