package com.greencross.lims.data

import reactor.core.publisher.Flux
import java.io.Serializable
import kotlin.math.ceil

data class PageReactive<T>(
    val totalElements: Long,
    val pageSize: Int,
    val currentPage: Int,
    val data: Flux<T>
): Serializable {
    fun totalPages(): Long {
        return ceil(totalElements / pageSize.toDouble()).toLong()
    }
}
