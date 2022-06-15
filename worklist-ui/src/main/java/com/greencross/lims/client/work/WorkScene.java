package com.greencross.lims.client.work;

import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.api.WorkApi;
import com.greencross.lims.client.AbstractScene;
import com.greencross.lims.client.Router;
import com.greencross.lims.data.Work;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.*;
import com.greencross.lims.dto.Promise;
import net.sayaya.ui.*;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.jboss.elemento.Elements.body;
import static org.jboss.elemento.Elements.label;

public class WorkScene extends AbstractScene<WorkScene> {
    public static WorkScene build(Query query) { return new WorkScene(query);}
    private final HtmlContentBuilder<HTMLLabelElement> title    = label().add("Avoid");
    private final WorkGridElement grid                          = WorkGridElement.build();
    private final ButtonElement btnSequencing                   = ButtonElement.outline().css("button").text("Sequence!").before(IconElement.icon(IconElement.Type.Regular, "fa-running"));
    private final ButtonElement btnAcceptAssuming               = ButtonElement.outline().css("button").text("적용");
    private final ButtonElement btnAcceptDilution               = ButtonElement.outline().css("button").text("적용");
    private final ButtonElement btnCalc                         = ButtonElement.outline().css("button").text("계산").before(IconElement.icon(IconElement.Type.Regular, "fa-calculator"));
    private final ButtonElement btnSave                         = ButtonElement.outline().css("button").text("저장").before(IconElement.icon(IconElement.Type.Regular, "fa-save"));
    private final ButtonElement btnBack                         = ButtonElement.outline().css("button").text("Exit").before(IconElement.icon(IconElement.Type.Regular, "fa-external-link-alt"));
    private final TextFieldElement<Double> iptAssuming          = TextFieldElement.numberBox().outlined().css("button").text("Assuming a Mr");
    private final TextFieldElement<Double> iptnMOfDilution      = TextFieldElement.numberBox().outlined().css("button").text("nM of dilution");
    private final BreadcumbElement breadcumb                    = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
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
        btnAcceptAssuming.onClick(this::acceptAssum);
        btnAcceptDilution.onClick(this::acceptDilut);
    }

    private void sequence(Event event) {
        this.dialog("시퀀싱을 수행합니다.").last(result->{
            if(result){
                //TODO: NOT YET
            }
        });
    }

    private void acceptAssum(Event event) {
        this.dialog("기존 정보가 모두 지워집니다.").last(result->{
            if(result){
                grid.updateAssum(iptAssuming.value());
            }
        });
    }
    private void acceptDilut(Event event) {
        this.dialog("기존 정보가 모두 지워집니다.").last(result->{
            if(result){
                grid.updateDilut(iptnMOfDilution.value());
            }
        });
    }
    private void calc(Event event){
        this.dialog("기존 정보가 모두 지워집니다.").last(result->{
            if(result){
                grid.calculate();
            }
        });
    }
    private void save(Event event){
        this.dialog("기존 정보가 모두 지워집니다.").last(result->{
            if(result){
                //TODO : not yet
            }
        });
    }
    private void back(Event event) {
        this.dialog("저장하지 않은 정보는 초기화됩니다.").last(result->{
            if(result){
                Router.location("", true);
            }
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
                .last(grid::update)
                .finally_(ProgressApi::close);
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
    protected BreadcumbElement breadcumb() {
        return breadcumb.that();
    }

    @Override
    protected IsElement<?>[][] controls() {
        return new IsElement[][]{
                new IsElement<?>[] { iptAssuming,     btnAcceptAssuming },
                new IsElement<?>[] { iptnMOfDilution, btnAcceptDilution },
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
