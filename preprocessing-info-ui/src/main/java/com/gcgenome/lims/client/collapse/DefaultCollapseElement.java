package com.gcgenome.lims.client.collapse;

import com.gcgenome.lims.client.CollapseElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.ui.IconElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.HTMLTableElement;
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
	private final IconElement icon = IconElement.icon(IconElement.Type.Light, "fa-vials");
	private final HtmlContentBuilder<HTMLElement> title = span().css("mdc-list-item__primary-text").add("Preprocessing");
	private final HtmlContentBuilder<HTMLElement> info = span().css("mdc-list-item__secondary-text").add("시퀀싱 전처리 과정의 정보를 열람합니다.");
	private final HtmlContentBuilder<HTMLElement> meta = span().css("mdc-list-item__meta");
	private final HtmlContentBuilder<HTMLTableElement> summary = table().style("margin-right: 40px;");
	private final HtmlContentBuilder<HTMLLabelElement> summaryDna = label("Not yet.");
	private final HtmlContentBuilder<HTMLLabelElement> summaryLibrary = label("Not yet.");
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
	}

	private void layout() {
		_this.add(span().css("mdc-list-item__graphic").style("height: auto; align-self: unset;").add(icon))
			 .add(div().css("mdc-list-item__text").style("margin-bottom: 10px;").add(title).add(info))
			 .add(meta);
	}
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
