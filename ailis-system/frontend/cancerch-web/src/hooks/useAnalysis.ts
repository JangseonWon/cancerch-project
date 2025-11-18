import useSWR from 'swr'
import { analysisApi } from '../api/analysisApi'
import type { AnalysisResultResponse, PagedAnalysisResponse } from '../types/analysis'

export function useAnalysisList(page: number = 0, size: number = 20, search?: string) {
  const { data, error, isLoading, mutate } = useSWR<PagedAnalysisResponse>(
    ['analysis', page, size, search],
    () => analysisApi.getAnalysisList(page, size, search),
    {
      revalidateOnFocus: true,
      refreshInterval: 30000, // Refresh every 30 seconds
    }
  )

  return {
    analysisList: data,
    isLoading,
    isError: error,
    mutate,
  }
}

export function useAnalysis(id: number | null) {
  const { data, error, isLoading, mutate } = useSWR<AnalysisResultResponse>(
    id ? ['analysis', id] : null,
    () => (id ? analysisApi.getAnalysis(id) : null),
    {
      revalidateOnFocus: true,
    }
  )

  return {
    analysis: data,
    isLoading,
    isError: error,
    mutate,
  }
}
