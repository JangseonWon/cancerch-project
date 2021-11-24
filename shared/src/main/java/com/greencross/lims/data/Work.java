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
    private Double no;
    private String id;
    private Double barcode;
    private String batch;
    @JsProperty(name = "req_num")
    private String reqNum;
    @JsProperty(name = "test_type")
    private String testType;
    @JsProperty(name = "test_code")
    private String testCode;
    @JsProperty(name = "pat_name")
    private String patName;
    private Double sample;
    @JsProperty(name = "cus_name")
    private String cusName;
    @JsProperty(name = "cus_code")
    private String cusCode;
    @JsProperty(name = "end_dt")
    private String endDt;
    @JsProperty(name = "dna_prep")
    private String dnaPrep;
    @JsProperty(name = "dna_method")
    private String dnaMethod;
    @JsProperty(name = "ext_dt")
    private String extDt;
    @JsProperty(name = "dna_conc")
    private Double dnaConc;
    @JsProperty(name = "dna_vol")
    private Double dnaVol;
    @JsProperty(name = "dw_vol")
    private Double dwVol;
    @JsProperty(name = "tot_amt")
    private Double totAmt;
    @JsProperty(name = "lib_prep")
    private String libPrep;
    @JsProperty(name = "lib_method")
    private String libMethod;
    private Double index;
    @JsProperty(name = "lib_conc")
    private Double libConc;
    @JsProperty(name = "frag_size")
    private Double fragSize;
    private Double mol;
    private Double bps;
    @JsProperty(name = "lib_pmol")
    private Double libPmol;
    private Double multiple;
    @JsProperty(name = "f_pool_vol")
    private Double fPoolVol;
    @JsProperty(name = "elut_vol")
    private Double elutVol;

    public Work(){}
}
