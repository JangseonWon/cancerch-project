package com.greencross.lims.client.worklist;

import com.greencross.lims.api.WorklistDTLApi;
import com.greencross.lims.client.ControllerElement;
import elemental2.core.JsDate;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.HTMLElementBuilder;
import com.greencross.lims.ui.IconElement;
import net.sayaya.ui.TextFieldElement;
import org.jboss.elemento.HtmlContentBuilder;

import static org.jboss.elemento.Elements.*;

public class WorklistDetailElement extends HTMLElementBuilder<HTMLDivElement, WorklistDetailElement> {
    private final ControllerElement controller = ControllerElement.instance().hidden(false).style("height : 5vh");
    private final ControllerElement centroller = ControllerElement.instance().hidden(false);
    private final WorkDetailTopGridElement topGrid = WorkDetailTopGridElement.instance();
    private final WorkDetailBotGridElement botGrid = WorkDetailBotGridElement.instance();
    private final ButtonElement Back = ButtonElement.outline().css("button").text("Back").before(IconElement.icon(IconElement.Type.Regular, "fa-backward")).style("height: 100%;");
    private final ButtonElement Add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Regular, "fa-plus")).style("height: 100%;").enabled(false);
    private final ButtonElement Del = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Regular, "fa-eraser")).style("height: 100%;").enabled(false);
    private final ButtonElement Save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Regular, "fa-save")).style("height: 100%;").enabled(false);
    private final ButtonElement Down = ButtonElement.outline().css("button").text("Down").before(IconElement.icon(IconElement.Type.Regular, "fa-hand-point-down")).style("height: 100%;").enabled(false);
    private final ButtonElement Up = ButtonElement.outline().css("button").text("Up").before(IconElement.icon(IconElement.Type.Regular, "fa-hand-point-up")).style("height: 100%;").enabled(false);
    private final ButtonElement Search = ButtonElement.outline().css("button").text("Search").before(IconElement.icon(IconElement.Type.Regular, "fa-search")).style("height: 100%;");
    private final TextFieldElement<String> Reader = TextFieldElement.textBox().outlined().text("검체바코드").style("margin-left: 10px;");
    private final TextFieldElement<JsDate> DateFrom = TextFieldElement.dateBox().outlined().text("Date from").value(new JsDate());
    private final TextFieldElement<JsDate> DateTo = TextFieldElement.dateBox().outlined().text("Date to").value(yesterday());
    private String mode = "normal";
    public static WorklistDetailElement instance(String id) { return new WorklistDetailElement(div());}
    public WorklistDetailElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.css("top"));
        Search.onClick(evt->{
            update(DateTo.value(), DateFrom.value());
        });
        Back.onClick(evt->{
            DomGlobal.location.assign("worklist.html");
        });
        Save.onClick(evt->{

        });
        Add.onClick(evt->{

        });
        Up.onClick(evt->{

        });
        Down.onClick(evt->{

        });

        e.add(controller
                .add(div().add(Back))
                .add(div().add(Reader).add(DateTo).add(DateFrom)).add(div().add(Search))
                .add(div().add(Save).add(Add).add(Del)))
         .add(div().css("layout").style("height: calc(94vh - 20px);")
                .add(topGrid)
                .add(centroller.add(div().add(Up).add(Down)).style("justify-content:center;"))
                .add(botGrid));
        update(DateTo.value(), DateFrom.value());
    }
    private static JsDate yesterday() {
        JsDate today = new JsDate();
        JsDate yesterday = new JsDate(today);
        yesterday.setDate(yesterday.getDate()-1);
        yesterday.setHours(0, 0, 0, 0);
        return yesterday;
    }
    private void update(JsDate yesterday, JsDate today){
        WorklistDTLApi.findSample(yesterday, today).then(sample -> {
            topGrid.value(sample);
            return null;
        });
    }
    @Override
    public WorklistDetailElement that() {
        return this;
    }
}
