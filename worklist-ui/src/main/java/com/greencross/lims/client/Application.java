package com.greencross.lims.client;

import com.greencross.lims.api.WindowApi;
import com.greencross.lims.client.worklist.WorklistDetailElement;
import com.greencross.lims.client.worklist.WorklistElement;
import com.greencross.lims.dto.Query;
import com.greencross.lims.sheet.column.ColumnBuilderLink;
import elemental2.dom.DomGlobal;

public class Application extends AbstractEntryPoint {
    private WorklistElement worklist;
    private WorklistDetailElement work;
    static {
        ColumnBuilderLink.onopen = link->{
            if("_self()".equalsIgnoreCase(link.target())) DomGlobal.window.location.replace(link.href());
            else WindowApi.open(link.href(), link.target(), null, true);
        };
    }
    @Override
    public AbstractScene<?>[] elements(Query query) {
        worklist = new WorklistElement(query);
        work = new WorklistDetailElement(query);
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
        if(param == null || param.isEmpty() || param.endsWith(".html")) return "RnD/Worklist";
        else return "RnD/Worklist/" + param;
    }
}

//public class Application implements EntryPoint {
//   public void onModuleLoad(){


//       String hash = DomGlobal.window.location.hash;
//       if(hash == null || hash.trim().isEmpty()) Elements.body().add(WorklistElement.instance());
//       else try{
//           String param = hash.substring(1);
//           if("link".equalsIgnoreCase(param)){
//                String[] params = DomGlobal.window.location.search.substring(1).split("#");
//                String[] state = params[0].split("&");
//               Elements.body().add(WorklistDetailElement.instance(params[0], state[1]));
//           }
//       } catch(Exception ignore){
//           Elements.body().add(WorklistElement.instance());
//       }
//   }
//}
