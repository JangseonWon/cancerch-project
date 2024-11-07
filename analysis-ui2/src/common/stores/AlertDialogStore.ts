import {create} from "zustand";
import {AlertData} from "../types/Types";

interface AlertState {
    dialog: AlertData
    setDialog: (newDialog: AlertData) => void
}

const useAlertDialogStore = create<AlertState>()((set) => ({
    dialog: {title: "", content: "", open: false},
    setDialog: (newDialog) =>
        set(() => ({ dialog: newDialog}))
}))

export default useAlertDialogStore
