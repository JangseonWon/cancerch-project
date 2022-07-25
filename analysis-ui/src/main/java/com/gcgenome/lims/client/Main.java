package com.gcgenome.lims.client;

import com.greencross.lims.client.AbstractEntryPoint;
import com.greencross.lims.client.AbstractScene;
import com.greencross.lims.dto.Query;

public class Main extends AbstractEntryPoint {
	private AnalysisScene elem;
	@Override
	public AbstractScene<?>[] elements(Query query) {
		elem = new AnalysisScene(query);
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
