package com.gcgenome.lims.client;

import com.gcgenome.lims.client.work.WorkScene;
import com.gcgenome.lims.client.worklist.WorklistScene;
import com.gcgenome.lims.dto.Query;

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
        } else {
            work.parent(param).update();
            return work;
        }
    }

    @Override
    protected String toParentUrl(String param) {
        if(param == null || param.isEmpty() || param.endsWith(".html")) return "액체생검/Worklist";
        else return "액체생검/Worklist/" + param;
    }
}
