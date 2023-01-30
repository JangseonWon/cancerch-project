package com.gcgenome.lims.client;

import com.gcgenome.lims.api.AnalysisApi;
import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.RouteApi;
import com.gcgenome.lims.api.SampleApi;
import com.gcgenome.lims.client.dialogInner.PRTPUBDialogInnerElement;
import com.gcgenome.lims.client.dialogInner.QueueDialogInnerElement;
import com.gcgenome.lims.data.Analysis;
import com.gcgenome.lims.data.Report;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.ui.IconElement;
import elemental2.core.JsDate;
import elemental2.dom.*;
import elemental2.promise.Promise;
import net.sayaya.ui.*;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.*;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.*;

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
	private final ButtonElementToggle btnAnalysisComplete = ButtonElement.toggle().css("button").text("결과지 전체 조회").style("min-width: 200px;").value(false);
	private final ButtonElementToggle btnProgressOnly = ButtonElement.toggle().css("button").text("미배포 목록 조회").style("min-width: 200px;").value(true);
	private final CheckBoxElement chkOnlyPass = CheckBoxElement.checkBox(true).text("PASS ONLY").style("margin-right: 30px;");
	private final TextFieldElement<JsDate, TextFieldElement.TextFieldOutlined<JsDate>> iptDateFrom = TextFieldElement.dateBox().outlined().css("button").style("width: 155px;border-right: 0px !important; height:36px;").text("Date from").value(prevday()).required(true);
	private final TextFieldElement<JsDate, TextFieldElement.TextFieldOutlined<JsDate>> iptDateTo = TextFieldElement.dateBox().outlined().css("button").style("width: 155px; height:36px;").text("Date to").value(new JsDate()).required(true);
	private final ButtonElement btnSearch = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Light, "fa-search"));
	private final ButtonElement btnPdf = ButtonElement.outline().css("button").text("Print").before(IconElement.icon(IconElement.Type.Light, "fa-file-pdf"));
	private final ButtonElement btnPublish = ButtonElement.outline().css("button").text("Publish").before(IconElement.icon(IconElement.Type.Light, "fa-upload"));
	private final ButtonElement btnSave = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Regular, "fa-save"));
	private final ButtonElement btnQueue = ButtonElement.outline().css("button").text("대기열");
	private final AnalysisGridElement grid = AnalysisGridElement.build();
	private final ButtonElementText cancel 		= ButtonElement.outline().text("CANCEL");
	private final Dialog dialog 				= Dialog.confirmation("출력, 전송 대기열", null, cancel);
	private final QueueDialogInnerElement inner = QueueDialogInnerElement.instance();
	private final Query query;

	public AnalysisScene(Query query) {
		super(query);
		this.query = query;
		initialize();
		this.sortable("ID", "의뢰일", "Batch", "row").sort("ID", true);
		((HTMLElement)iptDateFrom.element().parentElement).style.display = "flex";
		((HTMLElement)btnPdf.element().parentElement).style.display = "flex";
		grid.onSelectionChange(evt->{
			boolean selected = evt.selection().length > 0;
			btnPdf.enabled(selected);
			btnPublish.enabled(selected);
		});
		btnSearch.onClick(evt->{update();});
		btnSave.onClick(evt->save());
		btnPdf.onClick(evt->print());
		btnPublish.onClick(evt->publish());
		btnPdf.enabled(false);
		btnPublish.enabled(false);
		btnAnalysisComplete.onClick(evt->{
			if(!btnAnalysisComplete.value())btnAnalysisComplete.text("결과지 전체 조회");
			else 							btnAnalysisComplete.text("결과지 미생성 조회");
		});
		btnProgressOnly.onClick(evt->{
			if(!btnProgressOnly.value()) 	btnProgressOnly.text("배포 전체 상태 조회");
			else 							btnProgressOnly.text("미배포 목록 조회");
		});
		dialogInit();
		btnQueue.onClick(evt->dialog.open());
		SampleApi.PublishEvent.listen()
				.onFinish(evt->{
					update();
				});
	}

	private void save(){
		if(Arrays.stream(grid.changed()).findAny().isEmpty()) {
			DomGlobal.alert("변경사항이 없습니다.");
		} else {
			ProgressApi.open();
			AnalysisApi.update(grid.changed()).then(response -> {
				update();
				DomGlobal.alert("저장이 완료되었습니다.");
				return null;
			}).finally_(ProgressApi::close);
		}
	}
	private void update(Query query){
		Query proxy = new Query().asc(this.isAsc());
		List<Query.Filter> filters = new LinkedList<>();
		if(query.filters!=null){
			Arrays.stream(query.filters).forEach(filter->filter.key(" "));
			Collections.addAll(filters, query.filters);
		}
		filters.add(new Query.Filter().key("to").value(String.valueOf(iptDateTo.value().getTime()+53940000)));
		filters.add(new Query.Filter().key("from").value(String.valueOf(iptDateFrom.value().getTime()-32400000)));
		proxy.sortBy(this.sort());

		if(this.btnProgressOnly.value()) filters.add(new Query.Filter().key("published").value("true"));
		if(this.btnAnalysisComplete.value()) filters.add(new Query.Filter().key("printed").value("true"));
		if(this.chkOnlyPass.value()) filters.add(new Query.Filter().key("pass").value("true"));
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
	private Promise<Report[]> map2(String json) {
		if(json!=null && !json.trim().isEmpty()) return Promise.resolve((Report[])JSON.parse(json));
		else return Promise.resolve((Report[])null);
	}
	@Override
	protected IsElement<?> grid() {
		return grid;
	}

	private static JsDate prevday() {
		JsDate today = new JsDate();
		JsDate yesterday = new JsDate(today);
		yesterday.setDate(yesterday.getDate()-30);
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
	protected BreadcrumbElement breadcrumb() {
		return breadcumb;
	}
	@Override
	protected IsElement<?>[][] controls() {
		return new IsElement<?>[][]{
			new IsElement[] { chkOnlyPass, btnAnalysisComplete, btnProgressOnly },
			new IsElement<?>[]{ iptDateFrom, label("~").style("line-height: 36px; margin-left: 2px; margin-right: 2px;"), iptDateTo, btnSearch, btnQueue},
			new IsElement[] {btnPdf, btnPublish, btnSave}
		};
	}
	@Override
	public void update() {
		update(query);
	}
	private void dialogInit() {
		HTMLElement surface 			= (HTMLElement) dialog.element().getElementsByClassName("mdc-dialog__surface").item(0);
		surface.style.minWidth 			= CSSProperties.MinWidthUnionType.of("1200px");
		surface.style.minHeight			= CSSProperties.MinHeightUnionType.of("800px");
		SampleApi.works().then(Response::text).then(this::map2)
				.then(reports->{
					inner.init(reports);
					return null;
				});

		SampleApi.PrintEvent.listen()
				.onCreate(inner::onCreate)
				.onUpdate(inner::onPrinting)
				.onFinish(evt->{
					inner.onFinish(evt);
					update();
				});
		cancel.onClick(evt->{
			dialog.close();
		});
		dialog.add(inner);
		body().add(dialog);
	}
	private void print() {
		Analysis[] selection = grid.selection();
		if(selection.length == 0) return;
		ButtonElementText ok 			= ButtonElement.outline().text("OK").enabled(false);
		ButtonElementText cancel 		= ButtonElement.outline().text("CANCEL");
		CheckBoxElement chkConfirm 		= CheckBoxElement.checkBox(false).text("위 내용을 확인했습니다.");
		Dialog dialog 					= Dialog.confirmation("선택한 " + selection.length + "개의 검사 결과지를 생성합니다.", ok, cancel);
		HTMLElement surface 			= (HTMLElement) dialog.element().getElementsByClassName("mdc-dialog__surface").item(0);
		surface.style.minWidth 			= CSSProperties.MinWidthUnionType.of("1200px");
		long countGeneral 				= Arrays.stream(selection).filter(d->d.result().equals("GENERAL")).count();
		long countConcern 				= Arrays.stream(selection).filter(d->d.result().equals("CONCERN")).count();
		long countRisk	  				= Arrays.stream(selection).filter(d->d.result().equals("RISK")).count();
		long countNeedLog				= Arrays.stream(selection).filter(d->d.report().fileName() != null).count();
		PRTPUBDialogInnerElement inner 	= PRTPUBDialogInnerElement.build(selection);

		chkConfirm.onValueChange(evt->{ok.enabled(evt.value());});
		ok.onClick(evt->{
			for(Analysis analysis: selection){
				if(analysis.report().fileName() != null) {
					String description = DomGlobal.prompt("변경 사유 입력이 필요한 ("+analysis.sample()+")검사 결과입니다.(3자 이상)");
					if(description.length() < 2) {
						DomGlobal.alert("2자 이하 입력으로 해당 결과의 결과지 생성이 취소됩니다.");
					}
					else {
						Promise<Boolean> response = SampleApi.print(
								String.valueOf(analysis.request().sample().id()),
								analysis.request().service().id(),
								analysis.batch(),
								String.valueOf(analysis.row()),
								"kokr",
								description
						);
						response.then(res -> {
							if (res.equals(true)) {
								inner.remove(analysis.request().sample().id(), analysis.request().service().id());
							} else {
								DomGlobal.console.log("retry");
								SampleApi.print(
										String.valueOf(analysis.request().sample().id()),
										analysis.request().service().id(),
										analysis.batch(),
										String.valueOf(analysis.row()),
										"kokr",
										description);
							}
							return null;
						});
					}
				}
			}
			dialog.close();
			dialog.element().remove();
			update();
		});
		cancel.onClick(evt->{
			dialog.close();
			dialog.element().remove();
		});
		dialog.add(div().add(label("일반관리 : "+countGeneral+"건").style("margin-right: 1em;"))
						.add(label("관심관리 : "+countConcern+"건").style("margin-right: 1em;"))
						.add(label("집중관리 : "+countRisk+"건").style("margin-right: 1em;"))
						.add(label("이력입력 필요 : "+countNeedLog+"건").style("color: #FF0000;")))
				.add(inner).add(chkConfirm);
		body().add(dialog);
		dialog.open();
	}
	private void publish() {
		Analysis[] selection = grid.selection();
		if(selection.length == 0) return;
		ButtonElementText ok 			= ButtonElement.outline().text("OK").enabled(false);
		ButtonElementText cancel 		= ButtonElement.outline().text("CANCEL");
		CheckBoxElement chkConfirm 		= CheckBoxElement.checkBox(false).text("위 내용을 확인했습니다.");
		Dialog dialog 					= Dialog.confirmation("선택한 " + selection.length + "개의 검사 결과지를 전송합니다.", ok, cancel);
		HTMLElement surface 			= (HTMLElement) dialog.element().getElementsByClassName("mdc-dialog__surface").item(0);
		surface.style.minWidth 			= CSSProperties.MinWidthUnionType.of("1200px");
		long countGeneral 				= Arrays.stream(selection).filter(d->d.result().equals("GENERAL")).count();
		long countConcern 				= Arrays.stream(selection).filter(d->d.result().equals("CONCERN")).count();
		long countRisk	  				= Arrays.stream(selection).filter(d->d.result().equals("RISK")).count();
		PRTPUBDialogInnerElement inner 	= PRTPUBDialogInnerElement.build(selection);

		chkConfirm.onValueChange(evt->{ok.enabled(evt.value());});
		ProgressApi.open(false);
		ok.onClick(evt-> {
			for (Analysis analysis : selection) {
				Promise<Boolean> response = SampleApi.publish(String.valueOf(analysis.request().sample().id()), analysis.request().service().id(), String.valueOf((long) JsDate.parse(analysis.report().createAt())));
				response.then(res->{
					if(res.equals(true)){
						inner.remove(analysis.request().sample().id(), analysis.request().service().id());
					}
					else {
						DomGlobal.console.log("retry");
						SampleApi.publish(String.valueOf(analysis.request().sample().id()), analysis.request().service().id(), String.valueOf((long) JsDate.parse(analysis.report().createAt())));
					}
					return null;
				});
			}
			dialog.close();
			dialog.element().remove();
			update();
		});
		cancel.onClick(evt ->{
			dialog.close();
			dialog.element().remove();
		});
		dialog.add(div().add(label("일반관리 : "+countGeneral+"건").style("margin-right: 1em;"))
						.add(label("관심관리 : "+countConcern+"건").style("margin-right: 1em;"))
						.add(label("집중관리 : "+countRisk+"건")))
				.add(inner).add(chkConfirm);
		body().add(dialog);
		dialog.open();
	}
	@Override
	public AnalysisScene that() {
		return this;
	}
}
