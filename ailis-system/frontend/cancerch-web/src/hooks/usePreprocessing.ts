import useSWR from 'swr'
import { preprocessingApi } from '../api/preprocessingApi'
import type {
  PreprocessingResponse,
  PagedPreprocessingResponse,
} from '../types/preprocessing'

export function usePreprocessings(
  page: number = 0,
  size: number = 20,
  status?: string
) {
  const { data, error, isLoading, mutate } = useSWR<PagedPreprocessingResponse>(
    ['preprocessings', page, size, status],
    () => preprocessingApi.getPreprocessings(page, size, status),
    {
      revalidateOnFocus: true,
      refreshInterval: 30000, // Refresh every 30 seconds
    }
  )

  return {
    preprocessings: data,
    isLoading,
    isError: error,
    mutate,
  }
}

export function usePreprocessing(id: number | null) {
  const { data, error, isLoading, mutate } = useSWR<PreprocessingResponse>(
    id ? ['preprocessing', id] : null,
    () => (id ? preprocessingApi.getPreprocessing(id) : null),
    {
      revalidateOnFocus: true,
    }
  )

  return {
    preprocessing: data,
    isLoading,
    isError: error,
    mutate,
  }
}
