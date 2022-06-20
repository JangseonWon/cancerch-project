package com.greencross.lims.client;

import com.greencross.lims.data.*;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetState;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnString;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.*;

import static org.jboss.elemento.Elements.div;

public class AnalysisGridElement extends HTMLElementBuilder<HTMLDivElement, AnalysisGridElement> implements HasSelectionChangeHandlers<Analysis[]>, HasStateChangeHandlers<SheetState> {
	public static AnalysisGridElement build() { return new AnalysisGridElement(div()); }
	private static ColumnString column(String name) {
		return ColumnBuilder.string(name).name(name).readOnly(true).horizontal("center");
	}
	private final SheetElement.SheetConfiguration config = SheetElement.builder()
			.rowHeaders(true)
			.autoColSize(true)
			.autoRowSize(false)
			.manualColumnMove(true)
			.manualColumnResize(true)
			.stretchH("all")
			.columns(

			).data(new Data[10]);
	private final SheetElement elemSheet = config.build();

	private AnalysisGridElement(HtmlContentBuilder<HTMLDivElement> e) {
		super(e.style("width: 100%;"));
		HtmlContentBuilder<HTMLDivElement> table = div().style("overflow: hidden; width: 100%; height: 85vh; border-bottom: 1px solid #AAA;")
				.add(div().style("border-top: 1px solid #AAA;").add(elemSheet));
		e.add(table);
	}
	public AnalysisGridElement update(Analysis data) {

		return that();
	}
	public AnalysisGridElement update(Analysis[] data) {

		return that();
	}
	private Data map(Analysis dto) {
		Data data = new Data("test");
		return data;
	}
	public Analysis[] values() {
		return null;
	}
	private Analysis map(Data data) {
		return null;
	}
	@Override
	public AnalysisGridElement that() {
		return this;
	}

	@Override
	public Analysis[] selection() {
		return null;
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
		return null;
	}
}
