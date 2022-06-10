package com.gcgenome.lims.search;

import reactor.core.publisher.Flux;
import java.io.Serializable;
import java.util.function.Function;

public record PageReactive<T>(
        long totalElements,
        Integer pageSize,
        Integer currentPage,
        Flux<T> data
) implements Serializable {
    public Long totalPages() {
        if(pageSize == null) return null;
        return (long) Math.ceil(totalElements / pageSize.doubleValue());
    }
    public <F> PageReactive<F> map(Function<T, F> func) {
        return new PageReactive<F>(totalElements, pageSize, currentPage, data.map(func));
    }
}
