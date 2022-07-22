package com.gcgenome.lims.data;

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
    @JsProperty(name = "conc_na")
    private Double concNa;
    @JsProperty(name = "conc_input")
    private Double concInput;
    @JsProperty(name = "lib_prep")
    private Double libPrep;
    @JsProperty(name = "lib_conc_tape")
    private Double libConcTape;
    @JsProperty(name = "lib_conc_qubit")
    private Double libConcQubit;
    @JsProperty(name = "fragment_size")
    private Double fragmentSize;
    private Double amount;
    private Double dilution;
    private Double volume;
    @JsProperty(name = "lib_volume")
    private Double libVolume;
    @JsProperty(name = "buffer_volume")
    private Double bufferVolume;
    @JsProperty(name = "index_i7")
    private String indexI7;
    @JsProperty(name = "sequence_i7")
    private String sequenceI7;
    @JsProperty(name = "index_i5")
    private String indexI5;
    @JsProperty(name = "sequence_i5")
    private String sequenceI5;
    @JsOverlay
    @JsIgnore
    public Integer index(){
        if(index == null) return null;
        return index.intValue();
    }
}
