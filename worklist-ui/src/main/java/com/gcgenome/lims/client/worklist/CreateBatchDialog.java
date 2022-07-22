package com.gcgenome.lims.client.worklist;

import com.gcgenome.lims.data.Worklist;
import elemental2.promise.Promise;
import lombok.experimental.Delegate;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.ButtonElementText;
import net.sayaya.ui.Dialog;

import static org.jboss.elemento.Elements.body;

public class CreateBatchDialog {
    public static CreateBatchDialog build(String worklistId) {
        return new CreateBatchDialog(worklistId);
    }
    private ButtonElementText btnCancel = ButtonElement.flat().text("Cancel");
    private ButtonElementText btnSubmit = ButtonElement.contain().text("OK");
    @Delegate
    private Dialog dialog = Dialog.confirmation("Batch Initializer", btnCancel, btnSubmit);
    private final String worklistId;
    public CreateBatchDialog(String worklistId) {
        this.worklistId = worklistId;
        btnCancel.onClick(evt->dialog.close());
        body().add(dialog);
    }
    public Promise<Worklist> onSubmit() {
        return new Promise<>((resolve, reject)-> {
            btnSubmit.onClick(evt -> {
                dialog.close();
                dialog.element().remove();
                resolve.onInvoke(new Worklist().id(worklistId).prefix("").idx(0));
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
