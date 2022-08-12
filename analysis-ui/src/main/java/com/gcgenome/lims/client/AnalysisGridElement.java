package com.gcgenome.lims.client;

import com.gcgenome.lims.api.AnalysisApi;
import com.gcgenome.lims.api.WindowApi;
import com.gcgenome.lims.data.Analysis;
import com.google.gwt.core.client.JsDate;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetElementSelectableMulti;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnString;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.EventType.bind;

public class AnalysisGridElement extends HTMLElementBuilder<HTMLDivElement, AnalysisGridElement> implements HasSelectionChangeHandlers<Analysis[]> {
	public static AnalysisGridElement build() { return new AnalysisGridElement(div()); }
	private static ColumnString column(String name) {
		return ColumnBuilder.string(name).width(100).name(name).readOnly(true).horizontal("center");
	}
	private static ColumnString columnAndColor(String name, String type) {
		return ColumnBuilder.string(name).width(100).name(name).readOnly(true).horizontal("right").colorBackground((td, row, prop, value) ->{
			if("A".equals(type)) return "#df7368";
			else return "#eacbca";
		});
	}
	private final SheetElement.SheetConfiguration config = SheetElement.builder()
			.rowHeaders(true)
			.autoColSize(true).renderAllRows(true).viewportColumnRenderingOffset(500.0)
			.autoRowSize(false)
			.manualColumnMove(true)
			.manualColumnResize(true)
			.stretchH("all")
			.columns(
					ColumnBuilder.link("ID", data->"#"+data.idx()).name("ID").readOnly(true).horizontal("center")
							.onClick(data->{
								DomGlobal.window.open("Https://"+Router.location().split("/")[2]+"/sample.html#"+data.get("ID"));
							}).build(),
					column("검사명").build(),
					column("수진자명").build(),
					column("성별").build(),
					column("MRN").build(),
					column("의뢰일").build(),
					column("TAT").build(),
					column("의뢰기관").build(),
					column("분석일").build(),
					column("Batch").build(),
					column("Row").build(),
					ColumnBuilder.link("결과지", data->"#"+data.idx()).name("결과지").readOnly(true).horizontal("center")
							.onClick(this::preview).build(),
					column("결과발송일").build(),
					column("발송자").build(),
					column("관리분류").build(),
					column("top 5 prediction").build(),
					column("top 5 FEMS prob").horizontal("right").build(),
					column("top 6 prediction").build(),
					column("top 6 FEMS prob").horizontal("right").build(),
					column("iscore").horizontal("right").build(),
					column("cad ensemble prob").horizontal("right").build(),
					columnAndColor("freemix A", "A").build(),
					columnAndColor("raw read(Million) A", "A").build(),
					columnAndColor("filtered reads A", "A").build(),
					columnAndColor("dup rate A", "A").build(),
					columnAndColor("gc A", "A").build(),
					columnAndColor("mean is A", "A").build(),
					columnAndColor("median is A", "A").build(),
					columnAndColor("qc A", "A").horizontal("center").build(),
					columnAndColor("freemix B", "B").build(),
					columnAndColor("raw read(Million) B", "B").build(),
					columnAndColor("filtered reads B", "B").build(),
					columnAndColor("dup rate B", "B").build(),
					columnAndColor("gc B", "B").build(),
					columnAndColor("mean is B", "B").build(),
					columnAndColor("median is B", "B").build(),
					columnAndColor("qc B", "B").horizontal("center").build()
			).data(new Data[10]);

	private void preview(Data data) {
		String sample = data.get("ID");
		String service = data.get("검사코드");
		String report = data.get("reportCreated");
		AnalysisApi.download(sample, service, report)
				.then(blob->{
					String url = URL.createObjectURL(blob);
					DomGlobal.window.open(url);
					URL.revokeObjectURL(url);
					return null;
				});
	}

	private final SheetElement elemSheet = config.build();
	private SheetElementSelectableMulti wrapper;
	private Map<String, Analysis> values;

