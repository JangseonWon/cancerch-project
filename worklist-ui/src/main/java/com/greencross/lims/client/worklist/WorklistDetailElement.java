package com.greencross.lims.client.worklist;

import com.greencross.lims.client.ControllerElement;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HtmlContentBuilder;

import static org.jboss.elemento.Elements.div;

public class WorklistDetailElement extends HTMLElementBuilder<HTMLDivElement, WorklistDetailElement> {
    private final ControllerElement controller = ControllerElement.instance().hidden(false);
    private final WorkDetailTopGridElement topGrid = WorkDetailTopGridElement.instance();
    private final WorkDetailBotGridElement botGrid = WorkDetailBotGridElement.instance();
    public static WorklistDetailElement instance(String id) { return new WorklistDetailElement(div());}
    public WorklistDetailElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.css("top"));

        e.add(controller)
        .add(div().css("layout")
        .add(topGrid)
        .add(botGrid));

    }

    @Override
    public WorklistDetailElement that() {
        return this;
    }
}
