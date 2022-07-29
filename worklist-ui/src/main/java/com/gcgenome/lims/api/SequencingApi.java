package com.gcgenome.lims.api;

import com.gcgenome.lims.data.Index;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SequencingApi {
    public Promise<Index[]> indices(String plate){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1; charset=utf-8"}
        });
        return FetchApi.request("/plates/" + plate + "/indices", request).then(response -> {
            if(!response.ok) return response.text().then(msg->{
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        }).then(Response::json).then(json-> Promise.resolve((Index[]) json));
    }
}
