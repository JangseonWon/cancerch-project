package com.gcgenome.lims.api;

import com.gcgenome.lims.data.AlisResponse;
import com.gcgenome.lims.data.Report;
import elemental2.dom.*;
import elemental2.promise.Promise;
import jsinterop.base.Js;
import lombok.experimental.UtilityClass;
import net.sayaya.ui.event.HasValueChangeHandlers;

import static elemental2.core.Global.JSON;

@UtilityClass
public class SampleApi {
    public Promise<Boolean> print(String sample, String service, String batch, String row, String lang){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
        });
        request.setMethod("PUT");

        return FetchApi.request("/samples/"+sample+"/services/"+service+"/batch/"+batch+"/row/"+row+"/print/"+lang, request)
                .then(result->{
                    if(result.ok) {
                        return Promise.resolve(true);
                    }
                    else return Promise.resolve(false);
                });
    }
    public Promise<Blob> downloadAllReports(String sample, String service) {
        RequestInit request = RequestInit.create();
        request.setMethod("GET");
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1; charset=utf-8"}
        });
        return FetchApi.request("/samples/"+sample+"/services/"+service+"/reportTotal", request)
                .then(Response::blob);
    }
    public Promise<String> getLogs(String sample, String service) {
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
        });
        return FetchApi.request("/samples/"+sample+"/services/"+service+"/log", request)
                .then(Response::text);
    }
    public Promise<Blob> download(String sample, String service, String report) {
        return download("/samples/" + sample + "/services/" + service + "/reports/" + report);
    }
    public Promise<Blob> download(String url) {
        RequestInit request = RequestInit.create();
        request.setMethod("GET");
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1; charset=utf-8"}
        });

        return FetchApi.request(url, request)
                .then(Response::blob)
                .then(blob->Promise.resolve(blob.slice(0, blob.size, "application/pdf")));
    }
    public Promise<Boolean> publish(String sample, String service, String createAt){
        RequestInit request = RequestInit.create();
        request.setMethod("PUT");
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
        });
        return FetchApi.request("/samples/"+sample+"/services/"+service+"/reports/"+createAt+"/publish", request)
                .then(response-> {
                    if(response.ok) {
                        return Promise.resolve(true);
                    }
                    else return Promise.resolve(false);
                });
    }
    public Promise<Response> works(){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
        });
        return FetchApi.request("/samples/works", request)
                .then(response->{
                    if (!response.ok) return response.text().then(msg -> {
                        DomGlobal.alert(msg);
                        return Promise.reject(msg);
                    });
                    else return Promise.resolve(response);
                });
    }
    public static class PublishEvent{
        private static EventSource listener;
        public static void close(){
            if(listener!=null) listener.close();
        }
        public static PublishEvent listen(){
            if(listener!=null) listener.close();
            PublishEvent instance = new PublishEvent();
            instance.listener().then(evt->{
                listener = evt;
                return null;
            });
            return instance;
        }
        private HasValueChangeHandlers.ValueChangeEventListener<AlisResponse> finishCallback;

        public PublishEvent onFinish(HasValueChangeHandlers.ValueChangeEventListener<AlisResponse> callback){
            finishCallback = callback;
            return this;
        }
        private Promise<EventSource> listener(){
            return FetchApi.url("/samples/publishoutcome").then(url->{
                EventSource src = new EventSource(url);
                src.addEventListener("cancerch", evt->{
                    String json = (String) Js.asPropertyMap(evt).get("data");
                    AlisResponse response = (AlisResponse) JSON.parse(json);
                    if(finishCallback != null) finishCallback.handle(HasValueChangeHandlers.ValueChangeEvent.event(evt, response));
                });
                return Promise.resolve(src);
            });
        }
    }
    public static class PrintEvent {
        private static EventSource listener;
        public static void close(){
            if(listener!=null) listener.close();
        }
        public static PrintEvent listen(){
            if(listener!=null) listener.close();
            PrintEvent instance = new PrintEvent();
            instance.listener().then(evt->{
                listener = evt;
                return null;
            });
            return instance;
        }
        private HasValueChangeHandlers.ValueChangeEventListener<Report> createCallback;
        private HasValueChangeHandlers.ValueChangeEventListener<Report> updateCallback;
        private HasValueChangeHandlers.ValueChangeEventListener<Report> finishCallback;

        public PrintEvent onCreate(HasValueChangeHandlers.ValueChangeEventListener<Report> callback) {
            createCallback = callback;
            return this;
        }
        public PrintEvent onUpdate(HasValueChangeHandlers.ValueChangeEventListener<Report> callback) {
            updateCallback = callback;
            return this;
        }
        public PrintEvent onFinish(HasValueChangeHandlers.ValueChangeEventListener<Report> callback) {
            finishCallback = callback;
            return this;
        }
        private Promise<EventSource> listener(){
            return FetchApi.url("/samples/queue")
                    .then(url->{
                        EventSource src = new EventSource(url);
                        src.addEventListener("CREATE", evt->{
                            String json = (String) Js.asPropertyMap(evt).get("data");
                            Report report = (Report) JSON.parse(json);
                            if(createCallback != null) createCallback.handle(HasValueChangeHandlers.ValueChangeEvent.event(evt, report));
                        });
                        src.addEventListener("PRINTING", evt->{
                            String json = (String) Js.asPropertyMap(evt).get("data");
                            Report report = (Report) JSON.parse(json);
                            if(updateCallback != null) updateCallback.handle(HasValueChangeHandlers.ValueChangeEvent.event(evt, report));
                        });
                        src.addEventListener("PUBLISH", evt->{
                            String json = (String) Js.asPropertyMap(evt).get("data");
                            Report report = (Report) JSON.parse(json);
                            if(updateCallback != null) updateCallback.handle(HasValueChangeHandlers.ValueChangeEvent.event(evt, report));
                        });
                        src.addEventListener("FINISH", evt->{
                            String json = (String) Js.asPropertyMap(evt).get("data");
                            Report report = (Report) JSON.parse(json);
                            if(finishCallback != null) finishCallback.handle(HasValueChangeHandlers.ValueChangeEvent.event(evt, report));
                        });
                        return Promise.resolve(src);
                    });
        }
    }
}
