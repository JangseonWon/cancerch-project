package com.gcgenome.lims.client;

import com.gcgenome.lims.client.expand.DefaultExpandElement;
import com.gcgenome.lims.test.HasCategory;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExpandElementFactory {
	public ExpandElement<?> create(String id, long sample, JsPropertyMap<?> service) {
		var category = HasCategory.Category.valueOf((String)service.get("category"));
		// if(category == HasCategory.Category.RareDisease) return PanelExpandElement.build(id, sample, service);
		return DefaultExpandElement.build(sample, service);
	}
}
