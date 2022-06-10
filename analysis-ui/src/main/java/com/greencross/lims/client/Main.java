package com.greencross.lims.client;

import com.greencross.lims.dto.Query;

public class Main extends AbstractEntryPoint {
	private AnalysisElement elem;
	@Override
	public AbstractScene<?>[] elements(Query query) {
		elem = new AnalysisElement(query);
		return new AbstractScene[] {elem};
	}
	@Override
	protected AbstractScene<?> prepare(String param) {
		elem.update();
		return elem;
	}
	@Override
	protected String toParentUrl(String param) {
		return "액체생검/Analysis";
	}
}
