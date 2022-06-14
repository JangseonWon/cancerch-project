package com.greencross.lims.client.work;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.greencross.lims.data.Patient;
import com.greencross.lims.data.Request;
import com.greencross.lims.data.Service;
import com.greencross.lims.data.Work;
import com.greencross.lims.util.DataTransformUtil;
import elemental2.dom.*;
import elemental2.promise.Promise;
import jsinterop.annotations.JsProperty;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.*;
import net.sayaya.ui.chart.CellCoord;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnNumber;
import net.sayaya.ui.chart.column.ColumnString;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.*;

public class WorkGridElement extends HTMLElementBuilder<HTMLDivElement, WorkGridElement>{
    public static WorkGridElement build() { return new WorkGridElement(div());}
    private static ColumnString column(String name) {
        return ColumnBuilder.string(name).name(name).horizontal("center");
    }
    private static ColumnNumber columnNum(String name){
        return ColumnBuilder.number(name).name(name).horizontal("right");
    }
    private final SheetElement.SheetConfiguration config = SheetElement.builder()
            .rowHeaders(false)
            .autoColSize(true)
            .autoRowSize(false)
            .manualColumnMove(true)
            .manualColumnResize(true)
            .stretchH("all")
            .columns(
                    column("index").readOnly(true).build(),
                    column("G-ID").readOnly(true).build(),
                    column("수진자명").readOnly(true).build(),
                    column("MRN").readOnly(true).build(),
                    columnNum("Na Conc.(pg/ul)").build(),
                    columnNum("input Conc.(ng)").build(),
                    columnNum("Library Prep").build(),
                    columnNum("Lib conc.(ng/ul) Qubit").build(),
                    columnNum("fragment size (bp)").build(),
                    columnNum("convert to nM").readOnly(true).build(),
                    column("Assuming a Mr").build(),
                    column("nM of dilution").build(),
                    columnNum("Total Vol(ul)").readOnly(true).build(),
                    column("Library volume(ul)").build(),
                    columnNum("TE buffer (ul)").readOnly(true).build(),
                    column("I7 Index ID").build(),
                    column("I7 Sequence").build(),
                    column("I5 Index ID").build(),
                    column("I5 Sequence").build());

    private final SheetElement elemSheet = config.build();
    private final ListElement.SingleLineItem lblManager = ListElement.singleLine().label("담당자");
    private final ListElement.SingleLineItem lblPhone = ListElement.singleLine().label("연락처");
    private final ListElement.SingleLineItem lblRemark = ListElement.singleLine().label("비고입력");
    private final MenuElement menu = MenuElement.build(ListElement.singleLineList()
            .add(lblRemark)
            .divider()
            .add(lblManager)
            .add(lblPhone)).css("menu");

    private List<Work> works = new LinkedList<>();

    private WorkGridElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.style("width: 100%;"));
        HtmlContentBuilder<HTMLDivElement> table = div().style("overflow: hidden; width: 100%; height: 60vh; border-bottom: 1px solid #AAA;").add(div().style("border-top: 1px solid #AAA;").add(elemSheet));
        e.add(table);
        menu._for(element());
        menu.on(EventType.mouseleave, evt->menu.close());
    }
    public WorkGridElement update(Work[] values){
        this.works = Arrays.stream(values).collect(Collectors.toList());
        return update(Arrays.stream(values).map(this::map).toArray(Data[]::new));
    }
    public void calculate(Double assum, Double nM){

    }
    private WorkGridElement update(Data[] data){
        try {
            elemSheet.values(data).refresh();
            return that();
        } catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private Data map(Work value){
        if(value == null) return null;
        JSONObject json = (JSONObject) JSONParser.parseStrict(value.json());
        DomGlobal.console.log(json);
        return new Data(value.worklist()+"$"+value.index())
                .put("index",                   String.valueOf(value.index()))
                .put("G-ID",                    value.gid())
                .put("의뢰번호",                value.samples())
                .put("수진자명",                value.patientName())
                .put("MRN",                     value.mrns());

    }
    @Override
    public WorkGridElement that() {
        return this;
    }
}