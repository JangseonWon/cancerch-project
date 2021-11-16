package com.greencross.lims.client.worklist;

import com.greencross.lims.data.Request;
import elemental2.dom.*;
import net.sayaya.ui.chart.*;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;
import static org.jboss.elemento.EventType.bind;

public class WorkDetailTopGridElement extends HTMLElementBuilder<HTMLDivElement, WorkDetailTopGridElement> implements HasSelectionChangeHandlers<Optional<Request>> {
    public static WorkDetailTopGridElement instance() { return new WorkDetailTopGridElement(div()); }
    private enum COLUMN_KEY {
        RECEIPT, ID, TYPE, CUSTOMER_NAME, MRN, PATIENT_NAME, PATIENT_CODE, SEX, SPECIMEN_TYPE, END_DT, REMARK, INFO
    }
    private final HtmlContentBuilder<HTMLLabelElement> lblEmpty = label("Sample list is not present yet. Review by F5 OR Change to-from Date.").style("text-align: center; align-self: center; width: 100%;");
    private final SheetElement sheet;
    private final SheetElement.SheetConfiguration config;
    private final HtmlContentBuilder<HTMLDivElement> _this;
    private final Map<String, Request> values = new HashMap<>();
    private WorkDetailTopGridElement(HtmlContentBuilder<HTMLDivElement> e){
        super(e.css("top_grid"));
        _this = e;
        this.config = SheetElement.builder();
        this.sheet = config.build();

        layout();
    }
    private void layout() {
        SheetElementSelectableMulti.header(sheet);

        config.columns(
                ColumnBuilder.string(COLUMN_KEY.RECEIPT.name()).name("접수일").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.ID.name()).name("검체번호").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.TYPE.name()).name("검사명").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.CUSTOMER_NAME.name()).name("의뢰기관").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.MRN.name()).name("MRN").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.PATIENT_NAME.name()).name("수진자명").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.PATIENT_CODE.name()).name("수진자코드").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.SEX.name()).name("성별").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.SPECIMEN_TYPE.name()).name("검체종류").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.END_DT.name()).name("완료예정일").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.REMARK.name()).name("REMARK").width(100).build(),
                ColumnBuilder.string(COLUMN_KEY.INFO.name()).name("검체정보").width(100).build()
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
    public WorkDetailTopGridElement append(Request sample){
        sheet.append(map(sample));
        onUpdateSheet();

        return that();
    }
    public WorkDetailTopGridElement delete(Request sample){
        sheet.delete(sample.sample().toString());
        onUpdateSheet();

        return that();
    }
    public WorkDetailTopGridElement value(Request[] sample){
        this.values.clear();
        sheet.clear();
        sheet.values(Arrays.stream(sample)
                .peek(m->this.values.put(m.dateRequest(), m))
                .peek(m->this.values.put(String.valueOf(m.sample()), m))
                .peek(m->this.values.put(m.serviceName(), m))
                .peek(m->this.values.put(m.customerName(), m))
                .peek(m->this.values.put(m.mrn(), m))
                .peek(m->this.values.put(m.name(), m))
                .peek(m->this.values.put(m.code(), m))
                .peek(m->this.values.put(m.sex(), m))
                .peek(m->this.values.put(m.type(), m))
                .peek(m->this.values.put(m.dateEnd(), m))
                .peek(m->this.values.put(m.remark(), m))
                .peek(m->this.values.put(m.info(), m))
                .map(WorkDetailTopGridElement::map)
                .toArray(Data[]::new));
        onUpdateSheet();
        return that();
    }
    private static Data map(Request value) {
        if(value == null) return null;
        return new Data(String.valueOf(value.sample()))
                .put(COLUMN_KEY.RECEIPT.name(), value.dateRequest())
                .put(COLUMN_KEY.ID.name(), String.valueOf(value.sample()))
                .put(COLUMN_KEY.TYPE.name(),  String.valueOf(value.serviceName()))
                .put(COLUMN_KEY.CUSTOMER_NAME.name(), value.customerName())
                .put(COLUMN_KEY.MRN.name(), value.mrn())
                .put(COLUMN_KEY.PATIENT_NAME.name(), value.name())
                .put(COLUMN_KEY.PATIENT_CODE.name(), value.code())
                .put(COLUMN_KEY.SEX.name(), value.sex())
                .put(COLUMN_KEY.SPECIMEN_TYPE.name(), value.type())
                .put(COLUMN_KEY.END_DT.name(), String.valueOf(value.dateEnd()))
                .put(COLUMN_KEY.REMARK.name(), value.remark())
                .put(COLUMN_KEY.INFO.name(), value.info());

    }
    public WorkDetailTopGridElement refresh() {
        sheet.refresh();
        return that();
    }
    @Override
    public Optional<Request> selection() {
        return Arrays.stream(sheet.values())
                .filter((d) -> d.state() == Data.DataState.SELECTED)
                .map(d->values.get(d.idx()))
                .findAny();
    }

    @Override
    public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Optional<Request>> listener) {
        return onSelectionChange(sheet.element(), listener);
    }

    @Override
    public HandlerRegistration onSelectionChange(EventTarget dom, SelectionChangeEventListener<Optional<Request>> listener) {
        EventListener wrapper = evt->listener.handle(SelectionChangeEvent.event(evt, selection()));
        return bind(dom, "selection-change", wrapper);
    }

    @Override
    public WorkDetailTopGridElement that() {
        return this;
    }
}