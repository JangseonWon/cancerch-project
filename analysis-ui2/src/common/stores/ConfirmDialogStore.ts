import {create} from "zustand";
import {ConfirmData} from "../types/Types";

interface ConfirmState {
    dialog: ConfirmData
    setDialog: (newDialog: ConfirmData) => void
}

const useConfirmDialogStore = create<ConfirmState>()((set) => ({
    dialog: {title: "", content: "", open: false},
    setDialog: (newDialog) =>
        set(() => ({ dialog: newDialog}))
}))

export default useConfirmDialogStore
