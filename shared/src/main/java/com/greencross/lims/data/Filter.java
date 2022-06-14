package com.greencross.lims.data;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Accessors(fluent=true)
public final class Filter {
    public String key;
    public String value;
    @JsOverlay
    public static Filter build(String key, String value) {
        Filter filter = new Filter();
        filter.key = key;
        filter.value = value;
        return filter;
    }
}
