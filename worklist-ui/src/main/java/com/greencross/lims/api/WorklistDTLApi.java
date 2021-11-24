package com.greencross.lims.api;

import com.greencross.lims.data.Request;
import com.greencross.lims.data.Work;
import com.greencross.lims.dto.Promise;
import elemental2.core.JsDate;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WorklistDTLApi {
    public Promise<Work[]> findWork(String id){
        return FetchApi.request("/worklist/work/"+id)
                .then(Response::json)
                .then(r->Promise.resolve((Work[]) r));
    }
    public Promise<Request[]> findSample(JsDate yesterday, JsDate today){
        return FetchApi.request("/worklist/sample/"+yesterday.toISOString()+"&"+today.toISOString())
                .then(Response::json)
                .then(r->Promise.resolve((Request[]) r));
    }
}
