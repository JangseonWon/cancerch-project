package com.gcgenome.lims.data;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_= {@JsOverlay, @JsIgnore})
@Getter(onMethod_= {@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Preprocessing {
    private String id;
    private String lastModifyBy;
    private String lastModifyAt;
    private String createdBy;
    private String createdAt;
    private Double concNa;
    private Double concInput;
    private Double libPrep;
    private Double libConcTape;
    private Double libConcQubit;
    private Double fragmentSize;
    private Double amount;
    private Double dilution;
    private Double volume;
    private Double libVolume;
    private Double bufferVolume;
    private String indexI7;
    private String sequenceI7;
    private String indexI5;
    private String sequenceI5;
    private String json;
}
