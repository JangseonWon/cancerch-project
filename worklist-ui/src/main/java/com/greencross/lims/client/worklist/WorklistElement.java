package com.greencross.lims.client.worklist;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.WorklistApi;
import com.greencross.lims.client.ControllerElement;
import com.greencross.lims.data.Worklist;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HtmlContentBuilder;

import java.time.LocalDate;
import java.util.Arrays;

import static org.jboss.elemento.Elements.div;


public class WorklistElement extends HTMLElementBuilder<HTMLDivElement, WorklistElement> {
    public static WorklistElement instance() { return new WorklistElement(div()); }
    private final WorkGridElement grid = WorkGridElement.instance();
    private final ControllerElement controller = ControllerElement.instance().hidden(false);
    private final ButtonElement Add = ButtonElement.outline().css("button").text("Add List").before(IconElement.icon(IconElement.Type.Regular, "fa-plus"));
    private final ButtonElement Del = ButtonElement.outline().css("button").text("Del List").before(IconElement.icon(IconElement.Type.Regular, "fa-minus"));
    private final ButtonElement Save = ButtonElement.outline().css("button").text("Save List").before(IconElement.icon(IconElement.Type.Regular, "fa-check"));
    private final ButtonElement Close = ButtonElement.outline().css("button").text("Close Worklist").before(IconElement.icon(IconElement.Type.Regular, "fa-door-closed"));
    private final ButtonElement Detail = ButtonElement.outline().css("button").text("Detail").before(IconElement.icon(IconElement.Type.Regular, "fa-eye"));

    public WorklistElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.css("top"));
        Add.onClick(evt->add());
        Del.onClick(evt->del());
        Save.onClick(evt->save());
        Close.onClick(evt->close());
        Detail.onClick(evt->detail());

        e.add(controller.add(div().add(Detail))
                        .add(div().add(Close).add(Add).add(Del).add(Save)))
         .add(div().css("layout")
            .add(grid.css("layout-item")));

        grid.onSelectionChange(evt -> {
            Scheduler.get().scheduleFixedDelay(()->{
                grid.refresh();
                return false;
            }, 200);
        });
        update();
    }
    private void update(){
        WorklistApi.findWorklist().then(worklists->{
            grid.values(worklists);
            return null;
        });
    }
    private void close(){
//        grid.selection().map(Worklist)
    }
    private void add(){
        Double no = grid.getLastIndex();

        Worklist worklist = new Worklist();
        worklist.id("").no(no).title("").sample(0.0).comment("").state("open").createdBy("").createdAt("");
        grid.append(worklist);
    }
    private void del(){
        grid.selection().map(Worklist::id).ifPresent(WorklistApi::deleteWorklist);
        grid.selection().ifPresent(grid::delete);
    }
    private void save() {
        Worklist[] chgWorklists = grid.save();
        Worklist[] addWorklists = grid.added();

        if(checker(chgWorklists)){
//            Arrays.stream(chgWorklists).forEach(t->DomGlobal.console.log(t));
            WorklistApi.update(chgWorklists).then(response -> {
                update();
                return null;
            });
        }
        else DomGlobal.alert("기존 Row의 Title, Sample 컬럼이 공백입니다.");
        if(checker(addWorklists)){
//            Arrays.stream(addWorklists).forEach(t->DomGlobal.console.log(t));
            WorklistApi.save(addWorklists).then(response -> {
                update();
                return null;
            });
        }
        else DomGlobal.alert("신규 Row의 변경사항이 없거나 Title, Sample 컬럼이 공백입니다.");
    }
    private boolean checker(Worklist[] worklists){
        Worklist[] checker = Arrays.stream(worklists)
                .filter(t->t.title().equals("")
                        || t.sample().toString().equals("0"))
                .toArray(Worklist[]::new);
        return worklists.length != 0
                && checker.length == 0;
    }
    private void detail(){
        String id = grid.selection().get().id();
        DomGlobal.location.assign("worklist.html?"+id+"#Link");
    }
    //region #1 Override Method
    @Override
    public WorklistElement that() {
        return this;
    }
    //endregion
}
