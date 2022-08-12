package com.gcgenome.lims.client;

import com.gcgenome.lims.client.expand.DefaultExpandElement;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExpandElementFactory {
	public ExpandElement<?> create(String id, long sample, JsPropertyMap<?> service) {
		return DefaultExpandElement.build(sample, service);
	}
}
