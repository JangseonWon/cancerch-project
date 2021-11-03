package com.greencross.lims.api;

import com.greencross.lims.data.WorklistDTL;
import com.greencross.lims.dto.Promise;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WorklistDTLApi {
    public Promise<WorklistDTL[]> findWorklistDTL(String id){
        return FetchApi.request("/worklist/"+id)
                .then(Response::json)
                .then(r->Promise.resolve((WorklistDTL[]) r));
    }
}
