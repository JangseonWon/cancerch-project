import { apiClient } from './client'
import type {
  AnalysisResultResponse,
  PagedAnalysisResponse,
  UpdateAnalysisRequest,
} from '../types/analysis'

export const analysisApi = {
  // Get all analysis results with pagination and filtering
  getAnalysisList: async (
    page: number = 0,
    size: number = 20,
    search?: string
  ): Promise<PagedAnalysisResponse> => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    })
    if (search) {
      params.append('search', search)
    }
    const response = await apiClient.get<PagedAnalysisResponse>(
      `/api/analysis-results?${params}`
    )
    return response.data
  },

  // Get single analysis result by ID
  getAnalysis: async (id: number): Promise<AnalysisResultResponse> => {
    const response = await apiClient.get<AnalysisResultResponse>(
      `/api/analysis-results/${id}`
    )
    return response.data
  },

  // Update analysis result
  updateAnalysis: async (
    id: number,
    request: UpdateAnalysisRequest
  ): Promise<AnalysisResultResponse> => {
    const response = await apiClient.patch<AnalysisResultResponse>(
      `/api/analysis-results/${id}`,
      request
    )
    return response.data
  },
}
