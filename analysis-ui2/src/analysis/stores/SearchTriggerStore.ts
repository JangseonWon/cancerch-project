import {create} from "zustand";

interface LoadingState {
    trigger: boolean
    setTrigger: (trigger: boolean) => void
}

const useSearchTriggerStore = create<LoadingState>()((set) => ({
    trigger: false,
    setTrigger: (newTrigger) =>
        set(() => ({ trigger: newTrigger}))
}))

export default useSearchTriggerStore
