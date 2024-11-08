import {create} from "zustand";
import {SelectedAnalysis} from "../../common/types/Types";

interface SelectedAnalysisState {
    selectedAnalysis: SelectedAnalysis[]
    setSelectedAnalysis: (newSelectedAnalysis: SelectedAnalysis[]) => void
}

const useSelectedAnalysisStore = create<SelectedAnalysisState>()((set) => ({
    selectedAnalysis: [],
    setSelectedAnalysis: (newSelectedAnalysis) => set(state => ({selectedAnalysis: newSelectedAnalysis}))
}))

export default useSelectedAnalysisStore
