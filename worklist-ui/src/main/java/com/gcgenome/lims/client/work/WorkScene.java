package com.gcgenome.lims.client.work;

import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.RouteApi;
import com.gcgenome.lims.api.WorkApi;
import com.gcgenome.lims.client.AbstractScene;
import com.gcgenome.lims.client.Router;
import com.gcgenome.lims.data.Preprocessing;
import com.gcgenome.lims.data.Work;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.ui.IconElement;
import elemental2.dom.*;
import elemental2.promise.Promise;
import net.sayaya.ui.*;
import net.sayaya.ui.TextFieldElement.TextFieldOutlined;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;

import static org.jboss.elemento.Elements.*;

public class WorkScene extends AbstractScene<WorkScene> {
    public static WorkScene build(Query query) { return new WorkScene(query);}
    private final HtmlContentBuilder<HTMLLabelElement> title    = label().add("Avoid");
    private final WorkGridElement grid                          = WorkGridElement.build();
    private final ButtonElement btnSequencing                   = ButtonElement.outline().css("button").text("Sequence!").before(IconElement.icon(IconElement.Type.Regular, "fa-running"));
    private final ButtonElement btnAccept                       = ButtonElement.outline().css("button").text("적용");
    private final ButtonElement btnCalc                         = ButtonElement.outline().css("button").text("계산").before(IconElement.icon(IconElement.Type.Regular, "fa-calculator"));
    private final ButtonElement btnSave                         = ButtonElement.outline().css("button").text("저장").before(IconElement.icon(IconElement.Type.Regular, "fa-save"));
    private final ButtonElement btnBack                         = ButtonElement.outline().css("button").text("Exit").before(IconElement.icon(IconElement.Type.Regular, "fa-external-link-alt"));
    private final TextFieldElement<Double, TextFieldOutlined<Double>> iptAssuming          = TextFieldElement.numberBox().outlined().css("button", "input").style("height:36px;").text("Assuming a Mr");
    private final TextFieldElement<Double, TextFieldOutlined<Double>> iptnMOfDilution      = TextFieldElement.numberBox().outlined().css("button", "input").style("height:36px;").text("nM of dilution");
    private final TextFieldElement<Double, TextFieldOutlined<Double>> iptLibraryVol        = TextFieldElement.numberBox().outlined().css("button", "input").style("height:36px;").text("Library Volume");
    private final BreadcrumbElement breadcumb                    = BreadcrumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
                RouteApi.location("Worklist", true, false);
            }).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
            .add("Worklist", evt->{
                evt.preventDefault();
                evt.stopPropagation();
                Router.location("", true);
            });
    private final Query query;
    private String hash;
    public WorkScene(Query query) {
        super(query);
        this.query = query;
        initialize();
        btnSequencing.onClick(this::sequence);
        btnBack.onClick(this::back);
        btnCalc.onClick(this::calc);
        btnSave.onClick(this::save);
        btnAccept.onClick(this::accept);
    }

    private void sequence(Event event) {
        this.dialog("시퀀싱을 수행합니다.").then(result->{
            if(result){ }
            return null;
        });
    }
    private void accept(Event event) {
        if(iptAssuming.value() <= 0 || iptAssuming.value().isNaN()) DomGlobal.alert("잘못 입력된 숫자입니다.");
        else this.dialog("기존 정보가 모두 지워집니다.").then(result-> {
            if (result) return grid.initialize(iptAssuming.value(), iptnMOfDilution.value(), iptLibraryVol.value());
            else return Promise.reject(false);
        });
    }
    private void calc(Event event){
        this.dialog("기존 정보가 모두 지워집니다.").then(result->{
            if(result) return grid.calculate();
            else return Promise.reject(false);
        });
    }
    private void save(Event event){
        this.dialog("현재 상태를 저장합니다.")
                .then(result->{
                    if(!result) return Promise.reject(false);
                    Preprocessing[] data = Arrays.stream(grid.values()).map(datum->{
                        Preprocessing tmp = new Preprocessing();
                        tmp.id(datum.worklist()+"$"+datum.index()).json(datum.json());
                        return tmp;
                    }).toArray(Preprocessing[]::new);
                    ProgressApi.open(false);
                    return WorkApi.merge(hash, data);
                }).then(result2-> {
                    if (!result2.ok) return Promise.reject(false);
                    DomGlobal.alert("저장이 완료됬습니다.");
                    return WorkApi.works(hash);
                }).then(Response::json)
                .then(json-> Promise.resolve((Work[]) json))
                .then(works->Promise.resolve(grid.update(works)))
                .finally_(ProgressApi::close);
    }
    private void back(Event event) {
        this.dialog("저장하지 않은 정보는 초기화됩니다.").then(result->{
            if(result){ Router.location("", true); }
            return null;
        });
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

    private void update(Query query){
        ProgressApi.open(false);
        WorkApi.works(hash).then(Response::json).then(json-> Promise.resolve((Work[]) json))
                .then(works->{
                    grid.update(works);
                    return null;
                }).finally_(ProgressApi::close);
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
        return breadcumb.that();
    }
    @Override
    protected IsElement<?>[][] controls() {
        return new IsElement[][]{
                new IsElement<?>[] { div().add(iptAssuming).add(iptnMOfDilution).add(iptLibraryVol).add(btnAccept).style("display:flex;")},
                new IsElement<?>[] { btnCalc },
                new IsElement<?>[] { btnSequencing },
                new IsElement<?>[] { btnSave, btnBack }
        };
    }
    @Override
    protected IsElement<?>[] contents() {
        return new IsElement<?>[]{ grid };
    }
    @Override
    public void update() {
        update(query);
    }
    public WorkScene parent(String hash){
        this.hash = hash;
        while(breadcumb.element().childElementCount > 3) ((HTMLElement)breadcumb.element().childNodes.getAt(3)).remove();
        breadcumb.add(hash, evt->{
            evt.preventDefault();
            evt.stopPropagation();
            Router.location("", false);
        });
        return that();
    }

    @Override
    public WorkScene that() {
        return this;
    }
}
