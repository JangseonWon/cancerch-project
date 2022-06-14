package com.greencross.lims.client;

import com.greencross.lims.client.work.WorkScene;
import com.greencross.lims.client.worklist.WorklistScene;
import com.greencross.lims.dto.Query;

public class Application extends AbstractEntryPoint {
    private WorklistScene worklist;
    private WorkScene work;
    @Override
    public AbstractScene<?>[] elements(Query query) {
        worklist = WorklistScene.build(query);
        work = WorkScene.build(query);
        return new AbstractScene[] {
                worklist, work
        };
    }

    @Override
    protected AbstractScene<?> prepare(String param) {
        if(param == null || param.isEmpty() || param.endsWith(".html")) {
            worklist.update();
            return worklist;
        } else if(!param.endsWith("Worklist")) {
            work.parent(param).update();
            return work;
        } else {
            worklist.update();
            return worklist;
        }
    }

    @Override
    protected String toParentUrl(String param) {
        if(param == null || param.isEmpty() || param.endsWith(".html")) return "Avoid/Worklist";
        else return "Avoid/Worklist/" + param;
    }
}
