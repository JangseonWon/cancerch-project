import {ReportDialogData} from "../../common/types/Types";
import {create} from "zustand";

interface ReportDialogState {
    reportDialog: ReportDialogData,
    setReportDialog: (newReportDialog: ReportDialogData) => void
}

const useReportDialogStore = create<ReportDialogState>()((set) => ({
    reportDialog: {
        open: false,
        sampleId: "",
        serviceCode: "",
        serviceName: "",
        batchName: "",
        rowNumber: "",
        customerName: "",
        customerId: "",
        patientMrn: "",
        patientSex: "",
        patientName: "",
        reportCreateAt: "",
        reportCreateBy: "",
        description: "",
        reportName: "",
        fileUrl: "",
        reportPublishAt: "",
        reportPublishBy: "",
        reportResult: "",
        reportResultType: "",
        reportComment: "",
        reportText: ""
    },
    setReportDialog: (newReportDialog) => set(state => ({reportDialog: newReportDialog}))
}))

export default useReportDialogStore
