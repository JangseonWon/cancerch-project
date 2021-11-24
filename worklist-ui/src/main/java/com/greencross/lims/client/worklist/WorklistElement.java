package com.greencross.lims.client.worklist;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.api.WorklistApi;
import com.greencross.lims.client.AbstractScenePageable;
import com.greencross.lims.client.ControllerElement;
import com.greencross.lims.client.Router;
import com.greencross.lims.data.Worklist;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcumbElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.CheckBoxElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class WorklistElement extends AbstractScenePageable<WorklistElement>{
    private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Worklist");
    private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
        RouteApi.location("", true, false);
    }).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
            .add("RnD", evt->{
                evt.preventDefault();
                evt.stopPropagation();
            }).add("Worklist", evt->{
                evt.preventDefault();
                evt.stopPropagation();
                Router.location("", true);
            });

    private final WorkGridElement grid = WorkGridElement.instance();
    private final ControllerElement controller = ControllerElement.instance().hidden(false);
    private final ButtonElement Add = ButtonElement.outline().css("button").text("Add List").before(IconElement.icon(IconElement.Type.Regular, "fa-plus"));
    private final ButtonElement Del = ButtonElement.outline().css("button").text("Del List").before(IconElement.icon(IconElement.Type.Regular, "fa-minus"));
    private final ButtonElement Save = ButtonElement.outline().css("button").text("Save All").before(IconElement.icon(IconElement.Type.Regular, "fa-check"));
    private final ButtonElement Close = ButtonElement.outline().css("button").text("Close Worklist").before(IconElement.icon(IconElement.Type.Regular, "fa-door-closed"));
    private final ButtonElement Detail = ButtonElement.outline().css("button").text("Detail").before(IconElement.icon(IconElement.Type.Regular, "fa-eye"));
    private final CheckBoxElement CloseChecker = CheckBoxElement.checkBox(true).text("View Open Worklist");
    private final Query query = new Query().sortBy("sample").asc(false).limit(10).page(0);
    private Boolean addFlag = false;
    private Boolean chgFlag = false;

    public WorklistElement(Query query){
        super(query);
        Add.onClick(evt->add());
        Del.onClick(evt->del());
        Save.onClick(evt->save());
        Close.onClick(evt->close());
        Detail.onClick(evt->detail());
        CloseChecker.onValueChange(evt->{
           update();
        });
        controller.add(div().add(Detail).add(Save).add(Add).add(Del));

        grid.onSelectionChange(evt -> {
            Scheduler.get().scheduleFixedDelay(()->{
                grid.refresh();
                return false;
            }, 200);
        });
    }

    private void close(){
        String id = grid.selection().get().id();
        if(id != null)
            WorklistApi.closeWorklist(id).then(response ->{
                update();
                return null;
            });
    }
    private void add(){
        Worklist worklist = new Worklist();
        worklist.id("").no(grid.getLastIndex()).title("").sample(0.0).comment("").state("open").createdBy("").createdAt("");
        grid.append(worklist);
        addFlag = true;
    }
    private void del(){
        grid.selection().map(Worklist::id).ifPresent(WorklistApi::deleteWorklist);
        grid.selection().ifPresent(grid::delete);
    }
    private void save() {
        Worklist[] chgWorklists = grid.save();
        Worklist[] addWorklists = grid.added();
        chgFlag = chgWorklists.length > 0;

        if(chgFlag) {
            if (checker(chgWorklists)) {
                WorklistApi.update(chgWorklists).then(response -> {
                    update();
                    return null;
                });
            } else DomGlobal.alert("기존 Row의 Title, Sample 컬럼이 공백입니다.");
        }
        if(addFlag){
            if(checker(addWorklists))
                WorklistApi.save(addWorklists).then(response -> {
                    update();
                    return null;
                });
            else DomGlobal.alert("신규 Row의 변경사항이 없거나 Title, Sample 컬럼이 공백입니다.");
        }

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
        String id = grid.selection().get().no().toString();
        if(!grid.selection().get().state().equals("close")) {
            DomGlobal.location.assign("worklist.html#"+id);
        }
        else{
            DomGlobal.location.assign("worklist.html#"+id);
        }
    }

    //region #1 Override Method
    @Override
    public WorklistElement that() {
        return this;
    }
    @Override
    protected IconElement icon() { return IconElement.icon(IconElement.Type.Light, "fa-clipboard-list"); }

    @Override
    protected HtmlContentBuilder<HTMLLabelElement> title() { return title; }

    @Override
    protected BreadcumbElement breadcumb() { return breadcumb; }

    @Override
    protected IsElement<?>[][] controls() {
        return new IsElement[][]{
                new IsElement<?>[]{ CloseChecker },
                new IsElement<?>[]{ controller }
        };
    }
    @Override
    protected IsElement<?> grid() { return grid; }
    @Override
    public void update() {
        ProgressApi.open();
        Set<Query.Filter> filters = new HashSet<>();
        boolean chk = CloseChecker.value();
        WorklistApi.findWorklist(query.sortBy("no").asc(false).filters(filters.stream().toArray(Query.Filter[]::new)),chk).then(worklists->{
            WorklistApi.getNo().then(index->{
                grid.values(Double.parseDouble(index.toString()), worklists);
                return null;
            });
            return null;
        });
        addFlag = false;
    }
//    private void update(){
//        boolean chk = CloseChecker.value();
//        WorklistApi.findWorklist(chk).then(worklists->{
//            WorklistApi.getNo().then(index->{
//                grid.values(Double.parseDouble(index.toString()), worklists);
//                return null;
//            });
//            return null;
//        });
//        addFlag = false;
//    }

    //endregion
}
