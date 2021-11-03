package com.greencross.lims.api;

import com.greencross.lims.data.Worklist;
import com.greencross.lims.dto.Promise;
import elemental2.dom.EventSource;
import elemental2.dom.Response;
import jsinterop.base.Js;
import lombok.experimental.UtilityClass;
import net.sayaya.ui.event.HasValueChangeHandlers;

import static elemental2.core.Global.JSON;

@UtilityClass
public class WorklistApi {
    public Promise<Worklist[]> findWorklist(){
        return FetchApi.request("/worklist")
                .then(Response::json)
                .then(r->Promise.resolve((Worklist[]) r));
    }

    public static void deleteWorklist(String id) {

    }

    public static class WorklistEvent {
        private static EventSource listener;
        public static WorklistEvent listen(){
            if(listener!=null) listener.close();
            WorklistEvent instance = new WorklistEvent();
            instance.listener().then(evt->{
                listener = evt;
                return null;
            });
            return instance;
        }

        private HasValueChangeHandlers.ValueChangeEventListener<Worklist> createCallback;
        private HasValueChangeHandlers.ValueChangeEventListener<Worklist> updateCallback;
        private HasValueChangeHandlers.ValueChangeEventListener<Worklist> deleteCallback;

        public WorklistEvent onCreate(HasValueChangeHandlers.ValueChangeEventListener<Worklist> callback) {
            createCallback = callback;
            return this;
        }
        public WorklistEvent onUpdate(HasValueChangeHandlers.ValueChangeEventListener<Worklist> callback) {
            updateCallback = callback;
            return this;
        }
        public WorklistEvent onDelete(HasValueChangeHandlers.ValueChangeEventListener<Worklist> callback) {
            deleteCallback = callback;
            return this;
        }

        private Promise<EventSource> listener() {
            return FetchApi.url("/worklist/changes")
                    .then(url->{
                        EventSource src = new EventSource(url);
                        src.addEventListener("CREATE", evt->{
                            String json = (String) Js.asPropertyMap(evt).get("data");
                            Worklist worklist = (Worklist) JSON.parse(json);
                            if(createCallback!=null) createCallback.handle(HasValueChangeHandlers.ValueChangeEvent.event(evt, worklist));
                        });
                        src.addEventListener("UPDATE", evt->{
                            String json = (String)Js.asPropertyMap(evt).get("data");
                            Worklist worklist = (Worklist) JSON.parse(json);
                            if(updateCallback!=null) updateCallback.handle(HasValueChangeHandlers.ValueChangeEvent.event(evt, worklist));
                        });
                        src.addEventListener("DELETE", evt->{
                            String json = (String)Js.asPropertyMap(evt).get("data");
                            Worklist worklist = (Worklist) JSON.parse(json);
                            if(deleteCallback!=null) deleteCallback.handle(HasValueChangeHandlers.ValueChangeEvent.event(evt, worklist));
                        });

                        return Promise.resolve(src);
                    });
        }
    }
}
