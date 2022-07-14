package com.gcgenome.alis.data

import java.io.Serializable

data class State(
    val request: Request,
    val state: String,
    val member: String?,
    val machine: String?
) : Serializable