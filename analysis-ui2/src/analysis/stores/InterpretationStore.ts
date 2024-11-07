import {InterpretationDialogData} from "../../common/types/Types";
import {create} from "zustand";

interface InterpretationState {
    interpretationDialog: InterpretationDialogData
    setInterpretationDialog: (newInterpretationDialog: InterpretationDialogData) => void
}
const useInterpretationDialogStore = create<InterpretationState>()((set) => ({
    interpretationDialog: {
        open: false,
        interpretation: "",
        batchName: "",
        rowNumber: "",
        patientName: "",
        serviceCode: "",
        serviceName: "",
        sampleId: ""
    },
    setInterpretationDialog: (newInterpretationDialog) => set(state=>({interpretationDialog: newInterpretationDialog}))
}))

export default useInterpretationDialogStore
