package com.gcgenome.lims.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Search (
    @JsonProperty("page")
    val page: Int? = 0,
    @JsonProperty("limit")
    val limit: Int? = 0,
    @JsonProperty("sort_by")
    val sortBy: String? = null,
    @JsonProperty("asc")
    val asc: Boolean? = false,
    @JsonProperty("filters")
    val filters: MutableList<Filter> = mutableListOf()
) {
    companion object {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Filter(
            @JsonProperty("key")
            val key: String? = null,
            @JsonProperty("value")
            val value: String? = null
        )
    }
}
