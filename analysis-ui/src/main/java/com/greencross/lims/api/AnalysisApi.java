package com.greencross.lims.api;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.Promise;
import com.greencross.lims.dto.Query;
import com.greencross.lims.dto.Slice;
import elemental2.dom.Blob;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import lombok.experimental.UtilityClass;

import static elemental2.core.Global.JSON;

@UtilityClass
public class AnalysisApi {
	public Promise<Slice<Analysis>> analysis(Query query) {
		RequestInit request = RequestInit.create();
		request.setMethod("POST");
		request.setHeaders(new String[][] {
				new String[] {"Accept", "application/json"},
				new String[] {"Content-Type", "application/json"}
		});
		ProgressApi.open(false);
		request.setBody(JSON.stringify(query));
		return FetchApi.request("/analysis", request)
					   .then(Response::text)
					   .then(r->{
						   ProgressApi.close();
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Slice<Analysis>)JSON.parse(r));
						   else return Promise.resolve((Slice<Analysis>)null);
					   });
	}
	public Promise<Analysis> save(long sample, String service, String info) {
		RequestInit request = RequestInit.create();
		request.setMethod("PATCH");
		request.setHeaders(new String[][] {new String[] {"Accept", "application/json"}});
		request.setBody(info);
		return FetchApi.request("/analysis/samples/" + sample + "/" + service + "/etc", request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Analysis)JSON.parse(r));
						   else return Promise.resolve((Analysis)null);
					   });
	}
	public Promise<Analysis> pdf(long sample, String service) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setHeaders(new String[][] {new String[] {"Accept", "application/json"}});
		return FetchApi.request("/samples/" + sample + "/services/" + service + "/print", request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Analysis)JSON.parse(r));
						   else return Promise.resolve((Analysis)null);
					   });
	}
	public Promise<Analysis> publish(long sample, String service) {
		RequestInit request = RequestInit.create();
		request.setMethod("PUT");
		request.setHeaders(new String[][] {new String[] {"Accept", "application/json"}});
		return FetchApi.request("/samples/" + sample + "/services/" + service + "/publish", request)
					   .then(Response::text)
					   .then(r->{
						   if(r!=null && !r.trim().isEmpty()) return Promise.resolve((Analysis)JSON.parse(r));
						   else return Promise.resolve((Analysis)null);
					   });
	}
	public Promise<Blob> download(long sample, String service, long report) {
		return download("/samples/" + sample + "/services/" + service + "/reports/" + report);
	}
	public Promise<Blob> download(String url) {
		return FetchApi.request(url, null)
					   .then(Response::blob)
					   .then(blob->Promise.resolve(blob.slice(0, blob.size, "application/pdf")));
	}
}
