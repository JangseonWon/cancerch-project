import useSWR from 'swr'
import { reportApi } from '../api/reportApi'
import type { ReportResponse, PagedReportResponse } from '../types/report'

export function useReportList(page: number = 0, size: number = 20, status?: string) {
  const { data, error, isLoading, mutate } = useSWR<PagedReportResponse>(
    ['reports', page, size, status],
    () => reportApi.getReportList(page, size, status),
    {
      revalidateOnFocus: true,
      refreshInterval: 30000, // Refresh every 30 seconds
    }
  )

  return {
    reportList: data,
    isLoading,
    isError: error,
    mutate,
  }
}

export function useReport(id: number | null) {
  const { data, error, isLoading, mutate } = useSWR<ReportResponse>(
    id ? ['report', id] : null,
    () => (id ? reportApi.getReport(id) : null),
    {
      revalidateOnFocus: true,
    }
  )

  return {
    report: data,
    isLoading,
    isError: error,
    mutate,
  }
}
