package com.greencross.lims.client.worklist;

import com.greencross.lims.data.Work;
import elemental2.dom.EventListener;
import elemental2.dom.EventTarget;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.SheetElementSelectableMulti;
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
        NO, ID, BARCODE, BATCH, REQ_NUM, TEST_TYPE, TEST_CODE, PAT_NAME, SAMPLE, CUS_NAME, CUS_CODE, END_DT, DNA_PREP, DNA_METHOD, EXT_DT, DNA_CONC, DNA_VOL, DW_VOL, TOT_AMT, LIB_PREP,
        LIB_METHOD, INDEX, LIB_CONC, FRAG_SIZE, MOL, BPS, LIB_PMOL, MULTIPLE, POOL_VOL, F_POOL_VOL, ELUT_VOL
    }
    private final HtmlContentBuilder<HTMLLabelElement> lblEmpty = label("Work is not present yet. Pick Barcode or Typing Sample Number").style("text-align: center; align-self: center; width: 100%;");
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

    // region # layout / column
    private void layout() {
        SheetElementSelectableMulti.header(sheet);

        config.columns(
                ColumnBuilder.string(COLUMN_KEY.NO.name()).width(40).name("No").horizontal("center").build(),
                ColumnBuilder.string(COLUMN_KEY.ID.name()).width(40).name("Id").build(),
                ColumnBuilder.string(COLUMN_KEY.BARCODE.name()).width(250).name("barcode").build(),
                ColumnBuilder.string(COLUMN_KEY.BATCH.name()).width(80).name("batch").build(),
                ColumnBuilder.string(COLUMN_KEY.SAMPLE.name()).width(80).name("의뢰번호").build(),
                ColumnBuilder.string(COLUMN_KEY.TEST_TYPE.name()).width(80).name("검사종류").build(),
                ColumnBuilder.string(COLUMN_KEY.TEST_CODE.name()).width(300).name("검사코드").build(),
                ColumnBuilder.string(COLUMN_KEY.PAT_NAME.name()).width(80).name("수진자명").build(),
                ColumnBuilder.string(COLUMN_KEY.CUS_NAME.name()).width(80).name("의뢰기관").build(),
                ColumnBuilder.string(COLUMN_KEY.CUS_CODE.name()).width(100).name("기관등록번호").build(),
                ColumnBuilder.string(COLUMN_KEY.END_DT.name()).width(80).name("결과예정일").build(),
                ColumnBuilder.string(COLUMN_KEY.DNA_PREP.name()).width(80).name("DNA Prep Kit").build(),
                ColumnBuilder.string(COLUMN_KEY.DNA_METHOD.name()).width(80).name("DNA METHOD").build(),
                ColumnBuilder.string(COLUMN_KEY.EXT_DT.name()).width(80).name("추출일자").build(),
                ColumnBuilder.string(COLUMN_KEY.DNA_CONC.name()).width(80).name("DNA Conc").build(),
                ColumnBuilder.string(COLUMN_KEY.DNA_VOL.name()).width(80).name("DNA VOL.").build(),
                ColumnBuilder.string(COLUMN_KEY.DW_VOL.name()).width(80).name("DW Vol.").build(),
                ColumnBuilder.string(COLUMN_KEY.TOT_AMT.name()).width(80).name("Total Amount").build(),
                ColumnBuilder.string(COLUMN_KEY.LIB_PREP.name()).width(80).name("LIB Prep Kit").build(),
                ColumnBuilder.string(COLUMN_KEY.LIB_METHOD.name()).width(80).name("LIB Method").build(),
                ColumnBuilder.string(COLUMN_KEY.INDEX.name()).width(80).name("Index No").build(),
                ColumnBuilder.string(COLUMN_KEY.LIB_CONC.name()).width(80).name("LIB Conc").build(),
                ColumnBuilder.string(COLUMN_KEY.FRAG_SIZE.name()).width(80).name("Fragment Size").build(),
                ColumnBuilder.string(COLUMN_KEY.MOL.name()).width(80).name("1mol of library(ng)").build(),
                ColumnBuilder.string(COLUMN_KEY.BPS.name()).width(80).name("Batch per sample").build(),
                ColumnBuilder.string(COLUMN_KEY.LIB_PMOL.name()).width(80).name("LiB pmol").build(),
                ColumnBuilder.string(COLUMN_KEY.MULTIPLE.name()).width(80).name("Multiple").build(),
                ColumnBuilder.string(COLUMN_KEY.POOL_VOL.name()).width(80).name("Polling vol").build(),
                ColumnBuilder.string(COLUMN_KEY.F_POOL_VOL.name()).width(80).name("Final Pooling Vol").build(),
                ColumnBuilder.string(COLUMN_KEY.ELUT_VOL.name()).width(80).name("Elution vol").build()

        ).stretchH("all");
    }
    //endregion

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

    //region #Sheet value function
    public WorkDetailBotGridElement value(Work... work){
        this.values.clear();
        sheet.values(Arrays.stream(work)
                .peek(m->this.values.put(String.valueOf(m.no()), m))
                .peek(m->this.values.put(m.id(), m))
                .peek(m->this.values.put(String.valueOf(m.barcode()), m))
                .peek(m->this.values.put(m.batch(), m))
                .peek(m->this.values.put(m.reqNum(), m))
                .peek(m->this.values.put(m.testType(), m))
                .peek(m->this.values.put(m.testCode(), m))
                .peek(m->this.values.put(m.patName(), m))
                .peek(m->this.values.put(String.valueOf(m.sample()), m))
                .peek(m->this.values.put(m.cusName(), m))
                .peek(m->this.values.put(m.cusCode(), m))
                .peek(m->this.values.put(m.endDt(), m))
                .peek(m->this.values.put(m.dnaPrep(), m))
                .peek(m->this.values.put(m.dnaMethod(), m))
                .peek(m->this.values.put(m.extDt(), m))
                .peek(m->this.values.put(String.valueOf(m.dnaConc()), m))
                .peek(m->this.values.put(String.valueOf(m.dnaVol()), m))
                .peek(m->this.values.put(String.valueOf(m.dwVol()), m))
                .peek(m->this.values.put(String.valueOf(m.totAmt()), m))
                .peek(m->this.values.put(m.libPrep(), m))
                .peek(m->this.values.put(m.libMethod(), m))
                .peek(m->this.values.put(String.valueOf(m.index()), m))
                .peek(m->this.values.put(String.valueOf(m.libConc()), m))
                .peek(m->this.values.put(String.valueOf(m.fragSize()), m))
                .peek(m->this.values.put(String.valueOf(m.mol()), m))
                .peek(m->this.values.put(String.valueOf(m.bps()), m))
                .peek(m->this.values.put(String.valueOf(m.libPmol()), m))
                .peek(m->this.values.put(String.valueOf(m.multiple()), m))
                .peek(m->this.values.put(String.valueOf(m.fPoolVol()), m))
                .peek(m->this.values.put(String.valueOf(m.elutVol()), m))
                .map(WorkDetailBotGridElement::map)
                .toArray(Data[]::new));
        onUpdateSheet();
        return that();
    }
    //endregion
    public int getIndex(){return index;}
    private static Data map(Work value) {
        if(value == null) return null;
        return new Data(value.id())
                .put(COLUMN_KEY.NO.name(), String.valueOf(value.no()))
                .put(COLUMN_KEY.ID.name(), value.id())
                .put(COLUMN_KEY.BATCH.name(), value.batch())
                .put(COLUMN_KEY.SAMPLE.name(), String.valueOf(value.sample()))
                .put(COLUMN_KEY.BARCODE.name(), String.valueOf(value.barcode()))
                .put(COLUMN_KEY.TEST_TYPE.name(), value.testType())
                .put(COLUMN_KEY.TEST_CODE.name(), value.testCode())
                .put(COLUMN_KEY.REQ_NUM.name(), value.reqNum())
                .put(COLUMN_KEY.PAT_NAME.name(), value.patName())
                .put(COLUMN_KEY.CUS_NAME.name(), value.cusName())
                .put(COLUMN_KEY.CUS_CODE.name(), value.cusCode())
                .put(COLUMN_KEY.END_DT.name(), value.endDt().split("T")[0])
                .put(COLUMN_KEY.DNA_PREP.name(), value.dnaPrep())
                .put(COLUMN_KEY.DNA_METHOD.name(), value.dnaMethod())
                .put(COLUMN_KEY.EXT_DT.name(), value.extDt())
                .put(COLUMN_KEY.DNA_CONC.name(), String.valueOf(value.dnaConc()))
                .put(COLUMN_KEY.DNA_VOL.name(), String.valueOf(value.dnaVol()))
                .put(COLUMN_KEY.DW_VOL.name(), String.valueOf(value.dwVol()))
                .put(COLUMN_KEY.TOT_AMT.name(), String.valueOf(value.totAmt()))
                .put(COLUMN_KEY.LIB_PREP.name(), String.valueOf(value.libPrep()))
                .put(COLUMN_KEY.LIB_METHOD.name(), value.libMethod())
                .put(COLUMN_KEY.INDEX.name(), String.valueOf(value.index()))
                .put(COLUMN_KEY.LIB_CONC.name(), String.valueOf(value.libConc()))
                .put(COLUMN_KEY.FRAG_SIZE.name(), String.valueOf(value.fragSize()))
                .put(COLUMN_KEY.MOL.name(), String.valueOf(value.mol()))
                .put(COLUMN_KEY.BPS.name(), String.valueOf(value.bps()))
                .put(COLUMN_KEY.LIB_PMOL.name(), String.valueOf(value.libPmol()))
                .put(COLUMN_KEY.MULTIPLE.name(), String.valueOf(value.multiple()))
                .put(COLUMN_KEY.F_POOL_VOL.name(), String.valueOf(value.fPoolVol()))
                .put(COLUMN_KEY.ELUT_VOL.name(), String.valueOf(value.elutVol()));
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