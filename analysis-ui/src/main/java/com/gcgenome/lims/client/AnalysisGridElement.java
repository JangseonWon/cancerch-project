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

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.EventType.bind;

public class AnalysisGridElement extends HTMLElementBuilder<HTMLDivElement, AnalysisGridElement> implements HasSelectionChangeHandlers<Analysis[]> {
	public static AnalysisGridElement build() { return new AnalysisGridElement(div()); }
	private static ColumnString column(String name) {
		return ColumnBuilder.string(name).width(100).name(name).readOnly(true).horizontal("center");
	}
	private final SheetElement.SheetConfiguration config = SheetElement.builder()
			.rowHeaders(true)
			.autoColSize(true)
			.autoRowSize(false)
			.manualColumnMove(true)
			.manualColumnResize(true)
			.stretchH("all")
			.columns(
					ColumnBuilder.link("ID", data->"#"+data.idx()).name("ID").readOnly(true).horizontal("center")
							.onClick(data-> Router.location(data.idx(), true)).build(),
					column("Serial").build(),
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
					column("비고").build()
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
		String publishDt = value.report().publishAt().isEmpty() ? DataTransformUtil.formatDate((long) JsDate.parse(value.report().publishAt())) : "";
		return new Data(value.request().sample().id()+"$"+value.request().service().id())
				.put("ID", String.valueOf(value.request().sample().id()))
				.put("Serial", value.request().serial())
				.put("검사코드", value.request().service().id())
				.put("검사명", value.request().service().name())
				.put("수진자명", value.request().sample().patient().name())
				.put("성별", value.request().sample().patient().sex())
				.put("MRN", value.request().sample().patient().mrn())
				.put("의뢰일", DataTransformUtil.formatDate((long) JsDate.parse(value.request().dateRequest())))
				.put("TAT", DataTransformUtil.formatDate((long) JsDate.parse(value.request().dateDue())))
				.put("의뢰기관", value.request().sample().patient().customer())
				.put("분석일", DataTransformUtil.formatDate((long) JsDate.parse(value.createAt())))
				.put("Batch", value.batch())
				.put("Row", String.valueOf(value.row()))
				.put("reportCreated", String.valueOf((long) JsDate.parse(value.report().createAt())))
				.put("결과지", value.report().fileName())
				.put("발송자", value.report().publisher().name())
				.put("결과발송일", publishDt);
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
}
