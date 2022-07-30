package com.gcgenome.lims.api;

import com.gcgenome.lims.data.Work;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class WorkApi {
    public Promise<Work[]> works(String worklist){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][]{
                new String[] {"Content-Type", "application/vnd.avoid.v1; charset=utf-8"}
        });
        return FetchApi.request("/worklist/"+worklist+"/works", request).then(response->{
            if(!response.ok) return response.text().then(msg->{
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        }).then(Response::json)
        .then(json-> Promise.resolve((Work[]) json));
    }
    public Promise<Response> merge(String worklistId, Work[] works){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][]{
                new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
        });
        request.setMethod("PUT");
        request.setBody(JSON.stringify(works));
        return FetchApi.request("/worklist/"+worklistId+"/works", request).then(response->{
            if (!response.ok) return response.text().then(msg -> {
                DomGlobal.alert("알 수 없는 에러로 저장을 실패했습니다. LIMS팀에 문의해주세요.");
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        });
    }
}
