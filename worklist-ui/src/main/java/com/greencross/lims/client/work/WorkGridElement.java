package com.greencross.lims.client.work;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.greencross.lims.data.Work;
import elemental2.dom.*;
import net.sayaya.ui.*;
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
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.*;

public class WorkGridElement extends HTMLElementBuilder<HTMLDivElement, WorkGridElement>{
    public static WorkGridElement build() { return new WorkGridElement(div());}
    private static ColumnString column(String name) {
        return ColumnBuilder.string(name).name(name).horizontal("center");
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
                    column("Na Conc.(pg/ul)").horizontal("right").build(),
                    column("input Conc.(ng)").horizontal("right").build(),
                    column("Library Prep").horizontal("right").build(),
                    column("Lib conc.(ng/ul) Qubit").horizontal("right").build(),
                    column("fragment size (bp)").horizontal("right").build(),
                    column("convert to nM").horizontal("right").readOnly(true).build(),
                    column("Assuming a Mr").build(),
                    column("nM of dilution").build(),
                    column("Total Vol(ul)").horizontal("right").readOnly(true).build(),
                    column("Library volume(ul)").build(),
                    column("TE buffer (ul)").horizontal("right").readOnly(true).build(),
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
    public WorkGridElement calculate(){
        return that();
    }
    private WorkGridElement update(Data[] data){
        try {
            elemSheet.values(data);
            return that();
        } catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private Data map(Work value){
        if(value == null) return null;
        String[] values = new String[15];
        if(value.json() != null) {
            JSONArray arrValue = (JSONArray) JSONParser.parseStrict(value.json());
            JSONObject json = (JSONObject) arrValue.get(0);
            values[0] = json.containsKey("naConc")      ? String.valueOf(json.get("naConc"))    : "";
            values[1] = json.containsKey("inputConc")   ? String.valueOf(json.get("inputConc")) : "";
            values[2] = json.containsKey("libPrep")     ? String.valueOf(json.get("libPrep"))   : "";
            values[3] = json.containsKey("libConc")     ? String.valueOf(json.get("libConc"))   : "";
            values[4] = json.containsKey("fragSize")    ? String.valueOf(json.get("fragSize"))  : "";
            values[5] = json.containsKey("convert")     ? String.valueOf(json.get("convert"))   : "";
            values[6] = json.containsKey("assuming")    ? String.valueOf(json.get("assuming"))  : "";
            values[7] = json.containsKey("dilution")    ? String.valueOf(json.get("dilution"))  : "";
            values[8] = json.containsKey("totalVol")    ? String.valueOf(json.get("totalVol"))  : "";
            values[9] = json.containsKey("libVol")      ? String.valueOf(json.get("libVol"))    : "";
            values[10] =json.containsKey("teBuffer")    ? String.valueOf(json.get("teBuffer"))  : "";
            values[11] =json.containsKey("i7Index")     ? String.valueOf(json.get("i7Index"))   : "";
            values[12] =json.containsKey("i7Seq")       ? String.valueOf(json.get("i7Seq"))     : "";
            values[13] =json.containsKey("i5Index")     ? String.valueOf(json.get("i5Index"))   : "";
            values[14] =json.containsKey("i5Seq")       ? String.valueOf(json.get("i5Seq"))     : "";
            return new Data(value.worklist() + "$" + value.index())
                    .put("index", String.valueOf(value.index()))
                    .put("G-ID", value.gid())
                    .put("의뢰번호", value.samples())
                    .put("수진자명", value.patientName())
                    .put("MRN", value.mrns())
                    .put("Na Conc.(pg/ul)", values[0])
                    .put("input Conc.(ng)", values[1])
                    .put("Library Prep", values[2])
                    .put("Lib conc.(ng/ul)", values[3])
                    .put("fragment size (bp)", values[4])
                    .put("convert to nM", values[5])
                    .put("Assuming a Mr", values[6])
                    .put("nM of dilution", values[7])
                    .put("Total Vol(ul)", values[8])
                    .put("Library Volume(ul)", values[9])
                    .put("TE buffer (ul)", values[10])
                    .put("I7 Index ID", values[11])
                    .put("I7 Sequence", values[12])
                    .put("I5 Index ID", values[13])
                    .put("I5 Sequence", values[14]);
        }else{
            return new Data(value.worklist() + "$" + value.index())
                    .put("index", String.valueOf(value.index()))
                    .put("G-ID", value.gid())
                    .put("의뢰번호", value.samples())
                    .put("수진자명", value.patientName())
                    .put("MRN", value.mrns());
        }
    }
    public WorkGridElement updateDilut(Double value){
        return that();
    }
    public WorkGridElement updateAssum(Double value){
        return that();
    }
    @Override
    public WorkGridElement that() {
        return this;
    }
}