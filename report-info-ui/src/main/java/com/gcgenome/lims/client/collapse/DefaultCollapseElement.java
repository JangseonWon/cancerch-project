package com.gcgenome.lims.client.collapse;

import com.gcgenome.lims.client.CollapseElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.ui.IconElement;
import elemental2.core.Global;
import elemental2.dom.*;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.elemento.Elements.*;

public class DefaultCollapseElement extends HTMLElementBuilder<HTMLDivElement, DefaultCollapseElement> implements CollapseElement<HTMLDivElement> {
	public static DefaultCollapseElement build(String id, long sample, JsPropertyMap<?> service) {
		return new DefaultCollapseElement(div(), id, sample, service);
	}
	private final IconElement icon = IconElement.icon(IconElement.Type.Light, "fa-clipboard-prescription");
	private final HtmlContentBuilder<HTMLElement> title = span().css("mdc-list-item__primary-text").add("Report");
	private final HtmlContentBuilder<HTMLElement> info = span().css("mdc-list-item__secondary-text").add("결과 보고서를 생성하고 열람합니다.");
	private final HtmlContentBuilder<HTMLElement> meta = span().css("mdc-list-item__meta");
	private final HtmlContentBuilder<HTMLLabelElement> summaryReport = label();
	private final HtmlContentBuilder<HTMLTableElement> summary = table().style("margin-right: 40px;text-align: right; font-size: var(--mdc-typography-caption-font-size, 0.75rem);")
																		.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
																				.add("Report: ")).add(td().add(summaryReport)));
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private final String id;
	private DefaultCollapseElement(HtmlContentBuilder<HTMLDivElement> e, String id, long sample, JsPropertyMap<?> service) {
		super(e.css("work-summary")
			   .style("margin-left: 16px;margin-right: 16px;display: flex; flex-direction: row; " +
					  "flex-wrap: nowrap; align-content: space-between; justify-content: space-between; " +
					  "align-items: center; cursor: pointer;"));
		_this = e;
		this.id = id;
		layout();
		this.on(EventType.click, evt->fireStateChangeEvent());
		this.summary.on(EventType.click, evt->{
			evt.stopPropagation();
			evt.preventDefault();
			// if(last!=null) ReportApi.download(last.fileUrl(), last.fileName());
		});
		this.summary.on(EventType.mouseover, evt->{
			evt.stopPropagation();
			evt.preventDefault();
		});
	}

	private void layout() {
		_this.add(span().css("mdc-list-item__graphic").style("height: auto; align-self: unset;").add(icon))
			 .add(div().css("mdc-list-item__text").style("margin-bottom: 10px;").add(title).add(info))
			 .add(meta);
	}
	@Override
	public void update() {
		Message msg = Message.builder().id(id).type(Message.MessageType.COLLAPSE).param("64px").build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		meta.element().innerHTML = "";
		meta.add(label("Loading...").style("margin-right: 40px;"));
		/*ReportApi.reports(sample, service).then(reports->{
			this.update(reports);
			return null;
		});*/
	}
	/*
	private void update(Report[] values) {
		meta.element().innerHTML = "";
		meta.add(summary);
		if(values != null && values.length > 0) {
			last = Arrays.stream(values).max(Comparator.comparing(Report::createAt)).get();
			summary.element().innerHTML = "";
			summary.add(tr().add(td().add(a().add(last.fileName()).attr("href", last.fileUrl()))))
					.add(tr().add(td().add("Published at ").add(summaryReport)));
			if(last.publishAt()!=null) summaryReport.element().innerHTML = DataTransformUtil.formatDateTime(last.publishAt());
			else summaryReport.element().innerHTML = "Not yet.";
		} else summaryReport.element().innerHTML = "Not yet.";
	}*/
	@Override
	public DefaultCollapseElement that() {
		return this;
	}
	private final Set<StateChangeEventListener<WindowState>> listeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<WindowState>> listeners() {
		return listeners;
	}
	@Override
	public WindowState state() {
		return WindowState.FULLSCREEN;
	}
}
