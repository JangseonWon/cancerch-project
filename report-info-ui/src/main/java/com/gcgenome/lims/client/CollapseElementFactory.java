package com.gcgenome.lims.client;

import com.gcgenome.lims.client.collapse.DefaultCollapseElement;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CollapseElementFactory {
	public CollapseElement<?> create(String id, long sample, JsPropertyMap<?> service) {
		return DefaultCollapseElement.build(id, sample, service);
	}
}
