package com.greencross.lims.client.worklist;

import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HtmlContentBuilder;

import static org.jboss.elemento.Elements.div;

public class WorklistDetailElement extends HTMLElementBuilder<HTMLDivElement, WorklistDetailElement> {
    public static WorklistDetailElement instance(String id) { return new WorklistDetailElement(div());}
    public WorklistDetailElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.css("worklistDTL"));
    }

    @Override
    public WorklistDetailElement that() {
        return this;
    }
}
