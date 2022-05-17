package com.greencross.lims.client;

import com.greencross.lims.api.AnalysisApi;
import com.greencross.lims.data.*;
import com.greencross.lims.data.Request;
import com.greencross.lims.dto.Sheet.ColumnDefinition;
import com.greencross.lims.sheet.Data;
import com.greencross.lims.sheet.Sheet;
import com.greencross.lims.sheet.SheetState;
import com.greencross.lims.sheet.SpreadSheet;
import com.greencross.lims.sheet.function.ColumnHeaderRenderer;
import com.greencross.lims.util.DataTransformUtil;
import elemental2.core.JsArray;
import elemental2.dom.*;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;

@Setter
@Accessors(fluent=true)
public class AnalysisGridElement extends HTMLElementBuilder<HTMLDivElement, AnalysisGridElement> implements HasSelectionChangeHandlers<Analysis[]>, HasStateChangeHandlers<SheetState> {
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private Sheet sheet;
	private Map<String, Analysis> values;
	private static com.greencross.lims.dto.Sheet.LinkClickCallbackFn download = evt->{
		String href = evt.href();
		String[] split = href.split("-");
		String sample = split[0];
		String service = split[1];
		String report = split[2];
		AnalysisApi.download(Long.parseLong(sample), service, Long.parseLong(report))
				   .last(blob->{
					   String url = URL.createObjectURL(blob);
					   DomGlobal.window.open(url);
					   URL.revokeObjectURL(url);
				   });
	};
	private static ColumnDefinition[] columns() {
		return new ColumnDefinition[]{
				new ColumnDefinition().width(100).readonly(true).id("ID").name("ID").type(ColumnDefinition.ColumnType.LINK).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER).font("JetBrains Mono").fontSize(11).bold(true)).href("#"),
				new ColumnDefinition().width(80).readonly(true).id("Serial").name("Serial").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER).font("JetBrains Mono").fontSize(11).bold(true)),
				new ColumnDefinition().width(100).readonly(true).id("검사명").name("검사명").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER)),
				new ColumnDefinition().width(80).readonly(true).id("수진자명").name("수진자명").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER)),
				new ColumnDefinition().width(30).readonly(true).id("성별").name("성별").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER)),
				new ColumnDefinition().width(70).readonly(true).id("생년월일").name("생년월일").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER)),
				new ColumnDefinition().width(80).readonly(true).id("MRN").name("MRN").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER).font("JetBrains Mono").fontSize(11)),
				new ColumnDefinition().width(60).readonly(true).id("의뢰일").name("의뢰일").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER).font("JetBrains Mono").fontSize(11)),
				new ColumnDefinition().width(60).readonly(true).id("TAT").name("TAT").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER).font("JetBrains Mono").fontSize(11)),
				new ColumnDefinition().width(90).readonly(true).id("의뢰기관").name("의뢰기관").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER)),
				new ColumnDefinition().width(120).readonly(true).id("분석일").name("분석일").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER).font("JetBrains Mono").fontSize(11)),
				new ColumnDefinition().width(70).readonly(true).id("Batch").name("Batch").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER).font("JetBrains Mono").fontSize(11)),
				new ColumnDefinition().width(30).readonly(true).id("Row").name("#").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
				new ColumnDefinition().width(150).readonly(true).id("결과지").name("결과지").type(ColumnDefinition.ColumnType.LINK).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER)).callback(download).href("[\"Report\"]"),
				new ColumnDefinition().width(120).readonly(true).id("결과발송일").name("결과발송일").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.CENTER)),
				new ColumnDefinition().width(150).id("비고").name("비고").type(ColumnDefinition.ColumnType.STRING).styleText(new com.greencross.lims.dto.Sheet.StyleText().align(com.greencross.lims.dto.Sheet.Alignment.LEFT))
		};
	}
	@Builder
	private AnalysisGridElement() {
		this(div());
	}
	private AnalysisGridElement(HtmlContentBuilder<HTMLDivElement> e) {
		super(e);
		_this = e;
		layout();
	}
	public void layout() {
		_this.textContent("");
		SpreadSheet.SheetBuilder builder = SpreadSheet.builder()
													  .rowHeaders(true)
													  .autoColSize(true)
													  .rowHeaderWidth(new int[] {30})
													  .autoRowSize(false)
													  .manualColumnMove(true)
													  .manualColumnResize(true)
													  .stretchH("all")
													  .data(new Data[0]);
		sheet = Sheet.build(builder, Arrays.stream(columns()).map(c->c.renderer((sheet, td, row, col, prop, value, ci)->{
			Data data = sheet.spreadsheet.values()[row];
			if("true".equalsIgnoreCase(data.get("cancel")) || "true".equalsIgnoreCase(data.get("delete"))) {
		//		td.style.textDecoration = "line-through";
		//		td.style.color = "#CCC";
			}
			return td;
		})).toArray(ColumnDefinition[]::new));
		builder.afterGetColumnHeaderRenderers(renderers->{
			ColumnHeaderRenderer defaultRenderer = renderers[0];
			ColumnHeaderRenderer proxy = (row, TH)  ->{
				if(row == -1) {
					TH.innerHTML = "<input class='select-all-header-checkbox' type='checkbox' style='vertical-align: middle;margin: 0px;'/>";
				} else defaultRenderer.apply(row, TH);
			};
			renderers[0] = proxy;
		}).afterGetRowHeaderRenderers(renderers->{
			Data[] data = builder.data();
			JsArray.asJsArray(renderers).push((row, TH)->{
				boolean checked = data[row].state() == Data.DataState.SELECTED;
				TH.innerHTML = "<input class='row-header-checkbox' " +
						"idx='" + data[row].idx() + "' " + (checked?"checked ":"") +
					   "type='checkbox' style='vertical-align: middle;margin: 0px;'/>";
			});
		}).rowHeaders(false);
		sheet.element().addEventListener("click", evt->{
			HTMLElement target = (HTMLElement) evt.target;
			if(target.classList.contains("row-header-checkbox")) {
				String idx = target.getAttribute("idx");
				HTMLInputElement checkbox = (HTMLInputElement)target;
				Arrays.stream(builder.data()).filter(d->idx.equals(d.idx())).findAny().get().select(checkbox.checked);
			} else if(target.classList.contains("select-all-header-checkbox")) {
				HTMLInputElement checkbox = (HTMLInputElement)target;
				Arrays.stream(builder.data()).forEach(d->d.select(checkbox.checked));
				sheet.element().getElementsByClassName("row-header-checkbox").asList().forEach(e->((HTMLInputElement)e).checked = checkbox.checked);
			}
		});
		SelectionChangeEventListener<Data[]> wrapper = evt->{
			Analysis[] selection = Arrays.stream(evt.selection()).map(Data::idx).map(values::get).toArray(Analysis[]::new);
			SelectionChangeEvent<Analysis[]> evt2 = SelectionChangeEvent.event(evt.event(), selection);
			for(SelectionChangeEventListener<Analysis[]> listener: selectionChangeEventListeners) listener.handle(evt2);
		};
		sheet.onSelectionChange(wrapper);
		/*sheet.onStateChange(evt->{
			for(StateChangeEventListener<SheetState> listener: stateChangeEventListeners) listener.handle(evt);
		});*/
		_this.add(sheet);
	}
	public void refresh() {
		sheet.refresh();
	}
	public AnalysisGridElement update(Analysis data) {
		Data convert = map(data);
		Arrays.stream(sheet.value())
			  .filter(d->d.idx().equals(convert.idx()))
			  .findFirst()
			  .ifPresent(data2->{
				  data2.initialize("ID", convert.get("id"))
					   .initialize("비고", data.etc());
				  sheet.refresh();
			  });
		return that();
	}
	public AnalysisGridElement update(Analysis[] data) {
		this.values = Arrays.stream(data).collect(Collectors.toMap(a->a.id() + "/" + a.request().service().id(), w->w));
		sheet.value(Arrays.stream(data).map(this::map).toArray(Data[]::new));
		return that();
	}
	private Data map(Analysis dto) {
		Data data = new Data(dto.id()+dto.request().service().id()).initialize("ID", dto.id())
										  .initialize("Batch", dto.batch())
										  .initialize("Row", String.valueOf(dto.row()))
										  .initialize("분석일", DataTransformUtil.formatDateTime(dto.createAt()));
		Request request = dto.request();
		if(request!=null) {
			data.initialize("Serial", dto.request().serial())
				.initialize("cancel", String.valueOf(request.canceled()))
				.initialize("delete", String.valueOf(request.deleted()))
				.initialize("의뢰일", DataTransformUtil.formatDate(request.dateRequest()))
				.initialize("TAT", DataTransformUtil.formatDate(request.dateDue()));
			Sample sample = request.sample();
			if(sample!=null) {
				data.initialize("sample", String.valueOf(sample.id()));
				Patient patient = sample.patient();
				data.initialize("수진자명", patient.name())
					.initialize("성별", patient.sex())
					.initialize("생년월일", DataTransformUtil.formatDate(patient.birth()))
					.initialize("의뢰기관", patient.customer())
					.initialize("MRN", patient.mrn());
			}
			Service service = request.service();
			if(service!=null) {
				data.initialize("service", String.valueOf(service.id()))
					.initialize("검사명", dto.request().service().name());
			}
		}
		Report report = dto.report();
		if(report!=null) {
			data.initialize("결과지", report.fileName())
				.initialize("Report", data.get("sample") + "-" + data.get("service") + "-" + report.createAt())
				.initialize("결과발송일", DataTransformUtil.formatDateTime(report.publishAt()));
		}
		return data;
	}
	public AnalysisChanges[] changed() {
		return Arrays.stream(sheet.changed())
					 .map(data-> AnalysisChanges.builder().sample(Long.parseLong(data.get("sample"))).service(data.get("service")).info(data.get("비고")).build())
					 .toArray(AnalysisChanges[]::new);
	}
	public Analysis[] values() {
		return Arrays.stream(sheet.value()).map(this::map).toArray(Analysis[]::new);
	}
	private Analysis map(Data data) {
		String key = data.get("ID") + "/" + data.get("service");
		return values.get(key);
	}
	@Override
	public AnalysisGridElement that() {
		return this;
	}

	@Override
	public Analysis[] selection() {
		return Arrays.stream(sheet.selection()).map(d->d.get("ID") + "/" + d.get("service")).map(values::get).toArray(Analysis[]::new);
	}
	private final Set<SelectionChangeEventListener<Analysis[]>> selectionChangeEventListeners = new HashSet<>();
	@Override
	public HandlerRegistration onSelectionChange(SelectionChangeEventListener<Analysis[]> selectionChangeEventListener) {
		selectionChangeEventListeners.add(selectionChangeEventListener);
		return ()->selectionChangeEventListeners.remove(selectionChangeEventListener);
	}
	private final Set<StateChangeEventListener<SheetState>> stateChangeEventListeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<SheetState>> listeners() {
		return stateChangeEventListeners;
	}
	@Override
	public SheetState state() {
		return sheet.state();
	}
}