	private AnalysisGridElement(HtmlContentBuilder<HTMLDivElement> e) {
		super(e.style("width: 100%;"));
		SheetElementSelectableMulti.header(elemSheet);
		wrapper = SheetElementSelectableMulti.wrap(elemSheet);
		HtmlContentBuilder<HTMLDivElement> table = div().style("overflow: hidden; width: 100%; height: 85vh; border-bottom: 1px solid #AAA;")
				.add(div().style("border-top: 1px solid #AAA;").add(elemSheet));
		e.add(table);
	}
	public AnalysisGridElement update(Analysis[] values) {
		this.values = Arrays.stream(values).collect(Collectors.toMap(a->a.sample() + "/" + a.request().service().id(), w->w));
		return update(Arrays.stream(values).map(this::map).toArray(Data[]::new));
	}
	private AnalysisGridElement update(Data[] data){
		try {
			elemSheet.values(data).refresh();
			return that();
		} catch(Exception e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	private Data map(Analysis value) {
		if(value == null) return null;
		String id 			= String.valueOf(value.request().sample().id());
		String service 		= value.request().service().id();
		String idx 			= id+"$"+service;
		String serviceNm 	= value.request().service().name();

		//Nullable
		String sex 			= value.request().sample().patient().sex();
		String mrn 			= value.request().sample().patient().mrn();
		String patientNm 	= value.request().sample().patient().name();
		String requestDt	= DataTransformUtil.formatDate((long) JsDate.parse(value.request().dateRequest()));
		String tatDt		= DataTransformUtil.formatDate((long) JsDate.parse(value.request().dateDue()));
		String customer		= value.request().sample().patient().customer();
		String analysisDt	= DataTransformUtil.formatDate((long) JsDate.parse(value.createAt()));
		String batch		= value.batch();
		String row			= String.valueOf(value.row());
		String reportCreateDt = String.valueOf((long) JsDate.parse(value.report().createAt()));
		String reportNm		= value.report().fileName();
		String publishNm	= value.report().publisher().name();
		String publishDt 	= value.report().publishAt();
		String freemix		= String.valueOf(value.freemix());
		String rawReadMil	= String.valueOf(value.rawReadsMillions());
		String duprate      = String.valueOf(value.dupRate());
		String totalRead	= String.valueOf(value.totalReads());
		String gc			= String.valueOf(value.gc());
		String mean			= String.valueOf(value.mean());
		String median		= String.valueOf(value.median());
		String qc			= value.qc();
		String freemixT		= String.valueOf(value.freemixTmp());
		String rawReadMilT	= String.valueOf(value.rawReadsMillionsTmp());
		String duprateT     = String.valueOf(value.dupRateTmp());
		String totalReadT	= String.valueOf(value.totalReadsTmp());
		String gcT			= String.valueOf(value.gcTmp());
		String meanT		= String.valueOf(value.meanTmp());
		String medianT		= String.valueOf(value.medianTmp());
		String qcT			= value.qcTmp();

		String cadEnsemble = String.valueOf(value.cadEnsembleProb());
		String top5Pred		= convertCancerName(value.too5Pred());
		String top5FEMS		= String.valueOf(value.too5FemsProb());
		String top6Pred		= convertCancerName(value.too6Pred());
		String top6FEMS		= String.valueOf(value.too6FemsProb());
		String iscore		= String.valueOf(value.iscore());
		String result		= convertResultName(value.result());
		Data datum = new Data(idx)
				.put("ID",       		id)
				.put("검사코드", 		service)
				.put("검사명",   		serviceNm)
				.put("수진자명", 		patientNm)
				.put("성별", 	 		sex)
				.put("MRN",  	 		mrn == null ? "" : mrn)
				.put("의뢰일",   		requestDt)
				.put("TAT",      		tatDt)
				.put("의뢰기관", 		customer)
				.put("분석일",   		analysisDt)
				.put("Batch",    		batch)
				.put("Row",      		row)
				.put("reportCreated", 	reportCreateDt)
				.put("결과지", 			reportNm  == null ? "" : reportNm)
				.put("발송자", 			publishNm == null ? "" : publishNm)
				.put("결과발송일", 		publishDt.equals("null") ? "" : publishDt)
				.put("관리분류", 		result)
				.put("top 5 prediction",top5Pred)
				.put("top 5 FEMS prob", top5FEMS)
				.put("top 6 prediction",top6Pred)
				.put("top 6 FEMS prob", top6FEMS)
				.put("iscore",			iscore)
				.put("cad ensemble prob", cadEnsemble)
				.put("freemix A",		freemix)
				.put("raw read(Million) A", rawReadMil)
				.put("filtered reads A",totalRead)
				.put("dup rate A", 		duprate)
				.put("gc A",  			gc)
				.put("mean is A",		mean)
				.put("median is A",     median)
				.put("qc A",            qc)
				.put("freemix B",		freemixT)
				.put("raw read(Million) B", rawReadMilT)
				.put("filtered reads B",    totalReadT)
				.put("dup rate B", 		duprateT)
				.put("gc B",  			gcT)
				.put("mean is B",			meanT)
				.put("median is B",        medianT)
				.put("qc B",            qcT);
		return datum;
	}
	@Override
	public AnalysisGridElement that() {
		return this;
	}

	@Override
	public Analysis[] selection() {
		return  Arrays.stream(wrapper.selection()).map(d->d.get("ID") + "/" + d.get("검사코드")).map(values::get).toArray(Analysis[]::new);
	}
	@Override
	public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Analysis[]> selectionChangeEventListener) {
		return onSelectionChange(elemSheet.element(), selectionChangeEventListener);
	}
	@Override
	public HandlerRegistration onSelectionChange(EventTarget dom, SelectionChangeEventListener<Analysis[]> listener) {
		EventListener wrapper = evt->listener.handle(SelectionChangeEvent.event(evt, selection()));
		return bind(dom, "selection-change", wrapper);
	}
	private String convertCancerName(String pred){
		switch (pred) {
			case "ESO" : return "식도암";
			case "HCC" : return "간암";
			case "OV"  : return "난소암";
			case "colon"  : return "대장암";
			case "LuC"  : return "폐암";
			case "PanC"  : return "췌장암";
			default  : return "WTF";
		}
	}
	private String convertResultName(String result){
		switch (result) {
			case "RISK"  : return "집중관리";
			case "GENERAL" : return "일반관리";
			case "CONCERN"  : return "관심관리";
			default  : return "WTF";
		}
	}
}
