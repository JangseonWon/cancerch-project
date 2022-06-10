package com.greencross.lims.data;

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
public final class Work {
    private String worklist;
    private Double index;
    private String sample;
    private String patient_name;
    private String mrn;
    private String gid;
    private Double NAConc;
    private Double inputConc;
    private String libraryPrep;
    private Double libraryConc;
    private Double fragmentSize;
    private Double convertToNm;
    private Double assumingAMR;
    private Double nmOfDilution;
    private Double totalVol;
    private Double libraryVolume;
    private Double teBuffer;
    private String i7IndexId;
    private String i7Sequence;
    private String i5IndexId;
    private String i5Sequence;

    @JsOverlay
    @JsIgnore
    public Integer index(){
        if(index == null) return null;
        return index.intValue();
    }

    @JsOverlay
    @JsIgnore
    public Double convertToNm(){
        if(libraryConc == null || fragmentSize == null || assumingAMR == null) return 0.0;
        return libraryConc/(fragmentSize*assumingAMR)*1000000;
    }

    @JsOverlay
    @JsIgnore
    public void convertToNm(Double libraryConc, Double fragmentSize, Double assumingAMR){
        this.convertToNm = libraryConc/(fragmentSize*assumingAMR)*1000000;
    }

    @JsOverlay
    @JsIgnore
    public Double totalVol(){
        if(nmOfDilution == null) return 0.0;
        return libraryVolume*(convertToNm()/nmOfDilution);
    }

    @JsOverlay
    @JsIgnore
    public Double teBuffer(){
        if(libraryVolume == null) return 0.0;
        return totalVol()-libraryVolume;
    }
}
