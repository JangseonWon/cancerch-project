package com.gcgenome.lims.data;

import jsinterop.annotations.*;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
public final class Work {
    public String worklist;
    @JsProperty(name="index")
    public Double index;
    public String samples;
    public String services;
    public String mrns;
    @JsProperty(name = "patient_name")
    public String patientName;
    public String gid;
    public String json;
    public String address;
    @JsProperty(name = "last_modify_by")
    public User lastModifyBy;
    @JsProperty(name = "created_by")
    public User createdBy;
    @JsProperty(name = "conc_na")
    public Double concNa;
    @JsProperty(name = "conc_input")
    public Double concInput;
    @JsProperty(name = "lib_prep")
    public String libPrep;
    @JsProperty(name = "lib_conc_tape")
    public Double libConcTape;
    @JsProperty(name = "lib_conc_qubit")
    public Double libConcQubit;
    @JsProperty(name = "fragment_size")
    public Double fragmentSize;
    public Double amount;
    public Double dilution;
    public Double volume;
    @JsProperty(name = "lib_volume")
    public Double libVolume;
    @JsProperty(name = "buffer_volume")
    public Double bufferVolume;
    public Boolean qc;
    @JsProperty(name = "index_i7")
    public String indexI7;
    @JsProperty(name = "sequence_i7")
    public String sequenceI7;
    @JsProperty(name = "index_i5")
    public String indexI5;
    @JsProperty(name = "sequence_i5")
    public String sequenceI5;
    private String state;
    @JsOverlay
    @JsIgnore
    public Integer index(){
        if(index == null) return null;
        return index.intValue();
    }
    @JsOverlay
    @JsIgnore
    public State state() {
        if(state==null) return null;
        return State.valueOf(state);
    }
    @JsOverlay
    @JsIgnore
    public Work state(State state) {
        this.state = state.name();
        return this;
    }
    public enum State {
        COMPLETE, PENDING, HOLDING, PENDING_B, HOLDING_B
    }
}
