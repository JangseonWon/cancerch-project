package com.greencross.lims.data;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_= {@JsOverlay, @JsIgnore})
@Getter(onMethod_= {@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Request {
    private Double sample;
    private String info;
    private String service;
    private String dateRequest;
    private String dateEnd;
    private String type;
    private String remark;
    private String patient;
    private String customerName;
    private String mrn;
    private String name;
    private String code;
    private String sex;
    private String serviceName;
}
