package com.gcgenome.lims.client;

import com.gcgenome.lims.api.AnalysisApi;
import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.RouteApi;
import com.gcgenome.lims.api.SampleApi;
import com.gcgenome.lims.client.dialogInner.PRTPUBDialogInnerElement;
import com.gcgenome.lims.client.dialogInner.QueueDialogInnerElement;
import com.gcgenome.lims.data.Analysis;
import com.gcgenome.lims.data.LinkRequest;
import com.gcgenome.lims.data.Report;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.ui.IconElement;
import elemental2.core.JsDate;
import elemental2.dom.*;
import elemental2.promise.Promise;
import net.sayaya.ui.*;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.*;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.*;

public class AnalysisScene extends AbstractScenePageable<AnalysisScene> {
    private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Analysis");
    private final BreadcrumbElement breadcumb = BreadcrumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt -> {
                RouteApi.location("", true, false);
            }).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
            .add("액체생검", evt -> {
                evt.preventDefault();
                evt.stopPropagation();
            }).add("Analysis", evt -> {
                evt.preventDefault();
                evt.stopPropagation();
                Router.location("", true);
            });
    private final ButtonElementToggle btnAnalysisComplete = ButtonElement.toggle().css("button").text("결과지 전체 조회").style("width: 160px;").value(false);
    private final ButtonElementToggle btnProgressOnly = ButtonElement.toggle().css("button").text("미배포 목록 조회").style("width: 160px;").value(true);
    private final CheckBoxElement chkOnlyPass = CheckBoxElement.checkBox(true).text("PASS ONLY").style("margin-right: 30px;");
    private final TextFieldElement<JsDate, TextFieldElement.TextFieldOutlined<JsDate>> iptDateFrom = TextFieldElement.dateBox().outlined().css("button").style("width: 155px;border-right: 0px !important; height:36px;").text("Date from").value(prevday()).required(true);
    private final TextFieldElement<JsDate, TextFieldElement.TextFieldOutlined<JsDate>> iptDateTo = TextFieldElement.dateBox().outlined().css("button").style("width: 155px; height:36px;").text("Date to").value(new JsDate()).required(true);
    private final ButtonElement btnSearch = ButtonElement.outline().css("button").before(IconElement.icon(IconElement.Type.Light, "fa-search"));
    private final ButtonElement btnPdf = ButtonElement.outline().css("button").text("Print").before(IconElement.icon(IconElement.Type.Light, "fa-file-pdf"));
    private final ButtonElement btnPublish = ButtonElement.outline().css("button").text("Publish").before(IconElement.icon(IconElement.Type.Light, "fa-upload"));
    private final ButtonElement btnSave = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Regular, "fa-save"));
    private final ButtonElement btnQueue = ButtonElement.outline().css("button").text("대기열");
    private final ButtonElement btnLink = ButtonElement.outline().css("button").text("결과 연결");
    private final AnalysisGridElement grid = AnalysisGridElement.build();
    private final ButtonElementText cancel = ButtonElement.outline().text("CANCEL");
    private final ButtonElementText linkCancel = ButtonElement.outline().text("CANCEL");
    private final ButtonElementText btnLinkConfirm = ButtonElement.outline().text("LINK").enabled(false);
    private final Dialog dialog = Dialog.confirmation("출력, 전송 대기열", null, cancel);
    private final Dialog linkDialog = Dialog.confirmation("데이터 연결", btnLinkConfirm, linkCancel);
    private final TextFieldElement<String, TextFieldElement.TextFieldOutlined<String>> iptOriginSampleNumber = TextFieldElement.textBox().outlined().css("button").style("width: 195px; height: 36px; margin-right: 10px;").text("의뢰번호").required(true);
    private final TextFieldElement<String, TextFieldElement.TextFieldOutlined<String>> iptOriginServiceCode = TextFieldElement.textBox().outlined().css("button").style("width: 145px; height: 36px; margin-right: 10px;").text("검사코드").required(true);
    private final TextFieldElement<String, TextFieldElement.TextFieldOutlined<String>> iptOriginBatchName = TextFieldElement.textBox().outlined().css("button").style("width: 195px; height: 36px; margin-right: 10px;").text("배치명").required(true);
    private final TextFieldElement<String, TextFieldElement.TextFieldOutlined<String>> iptOriginRownumber = TextFieldElement.textBox().outlined().css("button").style("width: 145px; height: 36px; margin-right: 10px;").text("배치순서").required(true);
    private final TextFieldElement<String, TextFieldElement.TextFieldOutlined<String>> iptLinkedSampleNumber = TextFieldElement.textBox().outlined().css("button").style("width: 195px; height: 36px; margin-right: 10px;").text("의뢰번호").required(true);
    private final TextFieldElement<String, TextFieldElement.TextFieldOutlined<String>> iptLinkedServiceCode = TextFieldElement.textBox().outlined().css("button").style("width: 145px; height: 36px; margin-right: 10px;").text("검사코드").required(true);
    private final ButtonElement btnCheckOriginRequest = ButtonElement.outline().text("원의뢰 검증").style("min-width: 130px;");
    private final ButtonElement btnCheckLinkedRequest = ButtonElement.outline().text("연결의뢰 검증").style("min-width: 130px;");
    private final QueueDialogInnerElement inner = QueueDialogInnerElement.instance();
    private final Query query;

    public AnalysisScene(Query query) {
        super(query);
        this.query = query;
        initialize();
        this.sortable("ID", "의뢰일", "Batch", "row").sort("ID", true);
        ((HTMLElement) iptDateFrom.element().parentElement).style.display = "flex";
        ((HTMLElement) btnPdf.element().parentElement).style.display = "flex";
        grid.onSelectionChange(evt -> {
            boolean selected = evt.selection().length > 0;
            btnPublish.enabled(selected);
            btnPdf.enabled((grid.chkMulti() || grid.chkSingle()) && selected);
        });
        btnSearch.onClick(evt -> {
            update();
        });
        btnSave.onClick(evt -> save());
        btnPdf.onClick(evt -> print());
        btnPublish.onClick(evt -> publish());
        btnPdf.enabled(false);
        btnPublish.enabled(false);
        btnAnalysisComplete.onClick(evt -> {
            if (!btnAnalysisComplete.value()) btnAnalysisComplete.text("결과지 전체 조회");
            else btnAnalysisComplete.text("결과지 미생성 조회");
        });
        btnProgressOnly.onClick(evt -> {
            if (!btnProgressOnly.value()) btnProgressOnly.text("배포 전체 상태 조회");
            else btnProgressOnly.text("미배포 목록 조회");
        });
        dialogInit();
        linkDialogInit();
        btnQueue.onClick(evt -> dialog.open());
        btnLink.onClick(evt -> {
            linkDialog.open();
            iptOriginSampleNumber.focus();
        });
        SampleApi.PublishEvent.listen()
                .onFinish(evt -> {
                    update();
                });
    }

    private void save() {
        if (Arrays.stream(grid.changed()).findAny().isEmpty()) {
            DomGlobal.alert("변경사항이 없습니다.");
        } else {
            ProgressApi.open();
            AnalysisApi.update(grid.changed()).then(response -> {
                update();
                DomGlobal.alert("저장이 완료되었습니다.");
                return null;
            }).finally_(ProgressApi::close);
        }
    }

    private void update(Query query) {
        Query proxy = new Query().asc(this.isAsc());
        List<Query.Filter> filters = new LinkedList<>();
        if (query.filters != null) {
            Arrays.stream(query.filters).forEach(filter -> filter.key(" "));
            Collections.addAll(filters, query.filters);
        }
        filters.add(new Query.Filter().key("to").value(String.valueOf(iptDateTo.value().getTime() + 53940000)));
        filters.add(new Query.Filter().key("from").value(String.valueOf(iptDateFrom.value().getTime() - 32400000)));
        proxy.sortBy(this.sort());

        if (this.btnProgressOnly.value()) filters.add(new Query.Filter().key("published").value("true"));
        if (this.btnAnalysisComplete.value()) filters.add(new Query.Filter().key("printed").value("true"));
        if (this.chkOnlyPass.value()) filters.add(new Query.Filter().key("pass").value("true"));
        proxy.limit(show()).page((int) page());
        proxy.filters(filters.stream().toArray(Query.Filter[]::new));
        ProgressApi.open(false);
        AnalysisApi.search(proxy)
                .then(this::updateTotal)
                .then(Response::text)
                .then(this::map)
                .then(a -> {
                    grid.update(a);
                    return null;
                }).finally_(ProgressApi::close);
    }

    private Promise<Response> updateTotal(Response response) {
        total(Long.parseLong(response.headers.get("X-TOTAL-COUNT")));
        return Promise.resolve(response);
    }

    private Promise<Analysis[]> map(String json) {
        if (json != null && !json.trim().isEmpty()) return Promise.resolve((Analysis[]) JSON.parse(json));
        else return Promise.resolve((Analysis[]) null);
    }

    private Promise<Report[]> map2(String json) {
        if (json != null && !json.trim().isEmpty()) return Promise.resolve((Report[]) JSON.parse(json));
        else return Promise.resolve((Report[]) null);
    }

    @Override
    protected IsElement<?> grid() {
        return grid;
    }

    private static JsDate prevday() {
        JsDate today = new JsDate();
        JsDate yesterday = new JsDate(today);
        yesterday.setDate(yesterday.getDate() - 30);
        yesterday.setHours(0, 0, 0, 0);
        return yesterday;
    }

    @Override
    protected IconElement icon() {
        return IconElement.icon(IconElement.Type.Light, "fa-diagnoses");
    }

    @Override
    protected HtmlContentBuilder<HTMLLabelElement> title() {
        return title;
    }

    @Override
    protected BreadcrumbElement breadcrumb() {
        return breadcumb;
    }

    @Override
    protected IsElement<?>[][] controls() {
        return new IsElement<?>[][]{
                new IsElement[]{chkOnlyPass, btnAnalysisComplete, btnProgressOnly, btnLink},
                new IsElement<?>[]{iptDateFrom, label("~").style("line-height: 36px; margin-left: 2px; margin-right: 2px;"), iptDateTo, btnSearch, btnQueue},
                new IsElement[]{btnPdf, btnPublish, btnSave}
        };
    }

    @Override
    public void update() {
        update(query);
    }

    private void linkDialogInit() {
        HTMLElement surface = (HTMLElement) linkDialog.element().getElementsByClassName("mdc-dialog__surface").item(0);
        surface.style.minWidth = CSSProperties.MinWidthUnionType.of("600px");
        surface.style.minHeight = CSSProperties.MinHeightUnionType.of("400px");

        btnCheckOriginRequest.onClick(evt -> {
            if(iptOriginSampleNumber.value().isEmpty() || iptOriginServiceCode.value().isEmpty()) {
                DomGlobal.alert("원의뢰정보를 입력해주시기 바랍니다.");
                return ;
            }
            ProgressApi.open();
            AnalysisApi.chkOriginRequest(iptOriginSampleNumber.value(), iptOriginServiceCode.value(), iptOriginBatchName.value(), iptOriginRownumber.value())
                    .then(Response::text)
                    .then(response-> {
                        if(response.equals("true")) {
                            iptOriginSampleNumber.enabled(false);
                            iptOriginServiceCode.enabled(false);
                            iptOriginBatchName.enabled(false);
                            iptOriginRownumber.enabled(false);
                            btnCheckOriginRequest.enabled(false);
                            btnCheckOriginRequest.text("검증 완료");
                            if(Objects.equals(btnCheckLinkedRequest.text(), "검증 완료")) {
                                btnLinkConfirm.enabled(true);
                            }
                        }else {
                            DomGlobal.alert("원의뢰가 없습니다.\n원의뢰 결과가 LIMS에 정상 조회 가능한지 확인하시고\n동일 문제 발생 시 입력한 검사코드와 의뢰번호를 확인해주시기 바랍니다.");
                        }
                        ProgressApi.close();
                        return null;
                    });
        });
        btnCheckLinkedRequest.onClick(evt -> {
            if(iptLinkedSampleNumber.value().isEmpty() || iptLinkedServiceCode.value().isEmpty()) {
                DomGlobal.alert("연결의뢰정보를 입력해주시기 바랍니다.");
                return ;
            }
            AnalysisApi.chkLinkedRequest(iptLinkedSampleNumber.value(), iptLinkedServiceCode.value())
                    .then(Response::text)
                    .then(response -> {
                        if(response.equals("true")) {
                            iptLinkedSampleNumber.enabled(false);
                            iptLinkedServiceCode.enabled(false);
                            btnCheckLinkedRequest.enabled(false);
                            btnCheckLinkedRequest.text("검증 완료");
                            if(Objects.equals(btnCheckOriginRequest.text(), "검증 완료")) {
                                btnLinkConfirm.enabled(true);
                            }
                        } else {
                            DomGlobal.alert("연결 의뢰가 없습니다. 연결할 의뢰번호와 검사코드를 확인해주시기 바랍니다.");
                        }
                        return null;
                    });
        });

        linkCancel.onClick(evt -> {
            btnCheckLinkedRequest.text("연결의뢰 검증").enabled(true);
            btnCheckOriginRequest.text("원의뢰 검증").enabled(true);
            btnLinkConfirm.enabled(false);
            iptLinkedSampleNumber.value("").enabled(true);
            iptLinkedServiceCode.value("").enabled(true);
            iptOriginSampleNumber.value("").enabled(true);
            iptOriginServiceCode.value("").enabled(true);
            iptOriginBatchName.value("").enabled(true);
            iptOriginRownumber.value("").enabled(true);
            linkDialog.close();
        });
        btnLinkConfirm.onClick(evt -> {
            ProgressApi.open();
            LinkRequest dto = new LinkRequest();
            dto.originSample(iptOriginSampleNumber.value());
            dto.originService(iptOriginServiceCode.value());
            dto.batch(iptOriginBatchName.value());
            dto.row(iptOriginRownumber.value());
            dto.linkSample((iptLinkedSampleNumber.value()));
            dto.linkService(iptLinkedServiceCode.value());

            AnalysisApi.linkAnalysisData(dto)
                    .then(Response::text)
                    .then(response -> {
                        if(response.equals("true")) {
                            btnCheckLinkedRequest.text("연결의뢰 검증").enabled(true);
                            btnCheckOriginRequest.text("원의뢰 검증").enabled(true);
                            btnLinkConfirm.enabled(false);
                            iptLinkedSampleNumber.value("").enabled(true);
                            iptLinkedServiceCode.value("").enabled(true);
                            iptOriginSampleNumber.value("").enabled(true);
                            iptOriginServiceCode.value("").enabled(true);
                            iptOriginBatchName.value("").enabled(true);
                            iptOriginRownumber.value("").enabled(true);
                            DomGlobal.alert("연동을 완료했습니다.");
                            linkDialog.close();
                        } else {
                            DomGlobal.alert("연동에 실패했습니다. LIMS팀에 문의 바랍니다.");
                        }
                        ProgressApi.close();
                        return null;
                    });


        });
        linkDialog.add(div().style("display: flex; margin : 5px; flex-direction: column;")
                        .add(label("해당 기능은 기존 검사의 영문/국문 결과지 출력 및 동일인의 결과 연동을 위한 기능입니다."))
                        .add(label("사용법").style("font-size: 20px; margin-top:10px; margin-bottom: 10px;"))
                        .add(label("1. 원의뢰 결과정보 입력 후 검증"))
                        .add(label("2. 연결의뢰번호와 검사코드(ex. N203) 입력 후 검증"))
                        .add(label("3. 활성화 된 LINK 버튼 선택 후 작업 종료까지 대기")))
                .add(hr())
                .add(label("원 의뢰정보").style("font-size: 20px;"))
                .add(div().style("display: flex; margin: 10px;")
                        .add(iptOriginSampleNumber).add(iptOriginServiceCode))
                .add(div().style("display: flex; margin: 10px;")
                        .add(iptOriginBatchName).add(iptOriginRownumber).add(btnCheckOriginRequest))
                .add(hr())
                .add(label("연결 의뢰정보").style("font-size: 20px;"))
                .add(div().style("display: flex; margin : 10px;")
                        .add(iptLinkedSampleNumber).add(iptLinkedServiceCode).add(btnCheckLinkedRequest));

        body().add(linkDialog);
    }

    private void dialogInit() {
        HTMLElement surface = (HTMLElement) dialog.element().getElementsByClassName("mdc-dialog__surface").item(0);
        surface.style.minWidth = CSSProperties.MinWidthUnionType.of("1200px");
        surface.style.minHeight = CSSProperties.MinHeightUnionType.of("800px");
        SampleApi.works().then(Response::text).then(this::map2)
                .then(reports -> {
                    inner.init(reports);
                    return null;
                });

        SampleApi.PrintEvent.listen()
                .onCreate(inner::onCreate)
                .onUpdate(inner::onPrinting)
                .onFinish(evt -> {
                    inner.onFinish(evt);
                    update();
                });
        cancel.onClick(evt -> {
            dialog.close();
        });
        dialog.add(inner);
        body().add(dialog);
    }

    private void print() {
        Analysis[] selection = grid.selection();

        for (Analysis analysis : selection) {
            if (analysis.report().description() != "") {
                String description = DomGlobal.prompt("이력 입력 ");
                if (description.length() != 0) {
                    SampleApi.print(String.valueOf(analysis.request().sample().id()),
                            analysis.request().service().id(),
                            analysis.batch(),
                            String.valueOf(analysis.row()),
                            "kokr",
                            description
                    );
                } else {
                    DomGlobal.alert("취소되었습니다.");
                }
            } else {
                SampleApi.print(String.valueOf(analysis.request().sample().id()),
                        analysis.request().service().id(),
                        analysis.batch(),
                        String.valueOf(analysis.row()),
                        "kokr",
                        "");
            }

        }
        update();
    }

    private void publish() {
        Analysis[] selection = grid.selection();
        if (selection.length == 0) return;
        ButtonElementText ok = ButtonElement.outline().text("OK").enabled(false);
        ButtonElementText cancel = ButtonElement.outline().text("CANCEL");
        CheckBoxElement chkConfirm = CheckBoxElement.checkBox(false).text("위 내용을 확인했습니다.");
        Dialog dialog = Dialog.confirmation("선택한 " + selection.length + "개의 검사 결과지를 전송합니다.", ok, cancel);
        HTMLElement surface = (HTMLElement) dialog.element().getElementsByClassName("mdc-dialog__surface").item(0);
        surface.style.minWidth = CSSProperties.MinWidthUnionType.of("1200px");
        long countGeneral = Arrays.stream(selection).filter(d -> d.result().equals("GENERAL")).count();
        long countConcern = Arrays.stream(selection).filter(d -> d.result().equals("CONCERN")).count();
        long countRisk = Arrays.stream(selection).filter(d -> d.result().equals("RISK")).count();
        PRTPUBDialogInnerElement inner = PRTPUBDialogInnerElement.build(selection);

        chkConfirm.onValueChange(evt -> {
            ok.enabled(evt.value());
        });
        ProgressApi.open(false);
        ok.onClick(evt -> {
            for (Analysis analysis : selection) {
                Promise<Boolean> response = SampleApi.publish(String.valueOf(analysis.request().sample().id()), analysis.request().service().id(), String.valueOf((long) JsDate.parse(analysis.report().createAt())));
                response.then(res -> {
                    if (res.equals(true)) {
                        inner.remove(analysis.request().sample().id(), analysis.request().service().id());
                    } else {
                        DomGlobal.console.log("retry");
                        SampleApi.publish(String.valueOf(analysis.request().sample().id()), analysis.request().service().id(), String.valueOf((long) JsDate.parse(analysis.report().createAt())));
                    }
                    return null;
                });
            }
            dialog.close();
            dialog.element().remove();
            update();
        });
        cancel.onClick(evt -> {
            dialog.close();
            dialog.element().remove();
        });
        dialog.add(div().add(label("일반관리 : " + countGeneral + "건").style("margin-right: 1em;"))
                        .add(label("관심관리 : " + countConcern + "건").style("margin-right: 1em;"))
                        .add(label("집중관리 : " + countRisk + "건")))
                .add(inner).add(chkConfirm);
        body().add(dialog);
        dialog.open();
    }

    @Override
    public AnalysisScene that() {
        return this;
    }
}
