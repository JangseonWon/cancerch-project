package com.gcgenome.lims.api;

import com.gcgenome.lims.data.Analysis;
import com.gcgenome.lims.data.Report;
import com.gcgenome.lims.dto.Query;
import elemental2.dom.*;
import elemental2.promise.Promise;
import jsinterop.base.Js;
import lombok.experimental.UtilityClass;
import net.sayaya.ui.event.HasValueChangeHandlers;

import static elemental2.core.Global.JSON;
import static elemental2.core.Global.encodeURI;

@UtilityClass
public class AnalysisApi {
	public Promise<Response> search(Query query){
		RequestInit request = RequestInit.create();
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
		});
		request.setMethod("GET");

		StringBuilder urlBuilder = new StringBuilder("/analysis/search");
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
	public Promise<Response> comment(String sample, String service, String comment){
		RequestInit request = RequestInit.create();
		request.setMethod("PATCH");
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
		});
		request.setBody(comment);
		return FetchApi.request("/analysis/"+sample+"/"+service+"/comment", request)
				.then(response-> {
					if (!response.ok) return response.text().then(msg -> {
						DomGlobal.alert(msg);
						return Promise.reject(msg);
					});
					else return Promise.resolve(response);
				});
	}
	public Promise<Response> update(Analysis[] analyses){
		RequestInit request = RequestInit.create();
		request.setMethod("PATCH");
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
		});
		request.setBody(JSON.stringify(analyses));
		return FetchApi.request("/analysis/update", request)
				.then(response->{
					if (!response.ok) return response.text().then(msg -> {
						DomGlobal.alert(msg);
						return Promise.reject(msg);
					});
					else return Promise.resolve(response);
				});
	}
}
