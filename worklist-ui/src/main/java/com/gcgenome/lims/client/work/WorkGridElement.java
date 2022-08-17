package com.gcgenome.lims.client.work;

import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.data.Work;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.promise.Promise;
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
                    ColumnBuilder.link("의뢰번호", data->"#"+data.idx()).name("ID").readOnly(true).horizontal("center")
                            .onClick(data->{
                                DomGlobal.window.open("../sample.html#"+data.get("의뢰번호"));
                            }).build(),
                    column("수진자명").readOnly(true).build(),
                    column("MRN").readOnly(true).build(),
                    column(WorkModel.ConcNa.id).name(WorkModel.ConcNa.label).horizontal("right").build(),
                    column(WorkModel.ConcInput.id).name(WorkModel.ConcInput.label).horizontal("right").build(),
                    column(WorkModel.LibPrep.id).name(WorkModel.LibPrep.label).build(),
                    column(WorkModel.ConcTape.id).name(WorkModel.ConcTape.label).horizontal("right").build(),
                    column(WorkModel.ConcQubit.id).name(WorkModel.ConcQubit.label).horizontal("right").build(),
                    column(WorkModel.FragSize.id).name(WorkModel.FragSize.label).horizontal("right").build(),
                    column(WorkModel.NM.id).name(WorkModel.NM.label).horizontal("right").readOnly(true).build(),
                    column(WorkModel.Dilution.id).name(WorkModel.Dilution.label).name(WorkModel.Dilution.label).build(),
                    column(WorkModel.Volume.id).name(WorkModel.Volume.label).horizontal("right").readOnly(true).build(),
                    column(WorkModel.LibraryVolume.id).name(WorkModel.LibraryVolume.label).name(WorkModel.LibraryVolume.label).build(),
                    column(WorkModel.TEBuffer.id).name(WorkModel.TEBuffer.label).horizontal("right").readOnly(true).build(),
                    ColumnBuilder.dropdown(WorkModel.QC.id,
                            ListElement.singleLine().label("Pass"),
                            ListElement.singleLine().label("Fail"))
                            .color("#FFFFFF").colorBackground((td, row, prop, value)->{
                                if("Pass".equalsIgnoreCase(value)) return "#007B5F";
                                else return "#AD1742";
                            }).name(WorkModel.QC.name()).horizontal("center").build(),
                    column(WorkModel.Address.id).name(WorkModel.Address.label).readOnly(true).build(),
                    column(WorkModel.IndexI7.id).name(WorkModel.IndexI7.label).build(),
                    column(WorkModel.SequenceI7.id).name(WorkModel.SequenceI7.label).readOnly(true).build(),
                    column(WorkModel.IndexI5.id).name(WorkModel.IndexI5.label).build(),
                    column(WorkModel.SequenceI5.id).name(WorkModel.SequenceI5.label).readOnly(true).build());
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
            works[i].concNa         = toDouble(data[i].get(WorkModel.ConcNa.id));
            works[i].concInput      = toDouble(data[i].get(WorkModel.ConcInput.id));
            works[i].libPrep        = data[i].get(WorkModel.LibPrep.id);
            works[i].libConcTape    = toDouble(data[i].get(WorkModel.ConcTape.id));
            works[i].libConcQubit   = toDouble(data[i].get(WorkModel.ConcQubit.id));
            works[i].fragmentSize   = toDouble(data[i].get(WorkModel.FragSize.id));
            works[i].amount         = toDouble(data[i].get(WorkModel.NM.id));
            works[i].dilution       = toDouble(data[i].get(WorkModel.Dilution.id));
            works[i].volume         = toDouble(data[i].get(WorkModel.Volume.id));
            works[i].libVolume      = toDouble(data[i].get(WorkModel.LibraryVolume.id));
            works[i].bufferVolume   = toDouble(data[i].get(WorkModel.TEBuffer.id));
            works[i].qc             = "Pass".equalsIgnoreCase(data[i].get(WorkModel.QC.id));
            works[i].address        = data[i].get(WorkModel.Address.id);
            works[i].indexI7        = data[i].get(WorkModel.IndexI7.id);
            works[i].sequenceI7     = data[i].get(WorkModel.SequenceI7.id);
            works[i].indexI5        = data[i].get(WorkModel.IndexI5.id);
            works[i].sequenceI5     = data[i].get(WorkModel.SequenceI5.id);
        }
        return works;
    }
    public Promise<WorkGridElement> update(Work[] values){
        this.works = Arrays.stream(values).toArray(Work[]::new);
        return Promise.resolve(update(Arrays.stream(values).map(this::map).toArray(Data[]::new)));
    }
    Promise<Void> initialize(Double dilution, Double volume) {
        ProgressApi.open(false);
        return WorkCalculator.initialize(elemSheet.values(), dilution, volume)
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
    Promise<Void> indexing(String plate) {
        ProgressApi.open(false);
        return WorkCalculator.indexing(elemSheet.values(), plate)
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
    private Double toDouble(String text) {
        if(text == null || text.trim().isEmpty()) return null;
        return Double.parseDouble(text);
    }
    private String toString(Object value) {
        if(value==null) return "";
        else return String.valueOf(value);
    }
    private Data map(Work value){
        if(value == null) return null;
        return new Data(value.worklist + "$" + value.index)
                .put("index",                           String.valueOf(value.index))
                .put("G-ID",                            value.gid)
                .put("의뢰번호",                         value.samples!=null?DataTransformUtil.formatSampleId(Long.parseLong(value.samples)):null)
                .put("수진자명",                         value.patientName)
                .put("MRN",                             value.mrns)
                .put(WorkModel.ConcNa.id,               toString(value.concNa))
                .put(WorkModel.ConcInput.id,            toString(value.concInput))
                .put(WorkModel.LibPrep.id,              value.libPrep)
                .put(WorkModel.ConcTape.id,             toString(value.libConcTape))
                .put(WorkModel.ConcQubit.id,            toString(value.libConcQubit))
                .put(WorkModel.FragSize.id,             toString(value.fragmentSize))
                .put(WorkModel.NM.id,                   toString(value.amount))
                .put(WorkModel.Dilution.id,             toString(value.dilution))
                .put(WorkModel.Volume.id,               toString(value.volume))
                .put(WorkModel.LibraryVolume.id,        toString(value.libVolume))
                .put(WorkModel.TEBuffer.id,             toString(value.bufferVolume))
                .put(WorkModel.QC.id,                   value.qc!=null?(value.qc?"Pass":"Fail"):"Pass")
                .put(WorkModel.Address.id,              toString(value.address))
                .put(WorkModel.IndexI7.id,              toString(value.indexI7))
                .put(WorkModel.SequenceI7.id,           toString(value.sequenceI7))
                .put(WorkModel.IndexI5.id,              toString(value.indexI5))
                .put(WorkModel.SequenceI5.id,           toString(value.sequenceI5));
    }

    @Override
    public WorkGridElement that() {
        return this;
    }
}