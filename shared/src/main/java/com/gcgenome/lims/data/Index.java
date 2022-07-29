package com.gcgenome.lims.data;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Index {
    public String id;
    public String type;
    public String sequence;
    public String plate;
    public String position;
}
