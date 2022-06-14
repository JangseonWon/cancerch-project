package com.greencross.lims.api;

import com.greencross.lims.dto.Promise;
import com.greencross.lims.dto.Query;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
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
                .append("&sort_by=").append(query.sortBy())
                .append("&asc=").append(query.asc());

        if(query.filters()!=null && query.filters().length > 0) urlBuilder.append("&filters=").append(encodeURI(JSON.stringify(query.filters())));

        return FetchApi.request(urlBuilder.toString(), request).then(response -> {
            if (!response.ok) return response.text().then(msg -> {
                DomGlobal.alert(msg);
                return Promise.reject(msg);
            }); else return Promise.resolve(response);
        });
    }
}
