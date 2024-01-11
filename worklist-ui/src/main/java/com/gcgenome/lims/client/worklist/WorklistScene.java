package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.RouteApi;
import com.gcgenome.lims.api.SequencingApi;
import com.gcgenome.lims.api.WorklistApi;
import com.gcgenome.lims.client.AbstractScenePageable;
import com.gcgenome.lims.client.Router;
import com.gcgenome.lims.data.Worklist;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.ui.IconElement;
import elemental2.core.JsDate;
import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import net.sayaya.ui.*;
import net.sayaya.ui.TextFieldElement.TextFieldOutlined;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.*;

public class WorklistScene extends AbstractScenePageable<WorklistScene> {
    public static WorklistScene build(Query query) { return new WorklistScene(query); }
    private static JsDate yesterday() {
        JsDate today = new JsDate();
        JsDate yesterday = new JsDate(today);
        yesterday.setDate(yesterday.getDate()-7);
        yesterday.setHours(0, 0, 0, 0);
        return yesterday;
    }
    private static JsDate today(){
        JsDate today = new JsDate();
        today.setHours(23, 59, 59);
        return today;
    }
    private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Cancerch");
    private final WorklistGridElement grid = WorklistGridElement.build();
    private final ButtonElement btnSequencing                   = ButtonElement.outline().css("button").text("Sequence A").before(IconElement.icon(IconElement.Type.Regular, "fa-running"));
    private final ButtonElement btnSequencingB                   = ButtonElement.outline().css("button").text("Sequence B").before(IconElement.icon(IconElement.Type.Regular, "fa-running"));
    private final TextFieldElement<JsDate, TextFieldOutlined<JsDate>> iptDateFrom = TextFieldElement.dateBox().outlined().css("button").style("width: 125px;border-right: 0px !important; height:36px;").text("from").value(yesterday());
    private final TextFieldElement<JsDate, TextFieldOutlined<JsDate>> iptDateTo = TextFieldElement.dateBox().outlined().css("button").style("width: 125px; height:36px;").text("to").value(today());
    private final ButtonElement btnSearch = ButtonElement.outline().css("button").text("검색").before(IconElement.icon(IconElement.Type.Light, "fa-search")).style("display: inline-block;");
    private final BreadcrumbElement breadcrumb = BreadcrumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
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
        btnSearch.onClick(evt->update());
        btnSequencing.onClick(this::sequence);
        btnSequencingB.onClick(this::sequenceB);
    }
    private Promise<Boolean> dialog(String title){
        ButtonElementText ok = ButtonElement.outline().text("OK");
        ButtonElementText cancel = ButtonElement.outline().text("CANCEL");
        Dialog dialog = Dialog.alert(title, ok, cancel);
        body().add(dialog);
        return new Promise<>((resolve, reject)-> {
            ok.onClick(evt -> {
                dialog.close();
                dialog.element().remove();
                resolve.onInvoke(true);
            });
            cancel.onClick(evt -> {
                dialog.close();
                dialog.element().remove();
                reject.onInvoke(false);
            });
            dialog.open();
        });
    }
    private void sequence(Event event) {
        /* 선택한 Batch 내의 검체가 모두 'PENDING, HOLDING' 인지 체크한다. 그리고나서 Promise.then으로 아래 연결 */
        this.dialog("시퀀싱을 수행합니다.").then(result->{
            if(result){
                ProgressApi.open(false);
                SequencingApi.sequencing(grid.selection())
                        .then(e->{
                            DomGlobal.alert("시퀀싱 정보 전송을 완료했습니다.");
                            return Promise.resolve(e);
                        }).finally_(ProgressApi::close);
            }
            return null;
        });
    }
    private void sequenceB(Event event) {
        /* 선택한 Batch 내의 검체가 모두 'PENDING_B, HOLDING_B' 인지 체크한다. 그리고나서 Promise.then으로 아래 연결 */
        this.dialog("시퀀싱을 수행합니다.").then(result->{
            if(result){
                ProgressApi.open(false);
                SequencingApi.sequencingB(grid.selection())
                        .then(e->{
                            DomGlobal.alert("시퀀싱 정보 전송을 완료했습니다.");
                            return Promise.resolve(e);
                        }).finally_(ProgressApi::close);
            }
            return null;
        });
    }
    private void update(Query query){
        Query proxy = new Query().asc(this.isAsc());
        List<Query.Filter> filters = new LinkedList<>();
        if(query.filters!=null){
            Arrays.stream(query.filters).forEach(filter->filter.key(" "));
            Collections.addAll(filters, query.filters);
        }
        filters.add(new Query.Filter().key("domain").value("AVOID"));
        filters.add(new Query.Filter().key("confirmed").value(String.valueOf(true)));
        filters.add(new Query.Filter().key("to").value(String.valueOf(iptDateTo.value().getTime()+86400000)));
        filters.add(new Query.Filter().key("from").value(String.valueOf(iptDateFrom.value().getTime()-32400000)));
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
                .then(worklists->{
                  grid.update(worklists);
                  return Promise.resolve(worklists);
                }).finally_(ProgressApi::close);
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
    protected BreadcrumbElement breadcrumb() {
        return breadcrumb.that();
    }

    @Override
    protected IsElement<?>[][] controls() {
        return new IsElement[][]{
                new IsElement<?>[] { iptDateFrom },
                new IsElement<?>[] { label("~").style("line-height: 36px; margin-left: 2px; margin-right: 2px;")},
                new IsElement<?>[] { div().add(iptDateTo).add(btnSearch).style("display:flex;") },
                new IsElement<?>[] { div().add(btnSequencing).add(btnSequencingB).style("display:flex;") }
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
