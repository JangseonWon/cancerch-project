package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.client.Router;
import com.gcgenome.lims.client.work.DataTransformUtil;
import com.gcgenome.lims.data.Worklist;
import com.google.gwt.core.client.JsDate;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetElementSelectableMulti;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnString;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jboss.elemento.Elements.div;

public class WorklistGridElement extends HTMLElementBuilder<HTMLDivElement, WorklistGridElement> implements HasSelectionChangeHandlers<Worklist[]> {
    public static WorklistGridElement build() { return new WorklistGridElement(div());}
    private static ColumnString column(String name) {
        return ColumnBuilder.string(name).name(name).readOnly(true).horizontal("center");
    }
    private final SheetElement.SheetConfiguration config = SheetElement.builder()
            .rowHeaders(false)
            .autoColSize(true)
            .autoRowSize(false)
            .manualColumnMove(true)
            .manualColumnResize(true)
            .stretchH("all")
            .columns(
                    ColumnBuilder.link("Batch#", data->{
                                if(data.get("serial") == null || data.get("serial").trim().isEmpty()) return "#Create";
                                else return "#"+data.get("serial");
                            }).name("Batch#").readOnly(true)
                            .onClick(data->{
                                if(data.get("serial") == null || data.get("serial").trim().isEmpty()) {
                                    CreateBatchDialog.build(data.idx()).onSubmit().then(worklist->{
                                        Router.location(data.idx(), true);
                                        return null;
                                    });
                                }
                                else Router.location(data.idx(), true);
                            }).font("Nanum Gothic Coding").horizontal("center").build(),
                    column("워크리스트 명").horizontal("left").build(),
                    column("추출일").horizontal("center").font("Nanum Gothic Coding").build(),
                    column("상태").build(),
                    column("Comment").horizontal("left").build()
            ).data(new Data[10]);
    private final SheetElement elemSheet = config.build();
    private final SheetElementSelectableMulti selection = SheetElementSelectableMulti.wrap(elemSheet);
    private Map<String, Worklist> map = new HashMap<>();
    private WorklistGridElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.style("width: 100%;"));
        HtmlContentBuilder<HTMLDivElement> table = div().style("overflow: hidden; width: 100%; height: 85vh; border-bottom: 1px solid #AAA;")
                .add(div().style("border-top: 1px solid #AAA;").add(elemSheet));
        e.add(table);
    }
    public WorklistGridElement update(Worklist[] values){
        map.clear();
        return update(Arrays.stream(values).peek(w->map.put(w.id(), w)).map(this::map).toArray(Data[]::new));
    }
    private WorklistGridElement update(Data[] data){
        try {
            elemSheet.values(data).refresh();
            return that();
        } catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private Data map(Worklist value){
        if(value == null) return null;
        String serial = value.serial();
        if(value.serial() == null || value.serial().trim().isEmpty()) serial = "Create";
        return new Data(value.id()).put("serial", value.serial())
                .put("Batch#", serial)
                .put("워크리스트 명", value.title())
                .put("추출일", DataTransformUtil.formatDateTime((long) JsDate.parse(value.created())))
                .put("상태", toString(value.status()))
                .put("Comment", value.remark());
    }
    private String toString(Worklist.Status status) {
        if(status == null) return null;
        switch (status) {
            case NORMAL:        return "Plate 검증 완료";
            case PRE_CREATE:    return "Worklist 생성 완료";
            case POST_CREATE:   return "Plate 생성 완료";
            case DISPOSAL:      return "Worklist 폐기";
            case MERGED:        return "Worklist 병합됨";
            case SEQUENCING:    return "시퀀싱 진행";
            case ANALYZED:      return "분석 완료";
            case COMPLETE:      return "전송 완료";
        }
        return null;
    }
    @Override
    public WorklistGridElement that() {
        return this;
    }

    @Override
    public Worklist[] selection() {
        return Arrays.stream(selection.selection()).map(data->map.get(data.idx())).toArray(Worklist[]::new);
    }
    @Override
    public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Worklist[]> selectionChangeEventListener) {
        return selection.onSelectionChange(element, evt->{
            var selected = Arrays.stream(evt.selection()).map(data->map.get(data.idx())).toArray(Worklist[]::new);
            SelectionChangeEvent<Worklist[]> cast = SelectionChangeEvent.event(evt.event(), selected);
            selectionChangeEventListener.handle(cast);
        });
    }
}