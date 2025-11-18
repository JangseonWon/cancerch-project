import { apiClient } from './client'
import type {
  ReportResponse,
  PagedReportResponse,
  CreateReportRequest,
  PublishReportRequest,
} from '../types/report'

export const reportApi = {
  // Get all reports with pagination
  getReportList: async (
    page: number = 0,
    size: number = 20,
    status?: string
  ): Promise<PagedReportResponse> => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    })
    if (status) {
      params.append('status', status)
    }
    const response = await apiClient.get<PagedReportResponse>(
      `/api/reports?${params}`
    )
    return response.data
  },

  // Get single report by ID
  getReport: async (id: number): Promise<ReportResponse> => {
    const response = await apiClient.get<ReportResponse>(`/api/reports/${id}`)
    return response.data
  },

  // Create new report
  createReport: async (request: CreateReportRequest): Promise<ReportResponse> => {
    const response = await apiClient.post<ReportResponse>('/api/reports', request)
    return response.data
  },

  // Publish report
  publishReport: async (
    id: number,
    request: PublishReportRequest
  ): Promise<ReportResponse> => {
    const response = await apiClient.post<ReportResponse>(
      `/api/reports/${id}/publish`,
      request
    )
    return response.data
  },
}
