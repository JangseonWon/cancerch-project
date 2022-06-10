package com.greencross.lims.data;

import jsinterop.annotations.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_= {@JsOverlay, @JsIgnore})
@Getter(onMethod_= {@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Worklist {
    private String id;
    private String title;
    @JsProperty(name="created_at")
    private String created;
    private String status;
    private String remark;

    @JsOverlay
    @JsIgnore
    public Status status() {
        if (status == null) return null;
        return Status.valueOf(status);
    }

    public enum Status{
        NORMAL,         // 검증완료
        PRE_CREATE,     // Worklist 저장
        POST_CREATE,    // Worklist + Plate
        DISPOSAL,
        MERGED,
        SEQUENCING,
        ANALYZED,
        COMPLETE
    }
}
