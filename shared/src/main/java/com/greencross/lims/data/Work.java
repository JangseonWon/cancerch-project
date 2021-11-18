package com.greencross.lims.data;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_= {@JsOverlay, @JsIgnore})
@Getter(onMethod_= {@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Work {
    private String id;
    private Double no;
    private String batch;
    private Double sample;
    private Double barcode;
    @JsProperty(name = "test_type")
    private String testType;
    @JsProperty(name = "test_code")
    private String testCode;
    private String reqNum;
    @JsProperty(name = "patient_name")
    private String patientName;
    @JsProperty(name = "custermer_name")
    private String custermerName;
    @JsProperty(name = "custermer_code")
    private String custermerCode;
    @JsProperty(name = "end_dt")
    private String endDt;

    public Work(){}
//    private Double prepKit;
//    private String method;
//    private String extractDt;
//    private Double volume;
//    private Double totalAmount;
//    private Double libraryConc;
//    private Double fragmentSize;
//    private Double mol;
//    private Double batchPersample;
//    private Double pollVol;
//    private Double multiple;
//    private Double finalPoolingVol;
//    private Double elutionVol;
//    private String result;
//    private String failReason;
//    private String comment;
}
