package com.greencross.lims.client.work;

import com.greencross.lims.client.AbstractScenePageable;
import com.greencross.lims.client.BreadcrumbElement;
import com.greencross.lims.client.Router;
import com.greencross.lims.client.worklist.WorklistScene;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcumbElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import static org.jboss.elemento.Elements.label;

public class WorkScene extends AbstractScenePageable<WorklistScene> {
    public static WorkScene build(Query query) { return new WorkScene(query);}
    private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Avoid");
    private final String hash = DomGlobal.window.location.hash.substring(1);
    private final BreadcrumbElement breadcumb = BreadcrumbElement.build()
            .splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
            .add("Avoid", evt->{
                evt.preventDefault();
                evt.stopPropagation();
                Router.location("worklist.html#Worklist", true);
            }).add(hash, evt-> {
                evt.preventDefault();
                evt.stopPropagation();
                Router.location("worklist.html#" + hash, true);
            });
    public WorkScene(Query query) {
        super(query);

    }

    @Override
    protected IsElement<?> grid() {
        return null;
    }

    @Override
    protected IconElement icon() {
        return null;
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
        return new IsElement[0][];
    }

    @Override
    public void update() {

    }

    @Override
    public WorklistScene that() {
        return null;
    }
}
