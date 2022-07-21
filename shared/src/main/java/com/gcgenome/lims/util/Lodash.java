package com.gcgenome.lims.util;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="_")
public final class Lodash {
	@JsMethod(namespace="_")
	public static native <T> T debounce(T callback, int milliseconds);
}
