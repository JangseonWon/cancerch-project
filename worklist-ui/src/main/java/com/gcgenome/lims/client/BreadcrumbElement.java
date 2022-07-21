package com.gcgenome.lims.client;

import com.gcgenome.lims.api.RouteApi;
import com.gcgenome.lims.ui.IconElement;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;

public class BreadcrumbElement {
    public static BreadcrumbElement build() {
        return new BreadcrumbElement();
    }
    private final net.sayaya.ui.BreadcrumbElement breadcumb = net.sayaya.ui.BreadcrumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt ->{
        RouteApi.location("", true, false);
    });
    public BreadcrumbElement splitter(HTMLElement splitter) {
        breadcumb.splitter(splitter);
        return this;
    }
    public BreadcrumbElement add(String label, EventListener listener) {
        breadcumb.add(label, listener);
        return this;
    }
   public void set(int depth, String label, EventListener listener) {
        while(breadcumb.element().childElementCount > 2*depth-1) breadcumb.element().lastElementChild.remove();
        add(label, listener);
    }
    public net.sayaya.ui.BreadcrumbElement that() {
        return breadcumb.that();
    }
}
