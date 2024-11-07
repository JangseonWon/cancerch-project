import {create} from "zustand";

interface SelectedAnalysisState {
    selectedAnalysis: object[]
    setSelectedAnalysis: (newSelectedAnalysis: object[]) => void
}

const useSelectedAnalysisStore = create<SelectedAnalysisState>()((set) => ({
    selectedAnalysis: [],
    setSelectedAnalysis: (newSelectedAnalysis) => set(state => ({selectedAnalysis: newSelectedAnalysis}))
}))

export default useSelectedAnalysisStore
