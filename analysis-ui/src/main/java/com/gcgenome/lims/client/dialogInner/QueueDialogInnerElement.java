package com.gcgenome.lims.client.dialogInner;

import com.gcgenome.lims.client.DataTransformUtil;
import com.gcgenome.lims.data.Report;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasValueChangeHandlers;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jboss.elemento.Elements.*;

public class QueueDialogInnerElement extends HTMLElementBuilder<HTMLDivElement, QueueDialogInnerElement> {
    public static QueueDialogInnerElement instance() { return new QueueDialogInnerElement(div()); }
    private final HtmlContentBuilder<HTMLDivElement> _this;
    private final Map<String, InnerCardElement> values = new HashMap<>();
    private QueueDialogInnerElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.css("print-publish-dialog-inner").style("width:100%"));
        _this = e;
    }
    public QueueDialogInnerElement init(Report[] reports) {
        Arrays.stream(reports).map(InnerCardElement::instance).map(this::deckIn).forEach(_this::add);
        return that();
    }
    public QueueDialogInnerElement onCreate(HasValueChangeHandlers.ValueChangeEvent<Report> evt){
        Report report = evt.value();
        values.get(report.sample()+"$"+report.service()).change(InnerCardElement.Status.PREPARE);
        return that();
    }
    public QueueDialogInnerElement onPrinting(HasValueChangeHandlers.ValueChangeEvent<Report> evt){
        Report report = evt.value();
        values.get(report.sample()+"$"+report.service()).change(InnerCardElement.Status.PRINTING);
        return that();
    }
    public QueueDialogInnerElement onFinish(HasValueChangeHandlers.ValueChangeEvent<Report> evt){
        Report report = evt.value();
        values.get(report.sample()+"$"+report.service()).change(InnerCardElement.Status.FINISHED);
        return that();
    }
    private InnerCardElement deckIn(InnerCardElement card){
        values.put(card.value.sample()+"$"+card.value.service(), card);
        return card;
    }
    @Override
    public QueueDialogInnerElement that() { return this; }
    public static class InnerCardElement extends HTMLElementBuilder<HTMLDivElement, InnerCardElement> {
        public static InnerCardElement instance(Report value) { return new InnerCardElement(div(), value); }
        private final HtmlContentBuilder<HTMLDivElement> innerHeader = div().css("print-publish-card-header");
        private final HtmlContentBuilder<HTMLDivElement> innerBody = div().css("print-publish-card-body");
        private final HtmlContentBuilder<HTMLDivElement> _this;
        private final Report value;
        private InnerCardElement(HtmlContentBuilder<HTMLDivElement> e, Report value) {
            super(e.css("print-publish-card"));
            this.value = value;
            _this = e;
            e.add(innerHeader.add(label(DataTransformUtil.formatSampleId(value.sample())).css("print-publish-card-header-sample"))
                    .add(label(value.service()).css("print-publish-card-header-service")))
                    .add(hr().style("width: 90%;"))
                    .add(innerBody.style("display: flex; justify-content: center; align-items: center;"));
            change(Status.PREPARE);
        }
        public Report value() { return value; }
        private InnerCardElement change(Status status){
            switch(status){
                case PREPARE:  {
                    innerBody.element().innerHTML = "출력 대기중";
                    css("prepare");
                    ncss("printing");
                    ncss("finished");
                    break;
                }
                case PRINTING: {
                    innerBody.element().innerHTML = "출력 중...";
                    ncss("prepare");
                    css("printing");
                    ncss("finished");
                    break;
                }
                case FINISHED: {
                    innerBody.element().innerHTML = "출력 완료";
                    ncss("prepare");
                    ncss("printing");
                    css("finished");
                    break;
                }
                default: {

                }
            }
            return that();
        }
        enum Status {
            PREPARE, PRINTING, FINISHED
        }
        @Override
        public InnerCardElement that() { return this; }
    }
}
