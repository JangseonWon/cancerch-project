package com.greencross.lims.data;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Analysis2 {
	private Request request;
	private String serial;
	@JsProperty(name="recept")
	private Boolean recept;
	@JsProperty(name="create_at")
	private Double createAt;
	@JsProperty(name="last_modify_at")
	private Double lastModifyAt;
	private String etc;
	private String status;
	@JsProperty(name="send_log")
	private String sendLog;
	@JsProperty(name="sync_log")
	private String syncLog;
	private Report report;
	@JsOverlay
	@JsIgnore
	public Request request() {
		if(request == null) return null;
		return Js.uncheckedCast(request);
	}
	@JsOverlay
	@JsIgnore
	public Long createAt() {
		if(createAt == null) return null;
		else return createAt.longValue();
	}
	@JsOverlay
	@JsIgnore
	public Long lastModifyAt() {
		if(lastModifyAt == null) return null;
		else return lastModifyAt.longValue();
	}
}
