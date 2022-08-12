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
    private static native JsPropertyMap<String> params(String queryString) /*-{
		var params = {};
		queryString.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
		return params;
	}-*/;
    @Override
    public void onModuleLoad() {
        DomGlobal.console.log("a");
        JsPropertyMap<String> params = params(DomGlobal.window.location.search);
        DomGlobal.console.log(params);
        String id = (String)params.get("id");
        DomGlobal.console.log(id);
        long sample = Long.parseLong(((String)params.get("sample")).replace("-", ""));
        DomGlobal.console.log(sample);
        String service = (String)params.get("service");
        DomGlobal.console.log(service);
        ServiceApi.service(service).then((svc) -> {
            DomGlobal.console.log("!");
            CollapseElement<?> elemCollapsed = this.collapse(id, sample, svc);
            ExpandElement<?> elemExpand = this.expand(id, sample, svc);
            ((HtmlContentBuilder) Elements.body().add(elemCollapsed)).add(elemExpand);
            DomGlobal.console.log("4");
            elemCollapsed.onStateChange((evt) -> {
                elemExpand.update();
                elemCollapsed.element().style.display = "none";
                elemExpand.element().style.display = null;
            });
            DomGlobal.console.log("5");
            elemExpand.onStateChange((evt) -> {
                elemCollapsed.update();
                elemCollapsed.element().style.display = "flex";
                elemExpand.element().style.display = "none";
            });
            DomGlobal.console.log("6");
            elemExpand.element().style.display = "none";
            elemCollapsed.update();
            return null;
        });
        DomGlobal.console.log("3");
        Message msg = Message.builder().id(id).type(Message.MessageType.COLLAPSE).param("64px").build();
        DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
    }
    @Override
    protected ExpandElement<?> expand(String id, long sample, JsPropertyMap<?> service) {
        return ExpandElementFactory.create(id, sample, service);
    }
}