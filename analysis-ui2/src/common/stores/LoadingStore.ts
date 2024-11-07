import {create} from "zustand";
import {LoadingObject} from "../types/Types";

interface LoadingState {
    open: LoadingObject
    setOpen: (newOpen: LoadingObject) => void
}

const useLoadingStore = create<LoadingState>()((set) => ({
   open: {onLoading: false, loadingText: ""},
   setOpen: (newOpen) =>
   set(() => ({ open: newOpen}))
}))

export default useLoadingStore