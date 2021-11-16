package com.greencross.lims.client.worklist;

import com.greencross.lims.data.Request;
import elemental2.dom.EventListener;
import elemental2.dom.EventTarget;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.*;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.chart.SheetElementSelectableMulti;
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
    private final HtmlContentBuilder<HTMLLabelElement> lblEmpty = label("Worklist is not present yet. Create Worklist.").style("text-align: center; align-self: center; width: 100%;");
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
                ColumnBuilder.string(COLUMN_KEY.RECEIPT.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.ID.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.TYPE.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.CUSTOMER_NAME.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.MRN.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.PATIENT_NAME.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.PATIENT_CODE.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.SEX.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.SPECIMEN_TYPE.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.END_DT.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.REMARK.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.INFO.name()).build()
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
        sheet.values(Arrays.stream(sample)
                .peek(m->this.values.put(m.dateRequest(), m))
                .peek(m->this.values.put(m.sample().toString(), m))
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
        return new Data(value.sample().toString())
                .put(COLUMN_KEY.RECEIPT.name(), String.valueOf(value.dateRequest().split("T")[0]))
                .put(COLUMN_KEY.ID.name(), String.valueOf(value.sample()))
                .put(COLUMN_KEY.TYPE.name(), value.serviceName())
                .put(COLUMN_KEY.CUSTOMER_NAME.name(), value.customerName())
                .put(COLUMN_KEY.MRN.name(), value.mrn())
                .put(COLUMN_KEY.PATIENT_NAME.name(), value.name())
                .put(COLUMN_KEY.PATIENT_CODE.name(), value.code())
                .put(COLUMN_KEY.SEX.name(), value.sex())
                .put(COLUMN_KEY.SPECIMEN_TYPE.name(), value.type())
                .put(COLUMN_KEY.END_DT.name(), value.dateEnd().split("T")[0])
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