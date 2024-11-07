import {create} from "zustand";
import {AnalysisPredicates} from "../../common/types/Types";

interface PredicatesState {
    predicates: AnalysisPredicates
    setPredicates: (newPredicates: AnalysisPredicates) => void
}
const usePredicatesStore = create<PredicatesState>()((set) => ({
    predicates: {
        page: 0,
        limit: 10000000,
        sort_by: "Batch",
        asc: "false",
        filters: {
            to: new Date(),
            from: new Date(new Date().setMonth(new Date().getMonth() -1)),
            published: false,
            printed: false,
            pass: true
        }
    },
    setPredicates: (newPredicates) => set(state => ({predicates: newPredicates}))
}))

export default usePredicatesStore
