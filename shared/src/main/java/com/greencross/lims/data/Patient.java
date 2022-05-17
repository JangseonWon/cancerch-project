package com.greencross.lims.data;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class Patient {
	private String name;
	private String mrn;
	private String sex;
	private Double birth;
	private String customer;
	@JsOverlay
	@JsIgnore
	public Long birth() {
		if(birth == null) return null;
		else return birth.longValue();
	}
}
