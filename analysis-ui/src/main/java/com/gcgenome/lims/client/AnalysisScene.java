package com.gcgenome.lims.client;

import com.gcgenome.lims.api.AnalysisApi;
import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.RouteApi;
import com.gcgenome.lims.data.Analysis;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.core.client.Scheduler;
import elemental2.core.JsDate;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import net.sayaya.ui.BreadcrumbElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.ButtonElementToggle;
import net.sayaya.ui.TextFieldElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.label;

public class AnalysisScene extends AbstractScenePageable<AnalysisScene> {
	private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Analysis");
	private final BreadcrumbElement breadcumb = BreadcrumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
		RouteApi.location("", true, false);
	}).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
	.add("액체생검", evt->{
		evt.preventDefault();
		evt.stopPropagation();
	}).add("Analysis", evt->{
		evt.preventDefault();
		evt.stopPropagation();
		Router.location("", true);
	});
	private final ButtonElementToggle btnAnalysisComplete = ButtonElement.toggle().css("button").text("분석 완료 조회").style("min-width: 150px;").value(true);
	private final ButtonElementToggle btnProgressOnly = ButtonElement.toggle().css("button").text("미완료 항목 조회").style("min-width: 150px;").value(true);
	private final TextFieldElement<JsDate, TextFieldElement.TextFieldOutlined<JsDate>> iptDateFrom = TextFieldElement.dateBox().outlined().css("button").style("width: 125px;border-right: 0px !important; height:36px;").text("Date from").value(prevday()).required(true);
	private final TextFieldElement<JsDate, TextFieldElement.TextFieldOutlined<JsDate>> iptDateTo = TextFieldElement.dateBox().outlined().css("button").style("width: 125px; height:36px;").text("Date to").value(today()).required(true);
	private final ButtonElement btnSearch = ButtonElement.outline().css("button").text("Search").before(IconElement.icon(IconElement.Type.Light, "fa-search"));
	private final ButtonElement btnPdf = ButtonElement.outline().css("button").text("Print").before(IconElement.icon(IconElement.Type.Light, "fa-file-pdf"));
	private final ButtonElement btnPublish = ButtonElement.outline().css("button").text("Publish").before(IconElement.icon(IconElement.Type.Light, "fa-upload"));
	private final AnalysisGridElement grid = AnalysisGridElement.build();
	private final Query query;
	boolean initiailized = false;

	public AnalysisScene(Query query) {
		super(query);
		this.query = query;
		initialize();
		this.sortable("ID", "Serial").sort("ID", true);;
		((HTMLElement)iptDateFrom.element().parentElement).style.display = "flex";
		((HTMLElement)btnPdf.element().parentElement).style.display = "flex";
		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			btnPdf.enabled(selected);
			btnPublish.enabled(selected);
		});
		btnSearch.onClick(evt->{
			update();
		});
		btnPdf.onClick(evt->print());
		btnPublish.onClick(evt->publish());
		btnPdf.enabled(false);
		btnPublish.enabled(false);
		btnAnalysisComplete.onClick(evt->{
			if(btnAnalysisComplete.value()) btnAnalysisComplete.text("분석 완료 조회");
			else 							btnAnalysisComplete.text("전체 항목 조회");
		});
		btnProgressOnly.onClick(evt->{
			if(btnProgressOnly.value()) 	btnProgressOnly.text("미완료 항목 조회");
			else 							btnProgressOnly.text("전체 항목 조회");
		});
		Scheduler.get().scheduleFixedDelay(()->{
			initiailized = true;
			update();
			return false;
		}, 1000);
	}

	private void update(Query query){
		if(!initiailized) return;
		Query proxy = new Query().asc(this.isAsc());
		List<Query.Filter> filters = new LinkedList<>();
		if(query.filters!=null){
			Arrays.stream(query.filters).forEach(filter->filter.key(" "));
			Collections.addAll(filters, query.filters);
		}
		filters.add(new Query.Filter().key("to").value(String.valueOf(iptDateTo.value().getTime())));
		filters.add(new Query.Filter().key("from").value(String.valueOf(iptDateFrom.value().getTime())));
		proxy.sortBy(this.sort());

		if(!this.btnProgressOnly.value()) filters.add(new Query.Filter().key("published").value("true"));
		proxy.limit(show()).page((int) page());
		proxy.filters(filters.stream().toArray(Query.Filter[]::new));
		ProgressApi.open(false);
		AnalysisApi.search(proxy)
				.then(this::updateTotal)
				.then(Response::text)
				.then(this::map)
				.then(a->{
					grid.update(a);
					return null;
				}).finally_(ProgressApi::close);
	}
	private Promise<Response> updateTotal(Response response) {
		total(Long.parseLong(response.headers.get("X-TOTAL-COUNT")));
		return Promise.resolve(response);
	}

	private Promise<Analysis[]> map(String json) {
		if(json!=null && !json.trim().isEmpty()) return Promise.resolve((Analysis[])JSON.parse(json));
		else return Promise.resolve((Analysis[])null);
	}
	@Override
	protected IsElement<?> grid() {
		return grid;
	}

	private static JsDate prevday() {
		JsDate today = new JsDate();
		JsDate yesterday = new JsDate(today);
		yesterday.setDate(yesterday.getDate()-14);
		yesterday.setHours(0, 0, 0, 0);
		return yesterday;
	}
	private static JsDate today() {
		JsDate today = new JsDate();
		today.setHours(23,59,59);
		return today;
	}
	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Light, "fa-diagnoses");
	}
	@Override
	protected HtmlContentBuilder<HTMLLabelElement> title() {
		return title;
	}
	@Override
	protected BreadcrumbElement breadcrumb() {
		return breadcumb;
	}
	@Override
	protected IsElement<?>[][] controls() {
		return new IsElement<?>[][]{
			new IsElement[] { btnProgressOnly, btnAnalysisComplete },
			new IsElement<?>[]{ iptDateFrom, label("~").style("line-height: 36px; margin-left: 2px; margin-right: 2px;"), iptDateTo, btnSearch},
			new IsElement[] {btnPdf, btnPublish}
		};
	}
	@Override
	public void update() {
		update(query);
	}
	private void print() {
		Analysis[] selection = grid.selection();
		if(selection.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selection.length + "개의 검사 결과지를 생성합니다.")) return;
		ProgressApi.open(false);

//		AnalysisApi.print(selection).then(results->{
//			Arrays.stream(results).anyMatch(result->!result.ok)
//			DomGlobal.alert("완료되었습니다.");
//			update();
//			return null;
//		}).finally_(ProgressApi::close);

		for (Analysis analysis: selection) {
			AnalysisApi.print(String.valueOf(analysis.request().sample().id()), analysis.request().service().id(), "kokr")
					.then(result->{
						if(result.ok) {
							update();
							return Promise.resolve(true);
						}
						else return Promise.resolve(false);
					}).finally_(ProgressApi::close);
		}
	}
	private void publish() {
		Analysis[] selection = grid.selection();
		if(selection.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selection.length + "개의 검사 결과지를 전송합니다.")) return;
		ProgressApi.open(false);
		for (Analysis analysis: selection) {
			AnalysisApi.publish(String.valueOf(analysis.request().sample().id()), analysis.request().service().id(), String.valueOf((long) JsDate.parse(analysis.report().createAt())))
					.then(result-> {
						DomGlobal.alert("완료되었습니다.");
						update();
						return null;
					}).finally_(ProgressApi::close);
		}
	}
	@Override
	public AnalysisScene that() {
		return this;
	}
}
