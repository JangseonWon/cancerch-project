package com.greencross.lims.client.worklist;

import com.greencross.lims.data.Work;
import elemental2.dom.EventListener;
import elemental2.dom.EventTarget;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetElementSelectableSingle;
import net.sayaya.ui.chart.column.ColumnBuilder;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;
import static org.jboss.elemento.EventType.bind;

public class WorkDetailBotGridElement extends HTMLElementBuilder<HTMLDivElement,  WorkDetailBotGridElement> implements HasSelectionChangeHandlers<Optional<Work>> {
    public static  WorkDetailBotGridElement instance() { return new  WorkDetailBotGridElement(div()); }
    private enum COLUMN_KEY {
        NO, BARCODE, BATCH, SAMPLE, TEST_TYPE, TEST_CODE, REQ_NUM, PAT_NAME, CUS_NAME, CUS_CODE, END_DT
    }
    private final HtmlContentBuilder<HTMLLabelElement> lblEmpty = label("Work is not present yet. Select Sample Check and Click Down.").style("text-align: center; align-self: center; width: 100%;");
    private final SheetElement sheet;
    private final SheetElement.SheetConfiguration config;
    private final HtmlContentBuilder<HTMLDivElement> _this;
    private final Map<String, Work> values = new HashMap<>();
    private static int index = 0;
    private  WorkDetailBotGridElement(HtmlContentBuilder<HTMLDivElement> e){
        super(e.css("bot_grid"));
        _this = e;
        this.config = SheetElement.builder();
        this.sheet = config.build();

        layout();
    }
    private void layout() {
        SheetElementSelectableSingle.header(sheet);

        config.columns(
                ColumnBuilder.string(COLUMN_KEY.NO.name()).width(40).name("No").build(),
                ColumnBuilder.string(COLUMN_KEY.BARCODE.name()).width(250).name("barcode").build(),
                ColumnBuilder.string(COLUMN_KEY.BATCH.name()).width(80).name("batch").build(),
                ColumnBuilder.string(COLUMN_KEY.SAMPLE.name()).width(80).name("sample").build(),
                ColumnBuilder.string(COLUMN_KEY.TEST_TYPE.name()).width(80).name("검사종류").build(),
                ColumnBuilder.string(COLUMN_KEY.TEST_CODE.name()).width(300).name("검사코드").build(),
                ColumnBuilder.string(COLUMN_KEY.REQ_NUM.name()).width(80).name("의뢰번호").build(),
                ColumnBuilder.string(COLUMN_KEY.PAT_NAME.name()).width(80).name("수진자명").build(),
                ColumnBuilder.string(COLUMN_KEY.CUS_NAME.name()).width(80).name("의뢰기관").build(),
                ColumnBuilder.string(COLUMN_KEY.CUS_CODE.name()).width(80).name("기관등록번호").build(),
                ColumnBuilder.date(COLUMN_KEY.END_DT.name()).width(80).name("완료예상일").build()
        ).stretchH("all");
    }
    private void onUpdateSheet() {
        if(this.config.data()!=null && this.config.data().length > 0){
            if(this.sheet.element().parentElement == null){
                _this.element().textContent = "";
                _this.add(sheet);
            }
        }else{
            if(this.lblEmpty.element().parentElement == null) {
                _this.element().textContent = "";
                _this.add(lblEmpty);
            }
        }
    }
    public WorkDetailBotGridElement append(Work work){
        this.values.put(work.id(), work);
        sheet.append(map(work));
        onUpdateSheet();

        return that();
    }
    public WorkDetailBotGridElement delete(Work work){
        this.values.remove(work.id());
        sheet.delete(work.id());
        onUpdateSheet();

        return that();
    }
    public WorkDetailBotGridElement value(Work... work){
        this.values.clear();
        sheet.values(Arrays.stream(work)
                .peek(m->this.values.put(m.id(), m))
                .peek(m->this.values.put(String.valueOf(m.no()), m))
                .peek(m->this.values.put(String.valueOf(m.barcode()), m))
                .peek(m->this.values.put(String.valueOf(m.sample()), m))
                .peek(m->this.values.put(String.valueOf(m.testType()), m))
                .peek(m->this.values.put(m.testCode(), m))
                .peek(m->this.values.put(m.reqNum(), m))
                .peek(m->this.values.put(m.patientName(), m))
                .peek(m->this.values.put(m.custermerName(), m))
                .peek(m->this.values.put(m.custermerCode(), m))
                .peek(m->this.values.put(m.endDt(), m))
                .map(WorkDetailBotGridElement::map)
                .toArray(Data[]::new));
        onUpdateSheet();
        return that();
    }
    public int getIndex(){return index;}
    private static Data map(Work value) {
        if(value == null) return null;
        return new Data(value.id())
                .put(COLUMN_KEY.NO.name(), String.valueOf(++index))
                .put(COLUMN_KEY.BATCH.name(), value.batch())
                .put(COLUMN_KEY.SAMPLE.name(), String.valueOf(value.sample()))
                .put(COLUMN_KEY.BARCODE.name(), String.valueOf(value.barcode()))
                .put(COLUMN_KEY.TEST_TYPE.name(), value.testType())
                .put(COLUMN_KEY.TEST_CODE.name(), value.testCode())
                .put(COLUMN_KEY.REQ_NUM.name(), value.reqNum())
                .put(COLUMN_KEY.PAT_NAME.name(), value.patientName())
                .put(COLUMN_KEY.CUS_NAME.name(), value.custermerName())
                .put(COLUMN_KEY.CUS_CODE.name(), value.custermerCode())
                .put(COLUMN_KEY.END_DT.name(), value.endDt().split("T")[0]);
    }
    public WorkDetailBotGridElement refresh() {
        sheet.refresh();
        return that();
    }
    @Override
    public Optional<Work> selection() {
        return Arrays.stream(sheet.values())
                .filter((d) -> d.state() == Data.DataState.SELECTED)
                .map(d->values.get(d.idx()))
                .findAny();
    }

    @Override
    public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Optional<Work>> listener) {
        return onSelectionChange(sheet.element(), listener);
    }

    @Override
    public HandlerRegistration onSelectionChange(EventTarget dom, SelectionChangeEventListener<Optional<Work>> listener) {
        EventListener wrapper = evt->listener.handle(SelectionChangeEvent.event(evt, selection()));
        return bind(dom, "selection-change", wrapper);
    }

    @Override
    public WorkDetailBotGridElement that() {
        return this;
    }
}