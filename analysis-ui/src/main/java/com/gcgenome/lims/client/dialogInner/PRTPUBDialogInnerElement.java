package com.gcgenome.lims.client.dialogInner;

import com.gcgenome.lims.client.DataTransformUtil;
import com.gcgenome.lims.data.Analysis;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.jboss.elemento.Elements.*;

public class PRTPUBDialogInnerElement extends HTMLElementBuilder<HTMLDivElement, PRTPUBDialogInnerElement> {
    public static PRTPUBDialogInnerElement build(Analysis[] values) { return new PRTPUBDialogInnerElement(div(), values); }
    private final Map<String, InnerCardElement> values = new HashMap<>();
    private PRTPUBDialogInnerElement(HtmlContentBuilder<HTMLDivElement> e, Analysis[] values) {
        super(e.css("print-publish-dialog-inner").style("width:100%"));
        Arrays.stream(values).map(InnerCardElement::instance).map(this::deckIn).forEach(e::add);
    }
    private InnerCardElement deckIn(InnerCardElement card) {
        values.put(card.value.request().sample().id()+"$"+card.value.request().service().id(), card);
        return card;
    }
    public PRTPUBDialogInnerElement remove(Long sample, String service){
        values.get(sample+"$"+service).element().remove();
        return that();
    }

    @Override
    public PRTPUBDialogInnerElement that() {
        return this;
    }
    public static class InnerCardElement extends HTMLElementBuilder<HTMLDivElement, InnerCardElement> {
        public static InnerCardElement instance(Analysis value) { return new InnerCardElement(div(), value); }
        private final HtmlContentBuilder<HTMLDivElement> innerHeader = div().css("print-publish-card-header");
        private final HtmlContentBuilder<HTMLDivElement> innerBody = div().css("print-publish-card-body");
        private final Analysis value;
        private InnerCardElement(HtmlContentBuilder<HTMLDivElement> e, Analysis value) {
            super(e.css("print-publish-card"));
            e.style("background-color:"+resultToBgColor(value.result()));
            if(value.report().fileName() != null) e.css("twinkle");
            e.add(innerHeader).add(hr().style("width:95%;")).add(innerBody);
            innerHeader.add(label(DataTransformUtil.formatSampleId(value.request().sample().id())).css("print-publish-card-header-sample"))
                    .add(label(value.request().service().name()).css("print-publish-card-header-service"));
            innerBody.add(colgroup().add(col().style("width:40%")).add(col().style("width:60%")))
                    .add(tbody().add(tr().add(td().add(label().css("label").add("결과분석 :"))).add(td().add(resultToWord(value.result()))))
                            .add(tr().add(td().add(label().css("label").add("수진자명 :"))).add(td().add(value.request().sample().patient().name())))
                            .add(tr().add(td().add(label().css("label").add("예측암종 :"))).add(td().add(predToWord(value)))));
            this.value = value;
        }
        public Analysis value() { return value; }
        private String resultToBgColor(String result){
            switch(result){
                case "RISK":     return "#D9341D";
                case "CONCERN" : return "#EFA718";
                case "GENERAL" : return "#8DC556";
                default :        return "#FFFFFF";
            }
        }
        private String resultToWord(String result) {
            switch(result) {
                case "RISK":    return "집중관리";
                case "CONCERN": return "관심관리";
                case "GENERAL": return "일반관리";
                default:        return "이상한데?";
            }
        }
        private String predToWord(Analysis value){
            if(value.request().sample().patient().sex().equals("F")){
                switch(value.too6Pred()) {
                    case "ESO"  : 	return "식도암";
                    case "HCC"  : 	return "간암";
                    case "OV"   : 	return "난소암";
                    case "colon":   return "대장암";
                    case "LuC"  : 	return "폐암";
                    case "Panc" : 	return "췌장암";
                    case "Others":  return "기타암";
                    default     :	return "WTF";
                }
            }
            else{
                switch(value.too5Pred()) {
                    case "ESO"  : 	return "식도암";
                    case "HCC"  : 	return "간암";
                    case "colon":   return "대장암";
                    case "LuC"  : 	return "폐암";
                    case "Panc" : 	return "췌장암";
                    case "Others":  return "기타암";
                    default     :	return "WTF";
                }
            }
        }
        @Override
        public InnerCardElement that() { return this; }
    }
}
