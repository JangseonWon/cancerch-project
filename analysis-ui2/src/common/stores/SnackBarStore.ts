import {create} from "zustand";
import {SnackBarData} from "../types/Types";

interface SnackBarState {
    open: SnackBarData
    setSnackbar: (newOpen: SnackBarData) => void
}

const useSnackBarStore = create<SnackBarState>()((set) => ({
    open: {message: "", openType: "info", state: false},
    setSnackbar: (newOpen) =>
        set(() => ({ open: newOpen}))
}))

export default useSnackBarStore
