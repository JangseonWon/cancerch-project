package com.greencross.lims.client;

import com.google.gwt.core.client.EntryPoint;
import com.greencross.lims.api.WorklistApi;
import com.greencross.lims.data.Worklist;
import org.jboss.elemento.Elements;

import java.util.*;

import static org.jboss.elemento.Elements.div;

public class Application implements EntryPoint {
    private final ControllerElement elemController = ControllerElement.instance();
    private final WorkGridElement elemWorkGrid = WorkGridElement.instance();
    private final HashMap<String, Worklist> selected = new HashMap<>();
    @Override
    public void onModuleLoad() {
        Elements.body()
                .add(div().css("top")
                    .add(elemController)
                    .add(div().css("layout")
                            .add(elemWorkGrid.css("layout-item"))
                    )
                );

        update();
        WorklistApi.WorklistEvent.listen()
                .onCreate(evt->elemWorkGrid.append(evt.value()))
                .onDelete(evt->elemWorkGrid.delete(evt.value()));
    }
    private void update(){
        WorklistApi.findWorklist().then(worklists->{
            elemWorkGrid.value(worklists);
            return null;
        });
    }
}
