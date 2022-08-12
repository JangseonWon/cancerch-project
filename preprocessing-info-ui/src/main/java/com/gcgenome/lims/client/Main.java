package com.gcgenome.lims.client;

import com.gcgenome.lims.dto.Message;
import elemental2.core.Global;
import elemental2.dom.DomGlobal;
import jsinterop.base.JsPropertyMap;
import org.jboss.elemento.Elements;
import org.jboss.elemento.HtmlContentBuilder;

public class Main extends AbstractMain {
    @Override
    protected CollapseElement<?> collapse(String id, long sample, JsPropertyMap<?> service) {
        return CollapseElementFactory.create(id, sample, service);
    }
    @Override
    protected ExpandElement<?> expand(String id, long sample, JsPropertyMap<?> service) {
        return ExpandElementFactory.create(id, sample, service);
    }
    public void onModuleLoad() {
        JsPropertyMap<String> params = params(DomGlobal.window.location.search);
        String id = (String)params.get("id");
        long sample = Long.parseLong(((String)params.get("sample")).replace("-", ""));
        String service = (String)params.get("service");
        ServiceApi.service(service).then((svc) -> {
            DomGlobal.console.log(svc);
            CollapseElement<?> elemCollapsed = this.collapse(id, sample, svc);
            ExpandElement<?> elemExpand = this.expand(id, sample, svc);
            DomGlobal.console.log(Elements.body());
            ((HtmlContentBuilder) Elements.body().add(elemCollapsed)).add(elemExpand);
            DomGlobal.console.log(Elements.body());
            elemCollapsed.onStateChange((evt) -> {
                elemExpand.update();
                elemCollapsed.element().style.display = "none";
                elemExpand.element().style.display = null;
            });
            elemExpand.onStateChange((evt) -> {
                elemCollapsed.update();
                elemCollapsed.element().style.display = "flex";
                elemExpand.element().style.display = "none";
            });
            elemExpand.element().style.display = "none";
            elemCollapsed.update();
            return null;
        });
        Message msg = Message.builder().id(id).type(Message.MessageType.COLLAPSE).param("64px").build();
        DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
    }
    private static native JsPropertyMap<String> params(String queryString) /*-{
		var params = {};
		queryString.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
		return params;
	}-*/;
}