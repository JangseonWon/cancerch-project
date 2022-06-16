package com.greencross.lims.client.worklist;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.api.WorklistApi;
import com.greencross.lims.client.AbstractScenePageable;
import com.greencross.lims.client.Router;
import com.greencross.lims.data.Worklist;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.core.JsDate;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import net.sayaya.ui.BreadcumbElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.TextFieldElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class WorklistScene extends AbstractScenePageable<WorklistScene> {
    public static WorklistScene build(Query query) { return new WorklistScene(query); }
    private static JsDate yesterday() {
        JsDate today = new JsDate();
        JsDate yesterday = new JsDate(today);
        yesterday.setDate(yesterday.getDate()-7);
        yesterday.setHours(0, 0, 0, 0);
        return yesterday;
    }
    private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Avoid");
    private final WorklistGridElement grid = WorklistGridElement.build();
    private final TextFieldElement<JsDate> iptDateFrom = TextFieldElement.dateBox().outlined().css("button").style("width: 125px;border-right: 0px !important; height:36px;").text("from").value(yesterday());
    private final TextFieldElement<JsDate> iptDateTo = TextFieldElement.dateBox().outlined().css("button").style("width: 125px; height:36px;").text("to").value(new JsDate());
    private final ButtonElement btnSearch = ButtonElement.outline().css("button").text("검색").before(IconElement.icon(IconElement.Type.Light, "fa-search")).style("display: inline-block;");
    private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
                RouteApi.location("", true, false);
            }).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
            .add("Worklist", evt->{
                evt.preventDefault();
                evt.stopPropagation();
                Router.location("", true);
            });
    private final Query query;

    public WorklistScene(Query query) {
        super(query);
        this.sortable("작성일", "워크리스트 명", "상태").sort("작성일", false);
        this.query = query;
        initialize();
        btnSearch.onClick(evt->update());
        Scheduler.get().scheduleFixedDelay(()->{
            initiailized = true;
            update();
            return false;
        }, 1000);
    }
    boolean initiailized = false;
    private void update(Query query){
        if(!initiailized) return;
        Query proxy = new Query().asc(this.isAsc());
        List<Query.Filter> filters = new LinkedList<>();
        if(query.filters()!=null){
            Arrays.stream(query.filters()).forEach(filter->filter.key(" "));
            Collections.addAll(filters, query.filters());
        }
        filters.add(new Query.Filter().key("domain").value("AVOID"));
        filters.add(new Query.Filter().key("confirmed").value(String.valueOf(true)));
        filters.add(new Query.Filter().key("to").value(String.valueOf(iptDateTo.value().getTime())));
        filters.add(new Query.Filter().key("from").value(String.valueOf(iptDateFrom.value().getTime())));
        if(this.sort()!=null){
            if("워크리스트 명".equalsIgnoreCase(this.sort())) proxy.sortBy("워크리스트 명");
            else if("작성일".equalsIgnoreCase(this.sort())) proxy.sortBy("작성일");
        }else proxy.sortBy("워크리스트 명").asc(false);
        proxy.limit(show()).page((int) page());
        proxy.filters(filters.stream().toArray(Query.Filter[]::new));
        ProgressApi.open(false);
        WorklistApi.search(proxy)
                .then(this::updateTotal)
                .then(Response::text)
                .then(this::map)
                .last(grid::update)
                .finally_(ProgressApi::close);
    }
    private Promise<Response> updateTotal(Response response) {
        total(Long.parseLong(response.headers.get("X-TOTAL-COUNT")));
        return Promise.resolve(response);
    }
    private Promise<Worklist[]> map(String json) {
        if(json!=null && !json.trim().isEmpty()) return Promise.resolve((Worklist[])JSON.parse(json));
        else return Promise.resolve((Worklist[])null);
    }

    @Override
    protected IsElement<?> grid() {
        return grid;
    }
    @Override
    protected IconElement icon() {
        return IconElement.icon(IconElement.Type.Light, "fa-clipboard-list");
    }

    @Override
    protected HtmlContentBuilder<HTMLLabelElement> title() {
        return title;
    }

    @Override
    protected net.sayaya.ui.BreadcumbElement breadcumb() {
        return breadcumb.that();
    }

    @Override
    protected IsElement<?>[][] controls() {
        return new IsElement[][]{
                new IsElement<?>[] { iptDateFrom },
                new IsElement<?>[] { label("~").style("line-height: 36px; margin-left: 2px; margin-right: 2px;")},
                new IsElement<?>[] { div().add(iptDateTo).add(btnSearch).style("display:flex;") }
        };
    }
    @Override
    public void update() {
        update(query);
    }
    @Override
    public WorklistScene that() {
        return this;
    }
}
