package com.gcgenome.lims.api;

import com.gcgenome.lims.data.Index;
import com.gcgenome.lims.data.Worklist;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class SequencingApi {
    public Promise<Index[]> indices(String plate){
        RequestInit request = RequestInit.create();
        return FetchApi.request("/plates/" + plate + "/indices", request).then(response -> {
            if(!response.ok) return response.text().then(msg->{
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        }).then(Response::json).then(json-> Promise.resolve((Index[]) json));
    }
    public Promise<Void> sequencing(Worklist[] worklists){
        RequestInit request = RequestInit.create();
        request.setMethod("POST");
        String ids = Arrays.stream(worklists).map(Worklist::id).collect(Collectors.joining(","));
        request.setBody(ids);
        return FetchApi.request("/sequencing", request).then(response -> {
            if(!response.ok) return response.text().then(msg->{
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        }).then(r->Promise.resolve((Void)null));
    }
    public Promise<Void> sequencingB(Worklist[] worklists){
        RequestInit request = RequestInit.create();
        request.setMethod("POST");
        String ids = Arrays.stream(worklists).map(Worklist::id).collect(Collectors.joining(","));
        request.setBody(ids);
        return FetchApi.request("/sequencing-b", request).then(response -> {
            if(!response.ok) return response.text().then(msg->{
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        }).then(r->Promise.resolve((Void)null));
    }
}
