package com.gcgenome.lims.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchParam (
    @JsonProperty Integer page,
    @JsonProperty Integer limit,
    @JsonProperty("sort_by") String sortBy,
    @JsonProperty Boolean asc,
    @JsonProperty List<Filter> filters
) {
    public static Object Companion;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Filter(
        @JsonProperty String key,
        @JsonProperty String value
    ) { }
}
