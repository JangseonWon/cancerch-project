package com.gcgenome.lims.client;

import com.gcgenome.lims.api.AnalysisApi;
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

import static java.lang.Math.round;
import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.EventType.bind;

public class AnalysisGridElement extends HTMLElementBuilder<HTMLDivElement, AnalysisGridElement> implements HasSelectionChangeHandlers<Analysis[]> {
	public static AnalysisGridElement build() { return new AnalysisGridElement(div()); }
	private static ColumnString column(String name) {
		return ColumnBuilder.string(name).width(100).name(name).readOnly(true).horizontal("center");
	}
	private static ColumnString columnAndColor(String name, String type) {
		return ColumnBuilder.string(name).width(100).name(name).readOnly(true).horizontal("right").colorBackground((td, row, prop, value) ->{
			if("A".equals(type)) return "#BF5C26";
			else return "#8C7162";
		}).color("#FFFFFF");
	}
	private static ColumnString columnResult(String name){
		return ColumnBuilder.string(name).width(100).name(name).readOnly(true).horizontal("center").colorBackground((td, row, prop, value) ->{
			if("일반관리".equals(value)) return "#8DC556";
			else if("관심관리".equals(value)) return "#EFA718";
			else if("집중관리".equals(value)) return "#D9341D";
			else return "#FFFFFF";
		}).color("#FFFFFF");
	}
	private static ColumnString columnThreshold(String name, Code code, String type){
		return ColumnBuilder.string(name).width(100).name(name).readOnly(true).horizontal("center").colorBackground((td, row, prop, value) ->{
			if(code.equals(Code.FREEMIX)) {
				if(Double.valueOf(value) >= 1.0) return "#FF0000";
				else return type.equals("A") ? "#BF5C26" : "#8C7162";
			}
			else if(code.equals(Code.FINAL_READS)) {
				if(Double.valueOf(value) <= 40.0) return "#FF0000";
				else return type.equals("A") ? "#BF5C26" : "#8C7162";
			}
			else return type.equals("A") ? "#BF5C26" : "#8C7162";
		}).color("#FFFFFF");
	}
	private static ColumnString columnPassOrFail(String name){
		return ColumnBuilder.string(name).width(100).name(name).readOnly(true).horizontal("center").colorBackground((td, row, prop, value) ->{
			if("PASS".equals(value)) return "#46BF26";
			else return "#F25349";
		}).color("#FFFFFF");
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
							.onClick(data->{DomGlobal.window.open("../sample.html#"+data.get("ID"));}).build(),
					column("검사명").build(),
					column("수진자명").build(),
					column("성별").build(),
					column("MRN").build(),
					column("의뢰일").build(),
					column("TAT").build(),
					column("의뢰기관").build(),
					column("Labs 코드").build(),
					column("분석일").build(),
					column("Batch").build(),
					column("Row").build(),
					columnResult("결과 분석").build(),
					columnPassOrFail("QC 분석").build(),
					columnPassOrFail("성별 분석").build(),
					ColumnBuilder.link("결과지", data->"#"+data.idx()).name("결과지").readOnly(true).horizontal("center")
							.onClick(this::preview).build(),
					column("결과발송일").build(),
					column("발송자").build(),
					column("top 5 prediction").build(),
					column("top 5 FEMS prob").horizontal("right").build(),
					column("top 6 prediction").build(),
					column("top 6 FEMS prob").horizontal("right").build(),
					column("iscore").horizontal("right").build(),
					column("cad ensemble prob").horizontal("right").build(),
					columnThreshold("freemix A", Code.FREEMIX, "A").build(),
					columnAndColor("raw read(Million) A", "A").build(),
					columnThreshold("filtered reads A", Code.FINAL_READS, "A").build(),
					columnAndColor("dup rate A", "A").build(),
					columnAndColor("gc A", "A").build(),
					columnAndColor("mean is A", "A").build(),
					columnAndColor("median is A", "A").build(),
					columnPassOrFail("qc A").build(),
					columnAndColor("chrX Count A", "A").build(),
					columnAndColor("chrX Proportion A", "A").build(),
					columnAndColor("chrY Count A", "A").build(),
					columnAndColor("chrY Proportion A", "A").build(),
					columnPassOrFail("Sex Prediction A").build(),
					columnThreshold("freemix B", Code.FREEMIX,  "B").build(),
					columnAndColor("raw read(Million) B", "B").build(),
					columnThreshold("filtered reads B", Code.FINAL_READS, "B").build(),
					columnAndColor("dup rate B", "B").build(),
					columnAndColor("gc B", "B").build(),
					columnAndColor("mean is B", "B").build(),
					columnAndColor("median is B", "B").build(),
					columnPassOrFail("qc B").build(),
					columnAndColor("chrX Count B", "B").build(),
					columnAndColor("chrX Proportion B", "B").build(),
					columnAndColor("chrY Count B", "B").build(),
					columnAndColor("chrY Proportion B", "B").build(),
					columnPassOrFail("Sex Prediction B").build()
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
			elemSheet.clear();
			elemSheet.values(data);
			return that();
		} catch(Exception e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	private Data map(Analysis value) {
		if(value == null) return null;
		String id 			= (value.request()!=null && value.request().sample()!=null) ? DataTransformUtil.formatSampleId(value.request().sample().id()):null;
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
		String customerCd   = value.request().sample().remark() == null ? "" : value.request().sample().remark();
		String analysisDt	= DataTransformUtil.formatDate((long) JsDate.parse(value.createAt()));
		String batch		= value.batch();
		String row			= String.valueOf(value.row());
		String reportCreateDt = String.valueOf((long) JsDate.parse(value.report().createAt()));
		String reportNm		= value.report().fileName();
		String publishNm	= value.report().publisher().name();
		String publishDt 	= value.report().publishAt().equals("null") ? "" : DataTransformUtil.formatDate((long) JsDate.parse(value.report().publishAt()));

		String freemix		= convertFormat(value.freemix());
		String rawReadMil	= convertFormat(value.rawReadsMillions());
		String duprate      = convertFormat(value.dupRate());
		String totalRead	= convertFormat(value.totalReads());
		String gc			= convertFormat(value.gc());
		String mean			= convertFormat(value.mean());
		String median		= convertFormat(value.median());
		String qc			= value.qc().equals("P") ? "PASS" : "FAIL";
		String chrXCnt		= convertFormat(value.chrXCnt());
		String chrYCnt		= convertFormat(value.chrYCnt());
		String chrXProp		= convertFormat(value.chrXProp());
		String chrYProp		= convertFormat(value.chrYProp());
		String sexPred		= sex.equals(value.predSexTmp()) ? "PASS" : "FAIL";

		String freemixT		= convertFormat(value.freemixTmp());
		String rawReadMilT	= convertFormat(value.rawReadsMillionsTmp());
		String duprateT     = convertFormat(value.dupRateTmp());
		String totalReadT	= convertFormat(value.totalReadsTmp());
		String gcT			= convertFormat(value.gcTmp());
		String meanT		= convertFormat(value.meanTmp());
		String medianT		= convertFormat(value.medianTmp());
		String qcT			= value.qcTmp().equals("P") ? "PASS" : "FAIL";
		String chrXCntT		= convertFormat(value.chrXCntTmp());
		String chrYCntT		= convertFormat(value.chrYCntTmp());
		String chrXPropT	= convertFormat(value.chrXPropTmp());
		String chrYPropT	= convertFormat(value.chrYPropTmp());
		String sexPredT		= sex.equals(value.predSexTmp()) ? "PASS" : "FAIL";
		String sexCheck		= sexPred.equals("PASS") && sexPredT.equals("PASS") ? "PASS" : "FAIL";
		String qcCheck		= qc.equals("PASS") && qcT.equals("PASS") ? "PASS" : "FAIL";
		String cadEnsemble  = convertFormat(value.cadEnsembleProb());
		String top5Pred		= convertCancerName(value.too5Pred());
		String top5FEMS		= convertFormat(value.too5FemsProb());
		String top6Pred		= convertCancerName(value.too6Pred());
		String top6FEMS		= convertFormat(value.too6FemsProb());
		String iscore		= convertFormat(value.iscore());
		String result		= convertResultName(value.result());
		Data datum = new Data(idx)
				.put("ID",       				id)
				.put("검사코드", 				service)
				.put("검사명",   				serviceNm)
				.put("수진자명", 				patientNm)
				.put("성별", 	 				sex)
				.put("MRN",  	 				mrn == null ? "" : mrn)
				.put("의뢰일",   				requestDt)
				.put("TAT",      				tatDt)
				.put("의뢰기관", 				customer)
				.put("Labs 코드",               customerCd)
				.put("분석일",   				analysisDt)
				.put("Batch",    				batch)
				.put("Row",      				row)
				.put("reportCreated", 			reportCreateDt)
				.put("결과 분석", 				result)
				.put("QC 분석",					qcCheck)
				.put("성별 분석",               sexCheck)
				.put("결과지", 					reportNm  == null ? "" : reportNm)
				.put("발송자", 					publishNm == null ? "" : publishNm)
				.put("결과발송일", 				publishDt)
				.put("top 5 prediction",		top5Pred)
				.put("top 5 FEMS prob", 		top5FEMS)
				.put("top 6 prediction",		top6Pred)
				.put("top 6 FEMS prob", 		top6FEMS)
				.put("iscore",					iscore)
				.put("cad ensemble prob", 		cadEnsemble)
				.put("Sex Check A", 			sexPred)
				.put("freemix A",				freemix)
				.put("raw read(Million) A", 	rawReadMil)
				.put("filtered reads A",		totalRead)
				.put("dup rate A", 				duprate)
				.put("gc A",  					gc)
				.put("mean is A",				mean)
				.put("median is A",     		median)
				.put("qc A",            		qc)
				.put("chrX Count A",            chrXCnt)
				.put("chrX Proportion A",       chrXProp)
				.put("chrY Count A",            chrYCnt)
				.put("chrY Proportion A",       chrYProp)
				.put("Sex Prediction A",  		sexPred)
				.put("freemix B",				freemixT)
				.put("raw read(Million) B", 	rawReadMilT)
				.put("filtered reads B",    	totalReadT)
				.put("dup rate B", 				duprateT)
				.put("gc B",  					gcT)
				.put("mean is B",				meanT)
				.put("median is B",     		medianT)
				.put("qc B",            		qcT)
				.put("chrX Count B",            chrXCntT)
				.put("chrX Proportion B",       chrXPropT)
				.put("chrY Count B",            chrYCntT)
				.put("chrY Proportion B",       chrYPropT)
				.put("Sex Prediction B",  		sexPredT);
		return datum;
	}
	@Override
	public AnalysisGridElement that() {
		return this;
	}

	@Override
	public Analysis[] selection() {
		return Arrays.stream(wrapper.selection()).map(d->d.get("ID").replace("-", "") + "/" + d.get("검사코드")).map(values::get).toArray(Analysis[]::new);
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
			case "ESO" : 	return "식도암";
			case "HCC" : 	return "간암";
			case "OV"  : 	return "난소암";
			case "colon"  : return "대장암";
			case "LuC"  : 	return "폐암";
			case "Panc"  : 	return "췌장암";
			default  : 		return "WTF";
		}
	}
	private String convertResultName(String result){
		switch (result) {
			case "RISK"  : 		return "집중관리";
			case "GENERAL" : 	return "일반관리";
			case "CONCERN"  : 	return "관심관리";
			default  : 			return "WTF";
		}
	}
	private String convertFormat(Double data){
		return data == 0 ? "" : String.valueOf(round(data*100f)/100f);
	}
}
