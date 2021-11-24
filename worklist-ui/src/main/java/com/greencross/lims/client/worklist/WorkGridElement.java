package com.greencross.lims.client.worklist;

import com.greencross.lims.data.Worklist;
import elemental2.dom.*;
import elemental2.dom.EventListener;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElementSelectableSingle;
import net.sayaya.ui.chart.column.ColumnBuilder;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.*;
import java.util.stream.Stream;

import static org.jboss.elemento.Elements.*;
import static org.jboss.elemento.EventType.bind;

class WorkGridElement extends HTMLElementBuilder<HTMLDivElement, WorkGridElement> implements HasSelectionChangeHandlers<Optional<Worklist>> {
    public static WorkGridElement instance() { return new WorkGridElement(div()); }
    private enum COLUMN_KEY {
        ID, NO, TITLE, CREATOR, SAMPLE, STATE, COMMENT, CREATED
    }
    private final HtmlContentBuilder<HTMLLabelElement> lblEmpty = label("Worklist is not present yet. Create Worklist.").style("text-align: center; align-self: center; width: 100%;");
    private final SheetElement sheet;
    private final SheetElement.SheetConfiguration config;
    private final HtmlContentBuilder<HTMLDivElement> _this;
    private final Map<String, Worklist> values = new HashMap<>();
    private Double index = 0.0;
    private WorkGridElement(HtmlContentBuilder<HTMLDivElement> e){
        super(e.css("worklist"));
        _this = e;
        this.config = SheetElement.builder();
        this.sheet = config.build();

        layout();
    }
    private void layout() {
        SheetElementSelectableSingle.header(sheet);

        config.columns(
                ColumnBuilder.string(COLUMN_KEY.NO.name()).width(40).name("No").horizontal("center").readOnly(true).build(),
                ColumnBuilder.string(COLUMN_KEY.TITLE.name()).width(250).name("Title").build(),
                ColumnBuilder.string(COLUMN_KEY.CREATOR.name()).width(80).name("Creator").horizontal("center").readOnly(true).build(),
                ColumnBuilder.string(COLUMN_KEY.SAMPLE.name()).width(80).name("Sample").horizontal("center").build(),
                ColumnBuilder.string(COLUMN_KEY.STATE.name()).width(80).name("State").horizontal("center").readOnly(true).build(),
                ColumnBuilder.string(COLUMN_KEY.COMMENT.name()).width(300).name("Comment").build(),
                ColumnBuilder.string(COLUMN_KEY.CREATED.name()).width(80).name("Created_at").horizontal("center").readOnly(true).build()
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

    public Double getLastIndex(){ return ++index; }
    public WorkGridElement append(Worklist worklist){
        this.values.put(worklist.id(), worklist);
        Data convert = map(worklist);
        sheet.values(Stream.concat(Stream.of(convert), Arrays.stream(sheet.values())).toArray(Data[]::new)).refresh();
        onUpdateSheet();

        return that();
    }
    public WorkGridElement delete(Worklist worklist){
        this.values.remove(worklist.id());
        sheet.delete(worklist.id());
        onUpdateSheet();

        return that();
    }
    public WorkGridElement values(Double index, Worklist... worklists){
        this.index = index;
        this.values.clear();
        sheet.clear();
        sheet.values(Arrays.stream(worklists)
                .peek(m->this.values.put(m.id(), m))
                .peek(m->this.values.put(String.valueOf(m.no()), m))
                .peek(m->this.values.put(m.title(), m))
                .peek(m->this.values.put(m.createdBy(), m))
                .peek(m->this.values.put(String.valueOf(m.sample()), m))
                .peek(m->this.values.put(m.state(), m))
                .peek(m->this.values.put(m.comment(), m))
                .peek(m->this.values.put(m.createdAt(), m))
                .map(WorkGridElement::map)
                .toArray(Data[]::new));

        onUpdateSheet();
        return that();
    }
    public Worklist[] added() {
        return Arrays.stream(sheet.values())
                .filter((d)->(d.isChanged(COLUMN_KEY.COMMENT.name())
                        ||d.isChanged(COLUMN_KEY.TITLE.name())
                        ||d.isChanged(COLUMN_KEY.SAMPLE.name()))
                        && d.get(COLUMN_KEY.CREATED.name()).equals("")
                )
                .map(WorkGridElement::map)
                .toArray(Worklist[]::new);
    }
    public Worklist[] save() {
        return Arrays.stream(sheet.values())
                .filter((d)->(d.isChanged(COLUMN_KEY.COMMENT.name())
                        ||d.isChanged(COLUMN_KEY.TITLE.name())
                        ||d.isChanged(COLUMN_KEY.SAMPLE.name()))
                        && !d.get(COLUMN_KEY.CREATED.name()).equals("")
                )
                .map(WorkGridElement::map)
                .toArray(Worklist[]::new);
    }
    public WorkGridElement refresh() {
        sheet.refresh();
        return that();
    }
    private static Data map(Worklist value) {
        if(value == null) return null;

        return new Data(value.id())
                .put(COLUMN_KEY.ID.name(), value.id())
                .put(COLUMN_KEY.NO.name(), String.valueOf(value.no()))
                .put(COLUMN_KEY.TITLE.name(), value.title())
                .put(COLUMN_KEY.CREATOR.name(), value.createdBy())
                .put(COLUMN_KEY.SAMPLE.name(), String.valueOf(value.sample()))
                .put(COLUMN_KEY.STATE.name(), value.state())
                .put(COLUMN_KEY.COMMENT.name(), value.comment())
                .put(COLUMN_KEY.CREATED.name(), value.createdAt().split("T")[0]);
    }

    private static Worklist map(Data value) {
        if(value == null) return null;
        return new Worklist()
                .id(value.get(COLUMN_KEY.ID.name()))
                .no(Double.parseDouble(value.get(COLUMN_KEY.NO.name())))
                .title(value.get(COLUMN_KEY.TITLE.name()))
                .createdBy(value.get(COLUMN_KEY.CREATOR.name()))
                .sample(Double.parseDouble(value.get(COLUMN_KEY.SAMPLE.name())))
                .state(value.get(COLUMN_KEY.STATE.name()))
                .comment(value.get(COLUMN_KEY.COMMENT.name()))
                .createdAt(value.get(COLUMN_KEY.CREATED.name()))
                .activation("TRUE");
    }

    @Override
    public Optional<Worklist> selection() {
        return Arrays.stream(sheet.values())
                .filter((d) -> d.state() == Data.DataState.SELECTED)
                .map(d->values.get(d.idx()))
                .findAny();
    }
    @Override
    public HandlerRegistration onSelectionChange(EventTarget dom, SelectionChangeEventListener<Optional<Worklist>> listener) {
        EventListener wrapper = evt->listener.handle(SelectionChangeEvent.event(evt, selection()));
        return bind(dom, "selection-change", wrapper);
    }
    @Override
    public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Optional<Worklist>> listener) {
        return onSelectionChange(sheet.element(), listener);
    }
    @Override
    public WorkGridElement that() {
        return this;
    }
}
