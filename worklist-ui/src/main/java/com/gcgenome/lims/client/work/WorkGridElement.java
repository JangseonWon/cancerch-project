package com.gcgenome.lims.client.work;

import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.data.Work;
import elemental2.dom.HTMLDivElement;
import elemental2.promise.Promise;
import jsinterop.base.Js;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.ListElement;
import net.sayaya.ui.MenuElement;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnString;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.div;

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
                    column("의뢰번호").readOnly(true).build(),
                    column("수진자명").readOnly(true).build(),
                    column("MRN").readOnly(true).build(),
                    column("Na Conc(pg/ul)").horizontal("right").build(),
                    column("input Conc(ng)").horizontal("right").build(),
                    column("Library Prep").build(),
                    column(WorkModel.ConcTape.id).horizontal("right").build(),
                    column(WorkModel.ConcQubit.id).horizontal("right").build(),
                    column(WorkModel.FragSize.id).horizontal("right").build(),
                    column(WorkModel.NM.id).horizontal("right").readOnly(true).build(),
                    column(WorkModel.Assuming.id).name(WorkModel.Assuming.label).build(),
                    column(WorkModel.Dilution.id).name(WorkModel.Dilution.label).build(),
                    column(WorkModel.Volume.id).horizontal("right").readOnly(true).build(),
                    column(WorkModel.LibraryVolume.id).name(WorkModel.LibraryVolume.label).build(),
                    column(WorkModel.TEBuffer.id).horizontal("right").readOnly(true).build(),
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

    private Work[] works;

    private WorkGridElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.style("width: 100%; height:88vh;"));
        HtmlContentBuilder<HTMLDivElement> table = div().style("overflow: hidden; height: 85vh; width: 100%; height: 60vh; border-bottom: 1px solid #AAA;").add(div().style("border-top: 1px solid #AAA;").add(elemSheet));
        e.add(table);
        menu.with(element());
        menu.on(EventType.mouseleave, evt->menu.close());
    }
    public Work[] values(){
        Data[] data = elemSheet.values();
        for(int i = 0; i < works.length; i++){
            String json = "{";
            if(!data[i].get("Na Conc(pg/ul)").isEmpty())                 json += "\"naConc\":"       +data[i].get("Na Conc(pg/ul)")+",";
            if(!data[i].get("input Conc(ng)").isEmpty())                 json += "\"inputConc\":"    +data[i].get("input Conc(ng)")+",";
            if(!data[i].get("Library Prep").isEmpty())                   json += "\"libPrep\":\""    +data[i].get("Library Prep")+"\",";
            if(!data[i].get(WorkModel.ConcTape.id).isEmpty())            json += "\"libConcT\":"     +data[i].get(WorkModel.ConcTape.id)+",";
            if(!data[i].get(WorkModel.ConcQubit.id).isEmpty())           json += "\"libConc\":"      +data[i].get(WorkModel.ConcQubit.id)+",";
            if(!data[i].get(WorkModel.FragSize.id).isEmpty())             json += "\"fragSize\":"     +data[i].get(WorkModel.FragSize.id)+",";
            if(!data[i].get(WorkModel.NM.id).isEmpty())                  json += "\"convert\":"      +data[i].get(WorkModel.NM.id)+",";
            if(!data[i].get(WorkModel.Assuming.id).isEmpty())                  json += "\"assuming\":"     +data[i].get(WorkModel.Assuming.id)+",";
            if(!data[i].get(WorkModel.Dilution.id).isEmpty())                 json += "\"dilution\":"     +data[i].get(WorkModel.Dilution.id)+",";
            if(!data[i].get(WorkModel.Volume.id).isEmpty())                  json += "\"totalVol\":"     +data[i].get(WorkModel.Volume.id)+",";
            if(!data[i].get(WorkModel.LibraryVolume.id).isEmpty())             json += "\"libVol\":"       +data[i].get(WorkModel.LibraryVolume.id)+",";
            if(!data[i].get(WorkModel.TEBuffer.id).isEmpty())                 json += "\"teBuffer\":"     +data[i].get(WorkModel.TEBuffer.id)+",";
            if(!data[i].get("I7 Index ID").isEmpty())                    json += "\"i7Index\":\""    +data[i].get("I7 Index ID")+"\",";
            if(!data[i].get("I7 Sequence").isEmpty())                    json += "\"i7Seq\":\""      +data[i].get("I7 Sequence")+"\",";
            if(!data[i].get("I5 Index ID").isEmpty())                    json += "\"i5Index\":\""    +data[i].get("I5 Index ID")+"\",";
            if(!data[i].get("I5 Sequence").isEmpty())                    json += "\"i5Seq\":\""      +data[i].get("I5 Sequence")+"\",";
            json = json.substring(0, json.length()-1);
            json += "}";
            works[i].json(json);

        }
        return works;
    }
    public WorkGridElement update(Work[] values){
        this.works = Arrays.stream(values).toArray(Work[]::new);
        return update(Arrays.stream(values).map(this::map).toArray(Data[]::new));
    }
    Promise<Void> initialize(Double assuming, Double dilution, Double volume) {
        ProgressApi.open(false);
        return WorkCalculator.initialize(elemSheet.values(), assuming, dilution, volume)
                .then(data->Promise.resolve(update(data)))
                .then(s->Promise.resolve((Void)null))
                .finally_(ProgressApi::close);
    }
    Promise<Void> calculate() {
        ProgressApi.open(false);
        return WorkCalculator.calculate(elemSheet.values())
                .then(data->Promise.resolve(update(data)))
                .then(s->Promise.resolve((Void)null))
                .finally_(ProgressApi::close);
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
        String[] values = new String[16];
        if(value.json() != null) {
            var json = Js.asPropertyMap(JSON.parse(value.json()));
            values[0]   = json.has("naConc")      ? String.valueOf(json.get("naConc"))                                          : "";
            values[1]   = json.has("inputConc")   ? String.valueOf(json.get("inputConc"))                                       : "";
            values[2]   = json.has("libPrep")     ? String.valueOf(json.get("libPrep")).replace("\"", ""): "";
            values[3]   = json.has("libConcT")    ? String.valueOf(json.get("libConcT"))                                        : "";
            values[4]   = json.has("libConc")     ? String.valueOf(json.get("libConc"))                                         : "";
            values[5]   = json.has("fragSize")    ? String.valueOf(json.get("fragSize"))                                        : "";
            values[6]   = json.has("convert")     ? String.valueOf(json.get("convert"))                                         : "";
            values[7]   = json.has("assuming")    ? String.valueOf(json.get("assuming"))                                        : "";
            values[8]   = json.has("dilution")    ? String.valueOf(json.get("dilution"))                                        : "";
            values[9]   = json.has("totalVol")    ? String.valueOf(json.get("totalVol"))                                        : "";
            values[10]  = json.has("libVol")      ? String.valueOf(json.get("libVol"))                                          : "";
            values[11]  = json.has("teBuffer")    ? String.valueOf(json.get("teBuffer"))                                        : "";
            values[12]  = json.has("i7Index")     ? String.valueOf(json.get("i7Index")).replace("\"", ""): "";
            values[13]  = json.has("i7Seq")       ? String.valueOf(json.get("i7Seq")).replace("\"", "")  : "";
            values[14]  = json.has("i5Index")     ? String.valueOf(json.get("i5Index")).replace("\"", ""): "";
            values[15]  = json.has("i5Seq")       ? String.valueOf(json.get("i5Seq")).replace("\"", "")  : "";
            return new Data(value.worklist() + "$" + value.index())
                    .put("index",                           String.valueOf(value.index()))
                    .put("G-ID",                            value.gid())
                    .put("의뢰번호",                        value.samples())
                    .put("수진자명",                        value.patientName())
                    .put("MRN",                             value.mrns())
                    .put("Na Conc(pg/ul)",                  values[0])
                    .put("input Conc(ng)",                  values[1])
                    .put("Library Prep",                    values[2])
                    .put(WorkModel.ConcTape.id,             values[3])
                    .put(WorkModel.ConcQubit.id,            values[4])
                    .put(WorkModel.FragSize.id,             values[5])
                    .put(WorkModel.NM.id,                   values[6])
                    .put(WorkModel.Assuming.id,             values[7])
                    .put(WorkModel.Dilution.id,             values[8])
                    .put(WorkModel.Volume.id,               values[9])
                    .put(WorkModel.LibraryVolume.id,        values[10])
                    .put(WorkModel.TEBuffer.id,             values[11])
                    .put("I7 Index ID",                     values[12])
                    .put("I7 Sequence",                     values[13])
                    .put("I5 Index ID",                     values[14])
                    .put("I5 Sequence",                     values[15]);
        }else{
            return new Data(value.worklist() + "$" + value.index())
                    .put("index",                           String.valueOf(value.index()))
                    .put("G-ID",                            value.gid())
                    .put("의뢰번호",                        value.samples())
                    .put("수진자명",                        value.patientName())
                    .put("MRN",                             value.mrns())
                    .put("Na Conc(pg/ul)",                  "")
                    .put("input Conc(ng)",                  "")
                    .put("Library Prep",                    "")
                    .put(WorkModel.ConcTape.id,     "")
                    .put(WorkModel.ConcQubit.id,           "")
                    .put(WorkModel.FragSize.id,              "")
                    .put(WorkModel.NM.id,                   "")
                    .put(WorkModel.Assuming.id,                   "")
                    .put(WorkModel.Dilution.id,                  "")
                    .put(WorkModel.Volume.id,                   "")
                    .put(WorkModel.LibraryVolume.id,              "")
                    .put(WorkModel.TEBuffer.id,                  "")
                    .put("I7 Index ID",                     "")
                    .put("I7 Sequence",                     "")
                    .put("I5 Index ID",                     "")
                    .put("I5 Sequence",                     "");
        }
    }

    @Override
    public WorkGridElement that() {
        return this;
    }
}