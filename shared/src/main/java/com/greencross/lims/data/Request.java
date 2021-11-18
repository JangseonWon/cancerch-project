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
    @JsProperty(name = "date_request")
    private String dateRequest;
    @JsProperty(name = "date_end")
    private String dateEnd;
    private String type;
    private String remark;
    private Double barcode;
    private String patient;
    @JsProperty(name = "customer_name")
    private String customerName;
    @JsProperty(name = "customer_code")
    private String customerCode;
    private String mrn;
    private String name;
    private String code;
    private String sex;
    @JsProperty(name = "service_name")
    private String serviceName;
}
