package com.greencross.lims.client.worklist;

import com.greencross.lims.data.Worklist;
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

public class WorkDetailTopGridElement extends HTMLElementBuilder<HTMLDivElement, WorkDetailTopGridElement> implements HasSelectionChangeHandlers<Optional<Worklist>> {
    public static WorkDetailTopGridElement instance() { return new WorkDetailTopGridElement(div()); }
    private enum COLUMN_KEY {
        NO, RECEIPT, ID, TYPE, MRN, PATIENT_NAME, PATIENT_CODE, SEX, SPECIMEN_TYPE, END_DT, TAT
    }
    private final HtmlContentBuilder<HTMLLabelElement> lblEmpty = label("Worklist is not present yet. Create Worklist.").style("text-align: center; align-self: center; width: 100%;");
    private final SheetElement sheet;
    private final SheetElement.SheetConfiguration config;
    private final HtmlContentBuilder<HTMLDivElement> _this;
    private final Map<String, Worklist> values = new HashMap<>();
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
                ColumnBuilder.string(COLUMN_KEY.NO.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.RECEIPT.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.ID.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.TYPE.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.MRN.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.PATIENT_NAME.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.PATIENT_CODE.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.SEX.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.SPECIMEN_TYPE.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.END_DT.name()).build(),
                ColumnBuilder.string(COLUMN_KEY.TAT.name()).build()
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
    public WorkDetailTopGridElement append(Worklist worklist){
        this.values.put(worklist.id(), worklist);
        sheet.append(map(worklist));
        onUpdateSheet();

        return that();
    }
    public WorkDetailTopGridElement delete(Worklist worklist){
        this.values.remove(worklist.id());
        sheet.delete(worklist.id());
        onUpdateSheet();

        return that();
    }
    public WorkDetailTopGridElement value(Worklist... worklists){
        this.values.clear();
        sheet.values(Arrays.stream(worklists)
                .peek(m->this.values.put(m.id(), m))
                .peek(m->this.values.put(String.valueOf(m.no()), m))
                .peek(m->this.values.put(m.title(), m))
                .peek(m->this.values.put(m.createdBy(), m))
                .peek(m->this.values.put(String.valueOf(m.sample()), m))
                .peek(m->this.values.put(m.state(), m))
                .peek(m->this.values.put(m.comment(), m))
                .peek(m->this.values.put(m.createdAt(), m))
                .map(WorkDetailTopGridElement::map)
                .toArray(Data[]::new));
        onUpdateSheet();
        return that();
    }
    private static Data map(Worklist value) {
        if(value == null) return null;
        return new Data(value.id())
                .put(COLUMN_KEY.NO.name(), String.valueOf(value.no()))
                .put(COLUMN_KEY.ID.name(), value.title())
                .put(COLUMN_KEY.RECEIPT.name(), value.comment())
                .put(COLUMN_KEY.TYPE.name(), value.createdAt().split("T")[0]);

    }
    public WorkDetailTopGridElement refresh() {
        sheet.refresh();
        return that();
    }
    @Override
    public Optional<Worklist> selection() {
        return Arrays.stream(sheet.values())
                .filter((d) -> d.state() == Data.DataState.SELECTED)
                .map(d->values.get(d.idx()))
                .findAny();
    }

    @Override
    public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Optional<Worklist>> listener) {
        return onSelectionChange(sheet.element(), listener);
    }

    @Override
    public HandlerRegistration onSelectionChange(EventTarget dom, SelectionChangeEventListener<Optional<Worklist>> listener) {
        EventListener wrapper = evt->listener.handle(SelectionChangeEvent.event(evt, selection()));
        return bind(dom, "selection-change", wrapper);
    }

    @Override
    public WorkDetailTopGridElement that() {
        return this;
    }
}