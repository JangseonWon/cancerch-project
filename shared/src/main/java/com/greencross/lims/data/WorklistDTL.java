package com.greencross.lims.data;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_= {@JsOverlay, @JsIgnore})
@Getter(onMethod_= {@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class WorklistDTL {
    private String id;
    private Double no;
    private String title;
    private Double sample;
    private String comment;
    private String state;
    @JsProperty(name="created_by")
    private String createdBy;
    @JsProperty(name="created_at")
    private String createdAt;
}
