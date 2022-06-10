package com.greencross.lims.client;

import com.greencross.lims.api.AnalysisApi;
import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.data.Analysis;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.core.JsDate;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.*;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.jboss.elemento.Elements.label;

public class AnalysisElement extends AbstractScenePageable<AnalysisElement> {
	private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Analysis");
	private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
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
	private final ButtonElementToggle btnAnalysisComplete = ButtonElement.toggle().css("button").text("Analyzed").value(true);
	private final ButtonElementToggle btnProgressOnly = ButtonElement.toggle().css("button").text("Not complete only").value(true);
	private final TextFieldElement<JsDate> iptDateFrom = TextFieldElement.dateBox().outlined().css("button").text("Date from").value(prevday()).required(true);
	private final TextFieldElement<JsDate> iptDateTo = TextFieldElement.dateBox().outlined().css("button").text("Date to").value(new JsDate()).required(true);
	private final ButtonElement btnSearch = ButtonElement.outline().css("button").text("Search").before(IconElement.icon(IconElement.Type.Light, "fa-search"));
	private final ButtonElement btnSave = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
	private final ButtonElement btnPdf = ButtonElement.outline().css("button").text("Print").before(IconElement.icon(IconElement.Type.Light, "fa-file-pdf"));
	private final ButtonElement btnPublish = ButtonElement.outline().css("button").text("Publish").before(IconElement.icon(IconElement.Type.Light, "fa-upload"));
	private final AnalysisGridElement grid = AnalysisGridElement.builder().build().style("position: absolute; top: 90px; bottom: 50px; left: 20px; right: 20px; overflow: hidden;" +
																				 "border-top: 1px solid #AAA; border-bottom: 1px solid #AAA; transition: all 200ms;");
	private final Query query;
	private final PageElement page;
	public AnalysisElement(Query query) {
		super(query);
		this.query = query;
		initialize();
		page = (PageElement) super.contents()[1];
		page.sortable("ID", "Serial");
		page.sort("Serial", true);
		((HTMLElement)iptDateFrom.element().parentElement).style.display = "flex";
		((HTMLElement)btnPdf.element().parentElement).style.display = "flex";
		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			btnPdf.enabled(selected);
			btnPublish.enabled(selected);
		});
		btnProgressOnly.onValueChange(evt->{
			this.page.idx(0L);
			update();
		});
		btnAnalysisComplete.onValueChange(evt->{
			this.page.idx(0L);
			update();
		});
		btnSearch.onClick(evt->{
			this.page.idx(0L);
			update();
		});
		btnSave.onClick(evt->save());
		btnPdf.onClick(evt->pdf());
		btnPublish.onClick(evt->publish());
		btnPdf.enabled(false);
		btnPublish.enabled(false);
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
	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Light, "fa-diagnoses");
	}
	@Override
	protected HtmlContentBuilder<HTMLLabelElement> title() {
		return title;
	}
	@Override
	protected BreadcumbElement breadcumb() {
		return breadcumb;
	}
	@Override
	protected IsElement<?>[][] controls() {
		return new IsElement<?>[][]{
			new IsElement[] { btnProgressOnly, btnAnalysisComplete },
			new IsElement<?>[]{ iptDateFrom, label("~").style("line-height: 36px; margin-left: 2px; margin-right: 2px;"), iptDateTo, btnSearch},
			new IsElement[] {btnSave, btnPdf, btnPublish}
		};
	}
	@Override
	public void update() {
		java.util.List<Query.Filter> filters = new LinkedList<>();
		if(query.filters()!=null) filters.addAll(Arrays.asList(query.filters()));
		if(btnProgressOnly.value()) filters.add(new Query.Filter().key("not_complete").value("true"));
		if(btnAnalysisComplete.value()) filters.add(new Query.Filter().key("analyzed").value("true"));
		filters.add(new Query.Filter().key("from").value(String.valueOf(iptDateFrom.value().getTime())));
		filters.add(new Query.Filter().key("to").value(String.valueOf(iptDateTo.value().getTime())));
		Query clone = new Query();
		clone.filters(filters.stream().toArray(Query.Filter[]::new));
		String sortKey = "sample";
		boolean asc = false;
		if(page.sortBy()!=null && !page.sortBy().isEmpty()) {
			switch(page.sortBy()) {
				case "ID": sortKey = "sample"; break;
				case "Serial": sortKey = "serial"; break;
			}
			asc = page.isAsc();
		}
		AnalysisApi.analysis(clone.limit(show()).page((int) page()).sortBy(sortKey).asc(asc)).last(page->{
		   	total(page.totalElement());
		   	grid.update(page.content());
		});
	}
	private void save() {
		AnalysisChanges[] changes = grid.changed();
		if(changes.length <= 0) return;
		if(!DomGlobal.confirm("저장합니다.")) return;
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (AnalysisChanges change : changes) {
			AnalysisApi.save(change.sample(), change.service(), change.info())
					   .last(callback -> {
						   int completed = complete.incrementAndGet();
						   if (completed >= changes.length) {
							   ProgressApi.close();
						   } else ProgressApi.progress(completed / (double) changes.length);
						   grid.update(callback);
					   });
		}
	}
	private void pdf() {
		Analysis[] selection = grid.selection();
		if(selection.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selection.length + "개의 검사 결과지를 생성합니다.")) return;
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (Analysis analysis: selection) {
			AnalysisApi.pdf(analysis.request().sample().id(), analysis.request().service().id())
					   .last(callback->{
						   int completed = complete.incrementAndGet();
						   if (completed >= selection.length) {
							   ProgressApi.close();
							   DomGlobal.alert("완료되었습니다.");
						   } else ProgressApi.progress(completed / (double) selection.length);
						   grid.update(callback);
					   });
		}
	}
	private void publish() {
		Analysis[] selection = grid.selection();
		if(selection.length <= 0) return;
		if(!DomGlobal.confirm("선택한 " + selection.length + "개의 검사 결과를 A-LIS로 전송합니다.")) return;
		ProgressApi.open(true);
		AtomicInteger complete = new AtomicInteger(0);
		for (Analysis analysis: selection) {
			AnalysisApi.publish(analysis.request().sample().id(), analysis.request().service().id())
					   .last(callback->{
						   int completed = complete.incrementAndGet();
						   if (completed >= selection.length) {
							   ProgressApi.close();
							   DomGlobal.alert("완료되었습니다.");
						   } else ProgressApi.progress(completed / (double) selection.length);
						   grid.update(callback);
					   });
		}
	}
	@Override
	public AnalysisElement that() {
		return this;
	}
}
