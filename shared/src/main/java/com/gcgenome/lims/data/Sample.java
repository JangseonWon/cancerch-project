package com.gcgenome.lims.data;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Sample {
	private Double id;
	private Patient patient;
	private String barcode;
	private String remark;
	@JsOverlay
	@JsIgnore
	public Sample id(long id) {
		this.id = id+0.0;
		return this;
	}
	@JsOverlay
	@JsIgnore
	public Long id() {
		return id.longValue();
	}
	@JsOverlay
	@JsIgnore
	public Patient patient() {
		if(patient == null) return null;
		return Js.uncheckedCast(patient);
	}
}
