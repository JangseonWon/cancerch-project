package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.api.WorklistApi;
import com.gcgenome.lims.data.Worklist;
import elemental2.core.JsDate;
import elemental2.dom.DomGlobal;
import elemental2.dom.Event;
import elemental2.promise.Promise;
import lombok.experimental.Delegate;
import net.sayaya.ui.*;

import static org.jboss.elemento.Elements.body;
import static org.jboss.elemento.Elements.div;

public class CreateBatchDialog {
    public static CreateBatchDialog build(String worklistId) {
        return new CreateBatchDialog(worklistId);
    }
    private final ButtonElementText btnCancel = ButtonElement.flat().text("Cancel");
    private final ButtonElementText btnSubmit = ButtonElement.contain().text("OK");
    private final ButtonElementToggle btnUnlock = ButtonElement.toggle().text("잠금해제").style("margin-left:auto;").value(false);
    private final TextFieldElement<Double, TextFieldElement.TextFieldOutlined<Double>> iptBatchCount = TextFieldElement.numberBox().outlined().text("Batch").css("button", "input").style("height:36px; width:33%;").required(true).enabled(false);
    private final TextFieldElement<Double, TextFieldElement.TextFieldOutlined<Double>> iptBatchYear = TextFieldElement.numberBox().outlined().text("Year").css("button", "input").style("height:36px; width:33%;").required(true).value((double) new JsDate().getFullYear()).enabled(false);
    @Delegate
    private Dialog dialog = Dialog.confirmation("AVOID Batch 정보입력", btnCancel, btnSubmit);
    private final String worklistId;
    private DropDownElement iptBatchName;
    public CreateBatchDialog(String worklistId) {
        this.worklistId = worklistId;
        ListElement<ListElement.SingleLineItem> iptBatchNames = ListElement.singleLineList();
        iptBatchNames.add(ListElement.singleLine().label("AVOID")).add(ListElement.singleLine().label("AVOIDTEST"));
        iptBatchName = DropDownElement.outlined(iptBatchNames).css("button", "input").text("Name").enabled(false).style("min-width:25%; max-width:34%; width:34%;");
        WorklistApi.batchCurrent().then(result ->{
            iptBatchName.select(result.substring(2));
            WorklistApi.max(result).then(max ->{
                iptBatchCount.value(Double.valueOf(max));
                return null;
            });
            return null;
        });
        btnCancel.onClick(evt->dialog.close());
        btnUnlock.onClick(this::unlock);
        dialog.add(div().style("display:flex;padding-bottom:5px;").add(btnUnlock))
                .add(div().style("display:flex;").add(iptBatchYear).add(iptBatchName).add(iptBatchCount));
        body().add(dialog);
    }

    private void unlock(Event event) {
        if (!btnUnlock.value()) {
            btnUnlock.text("잠금해제");
            WorklistApi.batchCurrent().then(result ->{
                iptBatchName.select(result.substring(2));
                WorklistApi.max(result).then(max ->{
                    iptBatchCount.value(Double.valueOf(max));
                    iptBatchYear.value((double) new JsDate().getFullYear());
                    return null;
                });
                return null;
            });
        } else btnUnlock.text("잠금");

        iptBatchYear.enabled(btnUnlock.value());
        iptBatchName.enabled(btnUnlock.value());
        iptBatchCount.enabled(btnUnlock.value());
    }

    public Promise<Worklist> onSubmit() {
        return new Promise<>((resolve, reject)-> {
            btnSubmit.onClick(evt -> {
                String prefix = iptBatchYear.value()-2000+iptBatchName.value();
                Worklist worklist = new Worklist().id(worklistId).prefix(prefix)
                        .idx(iptBatchCount.value().intValue());
                WorklistApi.merge(worklist).then(result ->{
                    DomGlobal.alert("생성을 완료했습니다.");
                    dialog.close();
                    dialog.element().remove();
                    resolve.onInvoke(new Worklist().id(worklistId).prefix("").idx(iptBatchCount.value().intValue()));
                    return null;
                });
            });
            btnCancel.onClick(evt -> {
                dialog.close();
                dialog.element().remove();
                reject.onInvoke(null);
            });
            dialog.open();
        });
    }
}
