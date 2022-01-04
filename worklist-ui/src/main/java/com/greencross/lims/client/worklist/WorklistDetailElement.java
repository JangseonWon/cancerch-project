package com.greencross.lims.client.worklist;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.api.WorklistDTLApi;
import com.greencross.lims.client.AbstractScene;
import com.greencross.lims.client.Router;
import com.greencross.lims.dto.Query;
import elemental2.core.JsDate;
import elemental2.dom.*;
import net.sayaya.ui.*;
import com.greencross.lims.ui.IconElement;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import static org.jboss.elemento.Elements.*;

public class WorklistDetailElement extends AbstractScene<WorklistDetailElement> {
    // region # Checker
    private Boolean backChecker = false;
    private Boolean viewChecker = false;
    private String id = "";
    private Boolean Lock = false;
    private String iptPrev = null;
    // endregion
    // region # Title / Breadcumb
    private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Worklist");
    private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
        RouteApi.location("", true, false);
    }).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
            .add("RnD", evt->{
                evt.preventDefault();
                evt.stopPropagation();
            }).add("Worklist", evt->{
                if(backChecker)
                    if(!DomGlobal.confirm("변경사항이 반영되지 않습니다. 돌아가시겠습니까?"))
                        return ;
                evt.preventDefault();
                evt.stopPropagation();
                Router.location("", true);
            });
    // endregion

    //region # variable declare
    private final WorkDetailTopGridElement topGrid = WorkDetailTopGridElement.instance().style("position: relative; overflow: hidden;" +
            "border-top: 1px solid #AAA; border-bottom: 1px solid #AAA; transition: all 200ms;");
    private final WorkDetailBotGridElement botGrid = WorkDetailBotGridElement.instance().style("position: relative; height: 90vh; overflow: hidden;" +
            "border-top: 1px solid #AAA; border-bottom: 1px solid #AAA; transition: all 200ms;");
    private final ButtonElement Add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Regular, "fa-plus")).style("height: 4em;");
    private final ButtonElement Del = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Regular, "fa-eraser")).style("height: 4em;");
    private final ButtonElement Save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Regular, "fa-save")).style("height: 4em;");
    private final ButtonElementToggle Toggle = ButtonElement.toggle().css("button").text("Barcode").before(IconElement.icon(IconElement.Type.Regular, "fa-barcode")).style("height: 4em; width : 14vh;");
    private final ButtonElement Search = ButtonElement.outline().css("button").text("Search").before(IconElement.icon(IconElement.Type.Regular, "fa-search")).style("height: 4em;");
    private final ButtonElement Complete = ButtonElement.outline().css("button").text("Complete").before(IconElement.icon(IconElement.Type.Regular, "fa-check")).style("height: 4em;");
    private final ButtonElement Recept = ButtonElement.outline().css("button").text("접수").before(IconElement.icon(IconElement.Type.Regular, "fa-receipt")).style("height: 4em;");
    private final HtmlContentBuilder<HTMLDivElement> input = div().style("display: flex;");
    private final TextFieldElement<String> BarcodeReader = TextFieldElement.textBox().outlined().css("input", "barcode").text("검체바코드").style("margin-left: 10px; width : 20.2em;").required(true);
    private final TextFieldElement<String> RequestReader = TextFieldElement.textBox().outlined().css("input", "barcode").text("의뢰번호").style("margin-left: 10px;").required(true);
    private final TextFieldElement<JsDate> DateFrom = TextFieldElement.dateBox().outlined().text("Date from").value(yesterday());
    private final TextFieldElement<JsDate> DateTo = TextFieldElement.dateBox().outlined().text("Date to").value(new JsDate());
    private final IsElement<?>[] appendControls;
    private final IsElement<?>[] listControls;
    //endregion

    // region # Element instance
    public WorklistDetailElement(Query query){
        super(query);
        initialize();

        listControls = new IsElement[] { controlPanelElements()[4] };
        appendControls = new IsElement[] { controlPanelElements()[0], controlPanelElements()[1], controlPanelElements()[2], controlPanelElements()[3] };
        hideControls(appendControls);
        readModeUpdate("barcode");

        // region # button events
        Search.onClick(evt->{
            updateTop(DateFrom.value(), DateTo.value());
        });
        Save.onClick(evt->{ if(backChecker) backChecker = false; });
        Add.onClick(evt->{ addMode(); });
        Complete.onClick(evt->{listMode();});
        Toggle.onValueChange(evt -> {
            if(evt.value()){
                Toggle.text(" 의 뢰 번 호 ");
                readModeUpdate("Request");
            }else{
                Toggle.text("Barcode");
                readModeUpdate("barcode");
            }
        });
        BarcodeReader.on(EventType.keydown, evt-> Scheduler.get().scheduleDeferred(this::validation));
        Recept.onClick(evt->receptBySample());
        // endregion
    }
    // endregion

    // region #User Define
    private void validation(){
        String value = BarcodeReader.value();
        if(value.length() < 11) return ;
        if(Lock) return ;
        if(value.trim().equals(iptPrev)){
            BarcodeReader.select();
            return;
        }
        String barcode = value.substring(0, 11);
        iptPrev = barcode;
        ProgressApi.open(false);
        Lock = true;

    }
    private void receptBySample() {
        String value = RequestReader.value();
        value = value.replace("-", "");
        value = value.replace(" ", "");
        if(value.length() < 15) return;
        if(Lock) return;
        if(value.trim().equals(iptPrev)) {
            RequestReader.select();
            return;
        }
        String sample = value;
        iptPrev = sample;
        DomGlobal.console.log(iptPrev);
    }
    public WorklistDetailElement parent(String param){
        this.id = param;
        while(breadcumb.element().childElementCount > 5) ((HTMLElement)breadcumb.element().childNodes.getAt(5)).remove();
        breadcumb.add(id, evt->{
            evt.preventDefault();
            evt.stopPropagation();
            Router.location(id, false);
        });
        return that();
    }

    private static JsDate yesterday() {
        JsDate today = new JsDate();
        JsDate yesterday = new JsDate(today);
        yesterday.setDate(yesterday.getDate()-1);
        yesterday.setHours(0, 0, 0, 0);
        return yesterday;
    }
    private void updateTop(JsDate yesterday, JsDate today){
        ProgressApi.open(false);
        WorklistDTLApi.findSample(yesterday, today).then(sample -> {
            topGrid.value(sample);
            ProgressApi.close();
            return null;
        });
    }
    private void addMode() {
        updateTop(DateFrom.value(), DateTo.value());
        topGrid.element().style.height = CSSProperties.HeightUnionType.of("35vh");
        botGrid.element().style.height = CSSProperties.HeightUnionType.of("55vh");
        showControls(appendControls);
        hideControls(listControls);
    }
    private void listMode() {
        topGrid.element().style.height = CSSProperties.HeightUnionType.of("0");
        botGrid.element().style.height = CSSProperties.HeightUnionType.of("87vh");
        showControls(listControls);
        hideControls(appendControls);
    }
    private WorklistDetailElement readModeUpdate(String mode){
        input.element().innerHTML = "";
        if(mode.equals("barcode")){
            input.add(BarcodeReader);
        }
        else{
            input.add(RequestReader).add(Recept);
        }
        return this;
    }
    // endregion


    // region # Control Controller
    private void showControls(IsElement<?>[] controlPanels) {
        for(IsElement<?> controlPanel: controlPanels) controlPanel.element().style.display = "flex";
    }
    private void hideControls(IsElement<?>[] controlPanels) {
        for(IsElement<?> controlPanel: controlPanels) controlPanel.element().style.display = "none";
    }
    // endregion

    // region # Override
    @Override
    protected IconElement icon() {
        return IconElement.icon(IconElement.Type.Light, "fa-clipboard-list");
    }

    @Override
    protected HtmlContentBuilder<HTMLLabelElement> title() {
        return title;
    }

    @Override
    protected BreadcumbElement breadcumb() {
        return breadcumb;
    }

    @Override
    protected IsElement<?>[][] controls() {
        return new IsElement<?>[][]{
                new IsElement<?>[]{input},
                new IsElement<?>[]{Toggle},
                new IsElement<?>[]{DateFrom, DateTo},
                new IsElement<?>[]{Search, Complete},
                new IsElement<?>[]{Add, Del, Save}
        };
    }

    @Override
    protected IsElement<?>[] contents() {
        return new IsElement<?>[] { topGrid, botGrid };
    }
    @Override
    public WorklistDetailElement that() {
        return this;
    }

    @Override
    public void update() {
        ProgressApi.open(false);
        WorklistDTLApi.findWork(id).then(work->{
            botGrid.value(work);
            ProgressApi.close();
            return null;
        });
    }
    // endregion
}
