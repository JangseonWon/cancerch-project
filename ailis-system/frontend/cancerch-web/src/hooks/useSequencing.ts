import useSWR from 'swr'
import { sequencingApi } from '../api/sequencingApi'
import type { SequencingResponse, PagedSequencingResponse } from '../types/sequencing'

export function useSequencingList(page: number = 0, size: number = 20, status?: string) {
  const { data, error, isLoading, mutate } = useSWR<PagedSequencingResponse>(
    ['sequencing', page, size, status],
    () => sequencingApi.getSequencingList(page, size, status),
    {
      revalidateOnFocus: true,
      refreshInterval: 30000, // Refresh every 30 seconds
    }
  )

  return {
    sequencingList: data,
    isLoading,
    isError: error,
    mutate,
  }
}

export function useSequencing(id: number | null) {
  const { data, error, isLoading, mutate } = useSWR<SequencingResponse>(
    id ? ['sequencing', id] : null,
    () => (id ? sequencingApi.getSequencing(id) : null),
    {
      revalidateOnFocus: true,
    }
  )

  return {
    sequencing: data,
    isLoading,
    isError: error,
    mutate,
  }
}
