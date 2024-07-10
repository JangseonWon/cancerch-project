package com.gcgenome.lims.data;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class LinkRequest {
    @JsProperty(name="origin_sample")
    private String originSample;
    @JsProperty(name="origin_service")
    private String originService;
    private String batch;
    private String row;
    @JsProperty(name="link_sample")
    private String linkSample;
    @JsProperty(name="link_service")
    private String linkService;
}
