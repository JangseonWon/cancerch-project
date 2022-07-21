package com.gcgenome.lims.data;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Analysis {
	private String batch;
	private Double row;
	private Request request;
	private String sample;
	@JsProperty(name="created_at")
	private String createAt;
	private User createBy;
	@JsProperty(name="last_modify_at")
	private String lastModifyAt;
	private User lastModifyBy;
	private String etc;
	private Report report;
	@JsOverlay
	@JsIgnore
	public Integer row() {
		if(row == null) return null;
		else return row.intValue();
	}
	@JsOverlay
	@JsIgnore
	public Request request() {
		if(request == null) return null;
		return Js.uncheckedCast(request);
	}
}
