package com.greencross.lims.client.worklist;

import com.greencross.lims.data.Worklist;
import elemental2.dom.EventListener;
import elemental2.dom.EventTarget;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.sheet.Data;
import net.sayaya.ui.sheet.SheetElement;
import net.sayaya.ui.sheet.SheetElementSelectableSingle;
import net.sayaya.ui.sheet.column.ColumnBuilder;
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
        NO, ID, REQUEST, STB, REMARK, RECEIPT, TYPE, CODE, PATIENT, SEX, ORGANIZATION, ORG_NUM, COMPLETE, SPECIMEN, COMMENT
    }
    private final HtmlContentBuilder<HTMLLabelElement> lblEmpty = label("Worklist is not present yet. Create Worklist.").style("text-align: center; align-self: center; width: 100%;");
    private final SheetElement sheet;
    private final SheetElement.SheetConfiguration config;
    private final HtmlContentBuilder<HTMLDivElement> _this;
    private final Map<String, Worklist> values = new HashMap<>();
    private WorkDetailTopGridElement(HtmlContentBuilder<HTMLDivElement> e){
        super(e.css("top"));
        _this = e;
        this.config = SheetElement.builder();
        this.sheet = config.build();

        layout();
    }
    private void layout() {
        SheetElementSelectableSingle.header(sheet);

        config.columns(
                ColumnBuilder.string(COLUMN_KEY.NO.name()).width(40).name("No").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.ID.name()).width(250).name("ID").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.REQUEST.name()).width(80).name("REQUEST NUM").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.STB.name()).width(80).name("STB").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.REMARK.name()).width(80).name("REMARK").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.RECEIPT.name()).width(300).name("RECEIPT").build(),
                ColumnBuilder.string(COLUMN_KEY.TYPE.name()).width(80).name("TYPE").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.CODE.name()).width(80).name("CODE").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.PATIENT.name()).width(80).name("PATIENT").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.ORGANIZATION.name()).width(80).name("ORGANIZATION").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.ORG_NUM.name()).width(80).name("ORG_NUM").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.COMPLETE.name()).width(80).name("COMPLETE").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.SPECIMEN.name()).width(80).name("SPECIMEN").align("center").build(),
                ColumnBuilder.string(COLUMN_KEY.COMMENT.name()).width(80).name("COMMENT").align("center").build()

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
                .put(COLUMN_KEY.REQUEST.name(), value.createdBy())
                .put(COLUMN_KEY.STB.name(), String.valueOf(value.sample()))
                .put(COLUMN_KEY.REMARK.name(), value.state())
                .put(COLUMN_KEY.RECEIPT.name(), value.comment())
                .put(COLUMN_KEY.TYPE.name(), value.createdAt().split("T")[0])
                .put(COLUMN_KEY.CODE.name(), value.createdAt().split("T")[0])
                .put(COLUMN_KEY.PATIENT.name(), value.createdAt().split("T")[0])
                .put(COLUMN_KEY.ORGANIZATION.name(), value.createdAt().split("T")[0])
                .put(COLUMN_KEY.ORG_NUM.name(), value.createdAt().split("T")[0])
                .put(COLUMN_KEY.COMPLETE.name(), value.createdAt().split("T")[0])
                .put(COLUMN_KEY.SPECIMEN.name(), value.createdAt().split("T")[0])
                .put(COLUMN_KEY.COMMENT.name(), value.createdAt().split("T")[0]);
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