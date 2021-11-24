package com.greencross.lims.data;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Query {
    private int page;
    private int limit;
    @JsProperty(name="sort_by")
    private String sortBy;
    private boolean asc;
    private Filter[] filters;

    @JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
    @Setter(onMethod_={@JsOverlay, @JsIgnore})
    @Getter(onMethod_={@JsOverlay, @JsIgnore})
    @Accessors(fluent=true)
    public final static class Filter {
        private String key;
        private String value;
    }
}
