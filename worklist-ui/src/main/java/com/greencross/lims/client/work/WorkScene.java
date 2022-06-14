package com.greencross.lims.client.work;

import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.api.WorkApi;
import com.greencross.lims.api.WorklistApi;
import com.greencross.lims.client.AbstractScene;
import com.greencross.lims.client.AbstractScenePageable;
import com.greencross.lims.client.BreadcrumbElement;
import com.greencross.lims.client.Router;
import com.greencross.lims.client.worklist.WorklistScene;
import com.greencross.lims.data.Work;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.*;
import elemental2.promise.Promise;
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
    private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Avoid");
    private final WorkGridElement grid = WorkGridElement.build();
    private final ButtonElement btnSequencing = ButtonElement.outline().css("button").text("Sequence!").before(IconElement.icon(IconElement.Type.Regular, "fa-running"));
    private final ButtonElement btnAccept = ButtonElement.outline().css("button").text("계산").before(IconElement.icon(IconElement.Type.Regular, "fa-calculator"));
    private final ButtonElement btnSave = ButtonElement.outline().css("button").text("저장").before(IconElement.icon(IconElement.Type.Regular, "fa-save"));
    private final ButtonElement btnBack = ButtonElement.outline().css("button").text("Exit").before(IconElement.icon(IconElement.Type.Regular, "fa-external-link-alt"));
    private final TextFieldElement<Double> iptAssuming = TextFieldElement.<Double>numberBox().outlined().css("button").text("Assuming a Mr");
    private final TextFieldElement<Double> iptnMOfDilution = TextFieldElement.<Double>numberBox().outlined().css("button").text("nM of dilution");
    private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
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
        btnAccept.onClick(this::accept);
    }

    private void sequence(Event event) {
    }

    private void accept(Event event) {
        ButtonElementText ok = ButtonElement.outline().text("적용");
        ButtonElementText cancel = ButtonElement.outline().text("취소");
        Dialog dialog = Dialog.alert("기존 입력 값이 초기화 됩니다. 진행합니까?", ok, cancel);
        body().add(dialog);

        ok.onClick(evt->{
            dialog.close();
            dialog.element().remove();
            grid.calculate(iptAssuming.value(), iptnMOfDilution.value());
        });
        cancel.onClick(evt->{
            dialog.close();
            dialog.element().remove();
        });

        dialog.open();
    }

    private void back(Event event) {
        Router.location("", true);
    }

    private void update(Query query){
        ProgressApi.open(false);
        WorkApi.works(hash).then(Response::json).then(json-> Promise.resolve((Work[]) json))
                .then(json->{
                    return Promise.resolve(json);
                })
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
                new IsElement<?>[] { iptAssuming, iptnMOfDilution, btnAccept },
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
