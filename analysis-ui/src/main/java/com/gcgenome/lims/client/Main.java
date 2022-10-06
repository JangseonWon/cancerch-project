package com.gcgenome.lims.client;

import com.gcgenome.lims.dto.Query;

public class Main extends AbstractEntryPoint {
	private AnalysisScene elem;
	@Override
	public AbstractScene<?>[] elements(Query query) {
		elem = new AnalysisScene(query);
		return new AbstractScene[] {elem};
	}
	@Override
	protected AbstractScene<?> prepare(String param) {
		return elem;
	}
	@Override
	protected String toParentUrl(String param) {
		return "액체생검/Analysis";
	}
}
