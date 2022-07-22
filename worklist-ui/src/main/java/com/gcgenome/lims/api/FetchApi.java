package com.gcgenome.lims.api;

import com.google.gwt.dom.client.Document;
import elemental2.core.JsArray;
import elemental2.dom.DomGlobal;
import elemental2.dom.RequestInit;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FetchApi {
	private final String localhost = "http://localhost:56316";
	public Promise<Response> request(String url, RequestInit param) {
		return request(Document.get().createUniqueId(), url, param);
	}
	public Promise<Response> request(String id, String url, RequestInit param) {
		RequestInit proxy = RequestInit.create();
		if(param!=null) {
			proxy.setMethod(param.getMethod());
			if(param.getHeaders()!=null) {
				RequestInit.GetHeadersUnionType header = param.getHeaders();
				JsArray<JsArray<String>> map = header.asJsArray();
				JsArray<String> credential = new JsArray<>();
				credential.push("X-USER-ID", "218923");
				map.push(credential);
				proxy.setHeaders(map);
			}
			if(param.getBody()!=null) proxy.setBody(param.getBody());
		} else if(proxy.getHeaders() == null) proxy.setHeaders(new String[][]{
				new String[] {"X-USER-ID", "218923"}
		});
		return new Promise<>((resolve, reject) -> {
			DomGlobal.fetch(localhost + url, proxy).then(n -> {
				resolve.onInvoke(n);
				return null;
			}).catch_(n -> {
				reject.onInvoke(n);
				return null;
			});
		});
	}
}
