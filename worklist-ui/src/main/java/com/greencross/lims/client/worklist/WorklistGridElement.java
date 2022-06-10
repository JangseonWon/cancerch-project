package com.greencross.lims.client.worklist;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.i18n.client.NumberFormat;
import com.greencross.lims.api.WindowApi;
import com.greencross.lims.data.Worklist;
import com.greencross.lims.util.DataTransformUtil;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.ListElement;
import net.sayaya.ui.MenuElement;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnString;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Arrays;

import static org.jboss.elemento.Elements.div;

public class WorklistGridElement extends HTMLElementBuilder<HTMLDivElement, WorklistGridElement>{
    public static WorklistGridElement build() { return new WorklistGridElement(div());}
    private static ColumnString column(String name) {
        return ColumnBuilder.string(name).name(name).readOnly(true).horizontal("center");
    }
    private final SheetElement.SheetConfiguration config = SheetElement.builder()
            .rowHeaders(false)
            .autoColSize(true)
            .autoRowSize(false)
            .manualColumnMove(true)
            .manualColumnResize(true)
            .stretchH("all")
            .columns(
                    column("작성일").horizontal("center").font("Nanum Gothic Coding").build(),
                    column("워크리스트 종류").build(),
                    ColumnBuilder.link("워크리스트 명", data->"#"+data.idx()).name("워크리스트 세부정보").readOnly(true)
                            .onClick(data-> WindowApi.open("avoid.html#" + data.idx(), "_blank", null, false)).build(),
                    column("작성자").horizontal("center").build(),
                    column("검체 수").horizontal("center").build(),
                    column("Comment").build()
            ).data(new Data[10]);
    private final SheetElement elemSheet = config.build();
    private final ListElement.SingleLineItem lblManager = ListElement.singleLine().label("담당자");
    private final ListElement.SingleLineItem lblPhone = ListElement.singleLine().label("연락처");
    private final ListElement.SingleLineItem lblRemark = ListElement.singleLine().label("비고입력");
    private final MenuElement menu = MenuElement.build(ListElement.singleLineList()
            .add(lblRemark)
            .divider()
            .add(lblManager)
            .add(lblPhone)).css("menu");

    private WorklistGridElement(HtmlContentBuilder<HTMLDivElement> e) {
        super(e.style("width: 100%;"));
        HtmlContentBuilder<HTMLDivElement> table = div().style("overflow: hidden; width: 100%; height: 78vh; border-bottom: 1px solid #AAA;")
                .add(div().style("border-top: 1px solid #AAA;").add(elemSheet));
        e.add(table);
        menu._for(element());
        menu.on(EventType.mouseleave, evt->menu.close());
    }
    public WorklistGridElement update(Worklist[] values){
        return update(Arrays.stream(values).map(this::map).toArray(Data[]::new));
    }
    private WorklistGridElement update(Data[] data){
        try {
            elemSheet.values(data).refresh();
            return that();
        } catch(Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private Data map(Worklist value){
        if(value == null) return null;
        NumberFormat NF = NumberFormat.getFormat("#");
        return new Data(value.id())
                .put("워크리스트 명", value.title())
                .put("추출일", DataTransformUtil.formatDateTime((long) JsDate.parse(value.created())))
                .put("상태", toString(value.status()))
                .put("Comment", value.remark());
    }
    private String toString(Worklist.Status status) {
        if(status == null) return null;
        switch (status) {
            case NORMAL:        return "Plate 검증 완료";
            case PRE_CREATE:    return "Worklist 생성 완료";
            case POST_CREATE:   return "Plate 생성 완료";
            case DISPOSAL:      return "Worklist 폐기";
            case MERGED:        return "Worklist 병합됨";
            case SEQUENCING:    return "시퀀싱 진행";
            case ANALYZED:      return "분석 완료";
            case COMPLETE:      return "전송 완료";
        }
        return null;
    }
    @Override
    public WorklistGridElement that() {
        return this;
    }
}