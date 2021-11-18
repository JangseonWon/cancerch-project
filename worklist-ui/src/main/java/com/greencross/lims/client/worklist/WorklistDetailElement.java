package com.greencross.lims.client.worklist;

import com.greencross.lims.api.WorklistDTLApi;
import com.greencross.lims.client.ControllerElement;
import com.greencross.lims.data.Work;
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
    //region # variable declare
    private final ControllerElement controller = ControllerElement.instance().hidden(false).style("height : 5vh");
    private final ControllerElement centroller = ControllerElement.instance().hidden(false);
    private final WorkDetailTopGridElement topGrid = WorkDetailTopGridElement.instance();
    private final WorkDetailBotGridElement botGrid = WorkDetailBotGridElement.instance();
    private final ButtonElement Back = ButtonElement.outline().css("button").text("Back").before(IconElement.icon(IconElement.Type.Regular, "fa-backward")).style("height: 100%;");
    private final ButtonElement Add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Regular, "fa-plus")).style("height: 100%;");
    private final ButtonElement Del = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Regular, "fa-eraser")).style("height: 100%;");
    private final ButtonElement Save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Regular, "fa-save")).style("height: 100%;");
    private final ButtonElement Down = ButtonElement.outline().css("button").text("Down").before(IconElement.icon(IconElement.Type.Regular, "fa-hand-point-down")).style("height: 100%;");
    private final ButtonElement Up = ButtonElement.outline().css("button").text("Up").before(IconElement.icon(IconElement.Type.Regular, "fa-hand-point-up")).style("height: 100%;");
    private final ButtonElement Search = ButtonElement.outline().css("button").text("Search").before(IconElement.icon(IconElement.Type.Regular, "fa-search")).style("height: 100%;");
    private final TextFieldElement<String> Reader = TextFieldElement.textBox().outlined().text("검체바코드").style("margin-left: 10px;");
    private final TextFieldElement<JsDate> DateFrom = TextFieldElement.dateBox().outlined().text("Date from").value(new JsDate());
    private final TextFieldElement<JsDate> DateTo = TextFieldElement.dateBox().outlined().text("Date to").value(yesterday());
    private String mode = "normal";
    private Boolean backChecker = false;
    private Boolean viewChecker = false;
    //endregion
    // region # Element instance
    public static WorklistDetailElement instance(String id, String state) { return new WorklistDetailElement(div(), id, state);}
    public WorklistDetailElement(HtmlContentBuilder<HTMLDivElement> e, String id, String state) {
        super(e.css("top"));

        // region # button events
        Search.onClick(evt->{
            if(viewChecker)
                if(!DomGlobal.confirm("변경사항은 저장되지 않습니다. 재조회 하시겠습니까?"))
                    return ;
            updateTop(DateTo.value(), DateFrom.value());
            updateBot(id);
        });
        Back.onClick(evt->{
            if(backChecker)
                if(!DomGlobal.confirm("변경사항이 반영되지 않습니다. 돌아가시겠습니까?"))
                    return ;
            DomGlobal.location.assign("worklist.html");
        });
        Save.onClick(evt->{
            if(backChecker) backChecker = false;
        });
        Add.onClick(evt->{
            mode = "add";

        });
        Up.onClick(evt->{
            try{

            }catch(Exception ex){
                DomGlobal.console.log(ex);
            }
            if(!backChecker) backChecker = true;
            if(!viewChecker) viewChecker = true;
        });
        Down.onClick(evt->{
            try{
                topGrid.selection().forEach(t->{
                    DomGlobal.console.log(t);
                    Work tmp = new Work();
                    tmp.id(null).no(null).sample(t.sample()).barcode(t.barcode()).testType(t.type()).testCode(t.service()).reqNum(t.mrn()).patientName(t.name())
                            .custermerName(t.customerName()).custermerCode(t.customerCode()).endDt(t.dateEnd());
                    botGrid.append(tmp);
                });
            }catch(Exception ex){
                DomGlobal.console.log(ex);
            }

            if(!backChecker) backChecker = true;
            if(!viewChecker) viewChecker = true;
        });
        // endregion
        // region # layouts
        e.add(controller
                .add(div().add(Back))
                .add(div().add(Reader).add(DateTo).add(DateFrom)).add(div().add(Search))
                .add(div().add(Save).add(Add).add(Del)))
         .add(div().css("layout").style("height: calc(94vh - 20px);")
                .add(topGrid)
                .add(centroller.add(div().add(Up).add(Down)).style("justify-content:center;"))
                .add(botGrid));
        if(state.equals("close")){ Search.enabled(false); Save.enabled(false); Add.enabled(false); Del.enabled(false); Up.enabled(false); Down.enabled(false);}
        // endregion
        // region # initialize
        updateTop(DateTo.value(), DateFrom.value());
        updateBot(id);
        // endregion
    }
    // endregion
    private static JsDate yesterday() {
        JsDate today = new JsDate();
        JsDate yesterday = new JsDate(today);
        yesterday.setDate(yesterday.getDate()-1);
        yesterday.setHours(0, 0, 0, 0);
        return yesterday;
    }
    private void updateTop(JsDate yesterday, JsDate today){
        WorklistDTLApi.findSample(yesterday, today).then(sample -> {
            topGrid.value(sample);
            return null;
        });
    }
    private void updateBot(String id){
        WorklistDTLApi.findWork(id).then(work->{
            botGrid.value(work);
            return null;
        });
    }
    @Override
    public WorklistDetailElement that() {
        return this;
    }
}
