package com.idrsys.ailis.cancerch.application.dto.request

/**
 * Worklist 조회 Query
 */
data class GetWorklistQuery(
    val id: Long
)

/**
 * Worklist 목록 조회 Query
 */
data class ListWorklistsQuery(
    val status: String? = null,
    val page: Int = 0,
    val size: Int = 20
) {
    val offset: Int get() = page * size
}
