package com.greencross.lims.api;

import com.greencross.lims.data.Request;
import com.greencross.lims.data.Work;
import com.greencross.lims.dto.Promise;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WorklistDTLApi {
    public Promise<Work[]> findWork(String id){
        return FetchApi.request("/worklist/"+id)
                .then(Response::json)
                .then(r->Promise.resolve((Work[]) r));
    }
    public Promise<Request[]> findSample(){
        return FetchApi.request("/work/sample")
                .then(Response::json)
                .then(r->Promise.resolve((Request[]) r));
    }
}
