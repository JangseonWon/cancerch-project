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
    private String worklist;
    @JsProperty(name="index")
    private Double index;
    private String samples;
    private String services;
    private String mrns;
    @JsProperty(name = "patient_name")
    private String patientName;
    private String gid;
    private String json;
    @JsProperty(name = "last_modify_by")
    private User lastModifyBy;
    @JsProperty(name = "created_by")
    private User createdBy;

    @JsOverlay
    @JsIgnore
    public Integer index(){
        if(index == null) return null;
        return index.intValue();
    }

    private static class User{
        private String id;
        private String name;
    }
}
