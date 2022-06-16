package com.greencross.lims.api;

import com.greencross.lims.data.Preprocessing;
import com.greencross.lims.data.Work;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;
import com.greencross.lims.dto.Promise;

import static elemental2.core.Global.JSON;

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
    public Promise<Response> merge(String worklistId, Preprocessing[] works){
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
