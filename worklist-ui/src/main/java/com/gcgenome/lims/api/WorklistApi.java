package com.gcgenome.lims.api;

import com.gcgenome.lims.data.Worklist;
import com.gcgenome.lims.dto.Query;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;
import static elemental2.core.Global.encodeURI;

@UtilityClass
public class WorklistApi {
    public Promise<Response> search(Query query){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
        });
        request.setMethod("GET");

        StringBuilder urlBuilder = new StringBuilder("/worklist/search");
        urlBuilder.append("?page=").append(query.page())
                .append("&limit=").append(query.limit())
                .append("&sort_by=").append(query.sortBy)
                .append("&asc=").append(query.asc);

        if(query.filters!=null && query.filters.length > 0) urlBuilder.append("&filters=").append(encodeURI(JSON.stringify(query.filters)));

        return FetchApi.request(urlBuilder.toString(), request).then(response -> {
            if (!response.ok) return response.text().then(msg -> {
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        });
    }
    public Promise<String> batchCurrent(){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1; charset=utf-8"}
        });
        return FetchApi.request("/worklist/batches/current", request).then(response -> {
            if (!response.ok) return response.text().then(msg -> {
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return response.text();
        });
    }
    public Promise<Integer> max(String batch){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][] {
                new String[] {"Content-Type", "application/vnd.avoid.v1; charset=utf-8"}
        });
        return FetchApi.request("/worklist/batches/" + batch + "/max", request).then(response -> {
            if (!response.ok) return response.text().then(msg -> {
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return response.text();
        }).then(txt->Promise.resolve(Integer.parseInt(txt)));
    }
    public Promise<Response> merge(Worklist worklist){
        RequestInit request = RequestInit.create();
        request.setHeaders(new String[][]{
                new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
        });
        request.setMethod("PUT");
        request.setBody(JSON.stringify(worklist));
        return FetchApi.request("/worklist/"+worklist.id(), request).then(response->{
            if (!response.ok) return response.text().then(msg -> {
                DomGlobal.alert("알 수 없는 에러로 저장을 실패했습니다. LIMS팀에 문의해주세요.");
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        });
    }
}
