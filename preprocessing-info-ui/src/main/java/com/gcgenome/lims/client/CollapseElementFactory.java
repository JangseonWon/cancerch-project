package com.gcgenome.lims.client;

import com.gcgenome.lims.client.collapse.DefaultCollapseElement;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CollapseElementFactory {
	public CollapseElement<?> create(String id, long sample, JsPropertyMap<?> service) {
		// var category = Category.valueOf((String)service.get("category"));
		/*if(category == Category.DES || category == Category.WES || category == Category.DGS || category == Category.Single ||
			category == Category.SinglePlus || category == Category.Cancer || category == Category.RareDisease) return PanelCollapseElement.build(id, sample, service);
		*/return DefaultCollapseElement.build(id, sample, service);
	}
}
