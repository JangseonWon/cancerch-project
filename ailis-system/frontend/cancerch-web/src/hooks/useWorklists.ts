import useSWR from 'swr'
import { worklistApi } from '../api/worklistApi'
import type { WorklistResponse, PagedWorklistResponse } from '../types/worklist'

export function useWorklists(page: number = 0, size: number = 20, status?: string) {
  const { data, error, isLoading, mutate } = useSWR<PagedWorklistResponse>(
    ['worklists', page, size, status],
    () => worklistApi.getWorklists(page, size, status),
    {
      revalidateOnFocus: true,
      refreshInterval: 30000, // Refresh every 30 seconds
    }
  )

  return {
    worklists: data,
    isLoading,
    isError: error,
    mutate,
  }
}

export function useWorklist(id: number | null) {
  const { data, error, isLoading, mutate } = useSWR<WorklistResponse>(
    id ? ['worklist', id] : null,
    () => (id ? worklistApi.getWorklist(id) : null),
    {
      revalidateOnFocus: true,
    }
  )

  return {
    worklist: data,
    isLoading,
    isError: error,
    mutate,
  }
}
