package com.greencross.lims.api;

import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;
import com.greencross.lims.dto.Promise;

@UtilityClass
public class WorkApi {
    public Promise<Response> works(String worklist){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][]{
                new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
        });
        request.setMethod("GET");

        return FetchApi.request("/worklist/"+worklist+"/works", request).then(response->{
            if(!response.ok) return response.text().then(msg->{
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        });
    }
}
