package com.gcgenome.lims.api;

import com.gcgenome.lims.data.Analysis;
import com.gcgenome.lims.dto.Query;
import elemental2.dom.Blob;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

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
		urlBuilder.append("?page=").append(query.page)
				.append("&limit=").append(query.limit)
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
	public Promise<Response> print(String sample, String service, String lang){
		RequestInit request = RequestInit.create();
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
		});
		request.setMethod("PUT");

		return FetchApi.request("/samples/"+sample+"/services/"+service+"/print/"+lang, request)
				.then(response -> {
					if (!response.ok) return response.text().then(msg -> {
						DomGlobal.alert(msg);
						return Promise.reject(msg);
					}); else return Promise.resolve(response);
				});
	}
	public Promise<Blob> download(String sample, String service, String report) {
		return download("/samples/" + sample + "/services/" + service + "/reports/" + report);
	}
	public Promise<Blob> download(String url) {
		RequestInit request = RequestInit.create();
		request.setMethod("GET");
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "application/vnd.avoid.v1; charset=utf-8"}
		});

		return FetchApi.request(url, request)
				.then(Response::blob)
				.then(blob->Promise.resolve(blob.slice(0, blob.size, "application/pdf")));
	}
	public Promise<Analysis> pdf(String sample, String service){
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
		});
		return FetchApi.request("/samples/"+sample+"/services/"+service+"/print", request)
				.then(Response::text)
				.then(r->{
					if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Analysis)JSON.parse(r));
					else return Promise.resolve((Analysis)null);
				});
	}
	public Promise<Response> publish(String sample, String service, String createAt){
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setHeaders(new String[][] {
				new String[] {"Content-Type", "application/vnd.avoid.v1+json; charset=utf-8"}
		});
		return FetchApi.request("/samples/"+sample+"/services/"+service+"/reports/"+createAt+"/publish", request)
				.then(response-> {
					if (!response.ok) return response.text().then(msg -> {
						DomGlobal.alert(msg);
						return Promise.reject(msg);
					});
					else return Promise.resolve(response);
				});
	}
}
