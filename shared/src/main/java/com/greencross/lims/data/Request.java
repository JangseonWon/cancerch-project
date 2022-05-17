package com.greencross.lims.data;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Request {
	private Sample sample;
	private String serial;
	private Service service;
	@JsProperty(name="date_request")
	private Double dateRequest;
	@JsProperty(name="date_start")
	private Double dateStart;
	@JsProperty(name="date_sampling")
	private Double dateSampling;
	@JsProperty(name="date_due")
	private Double dateDue;
	private Boolean registered;
	private Boolean canceled;
	private Boolean deleted;
	private String state;
	@JsOverlay
	@JsIgnore
	public Sample sample() {
		if(sample == null) return null;
		return Js.uncheckedCast(sample);
	}
	@JsOverlay
	@JsIgnore
	public Service service() {
		if(service == null) return null;
		return Js.uncheckedCast(service);
	}
	@JsOverlay
	@JsIgnore
	public Long dateRequest() {
		if(dateRequest == null) return null;
		else return dateRequest.longValue();
	}
	@JsOverlay
	@JsIgnore
	public Long dateDue() {
		if(dateDue == null) return null;
		else return dateDue.longValue();
	}
}
