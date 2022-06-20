package com.greencross.lims.client;

import com.greencross.lims.api.RouteApi;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.core.JsDate;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.*;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import static org.jboss.elemento.Elements.label;

public class AnalysisScene extends AbstractScenePageable<AnalysisScene> {
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
	private final AnalysisGridElement grid = AnalysisGridElement.build();
	private final Query query;
	public AnalysisScene(Query query) {
		super(query);
		this.query = query;
		initialize();
		this.sortable("ID", "Serial").sort("Serial", true);;
		((HTMLElement)iptDateFrom.element().parentElement).style.display = "flex";
		((HTMLElement)btnPdf.element().parentElement).style.display = "flex";
		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			btnPdf.enabled(selected);
			btnPublish.enabled(selected);
		});
		btnProgressOnly.onValueChange(evt->{
			update();
		});
		btnAnalysisComplete.onValueChange(evt->{
			update();
		});
		btnSearch.onClick(evt->{
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

	}
	private void save() {

	}
	private void pdf() {

	}
	private void publish() {

	}
	@Override
	public AnalysisScene that() {
		return this;
	}
}
