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
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import net.sayaya.ui.BreadcumbElement;
import net.sayaya.ui.ButtonElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.jboss.elemento.Elements.label;

public class WorkScene extends AbstractScene<WorkScene> {
    public static WorkScene build(Query query) { return new WorkScene(query);}
    private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Avoid");
    private final WorkGridElement grid = WorkGridElement.build();
    private final ButtonElement btnSequencing = ButtonElement.outline().css("button");
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
        btnSequencing.onClick(evt->{});
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
                new IsElement<?>[] { btnSequencing }
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
