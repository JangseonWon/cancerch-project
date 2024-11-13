import {HistoryDialogData} from "../../common/types/Types";
import {create} from "zustand";

interface HistoryState {
    historyDialog: HistoryDialogData
    setHistoryDialog: (newHistoryDialog: HistoryDialogData) => void
}

const useHistoryDialogStore = create<HistoryState>()((set) => ({
    historyDialog: {
        open: false,
        history: "",
        batchName: "",
        rowNumber: 0,
        patientName: "",
        serviceCode: "",
        serviceName: "",
        sampleId: ""
    },
    setHistoryDialog: (newHistoryDialog) => set(state => ({historyDialog: newHistoryDialog}))
}))

export default useHistoryDialogStore
