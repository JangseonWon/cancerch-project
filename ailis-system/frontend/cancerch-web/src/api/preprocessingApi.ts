import { apiClient } from './client'
import type {
  PreprocessingResponse,
  PagedPreprocessingResponse,
  CreatePreprocessingRequest,
  UpdatePreprocessingRequest,
  StartPreprocessingRequest,
  CompletePreprocessingRequest,
} from '../types/preprocessing'

export const preprocessingApi = {
  // Get all preprocessing items with pagination and filtering
  getPreprocessings: async (
    page: number = 0,
    size: number = 20,
    status?: string
  ): Promise<PagedPreprocessingResponse> => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    })
    if (status) {
      params.append('status', status)
    }
    const response = await apiClient.get<PagedPreprocessingResponse>(
      `/api/preprocessing?${params}`
    )
    return response.data
  },

  // Get single preprocessing by ID
  getPreprocessing: async (id: number): Promise<PreprocessingResponse> => {
    const response = await apiClient.get<PreprocessingResponse>(`/api/preprocessing/${id}`)
    return response.data
  },

  // Create new preprocessing
  createPreprocessing: async (
    request: CreatePreprocessingRequest
  ): Promise<PreprocessingResponse> => {
    const response = await apiClient.post<PreprocessingResponse>(
      '/api/preprocessing',
      request
    )
    return response.data
  },

  // Update preprocessing
  updatePreprocessing: async (
    worklistId: number,
    request: UpdatePreprocessingRequest
  ): Promise<PreprocessingResponse> => {
    const response = await apiClient.patch<PreprocessingResponse>(
      `/api/preprocessing/${worklistId}`,
      request
    )
    return response.data
  },

  // Start Process A
  startProcessA: async (
    worklistId: number,
    request?: StartPreprocessingRequest
  ): Promise<PreprocessingResponse> => {
    const response = await apiClient.post<PreprocessingResponse>(
      `/api/preprocessing/${worklistId}/start`,
      request || {}
    )
    return response.data
  },

  // Complete Process A
  completeProcessA: async (
    worklistId: number,
    request?: CompletePreprocessingRequest
  ): Promise<PreprocessingResponse> => {
    const response = await apiClient.post<PreprocessingResponse>(
      `/api/preprocessing/${worklistId}/complete`,
      request || {}
    )
    return response.data
  },

  // Hold Process A
  holdProcessA: async (
    worklistId: number,
    request?: StartPreprocessingRequest
  ): Promise<PreprocessingResponse> => {
    const response = await apiClient.post<PreprocessingResponse>(
      `/api/preprocessing/worklists/${worklistId}/hold-a`,
      request || {}
    )
    return response.data
  },

  // Start Process B
  startProcessB: async (
    worklistId: number,
    request?: StartPreprocessingRequest
  ): Promise<PreprocessingResponse> => {
    const response = await apiClient.post<PreprocessingResponse>(
      `/api/preprocessing/worklists/${worklistId}/start-b`,
      request || {}
    )
    return response.data
  },

  // Complete Process B
  completeProcessB: async (
    worklistId: number,
    request?: CompletePreprocessingRequest
  ): Promise<PreprocessingResponse> => {
    const response = await apiClient.post<PreprocessingResponse>(
      `/api/preprocessing/worklists/${worklistId}/complete-b`,
      request || {}
    )
    return response.data
  },

  // Hold Process B
  holdProcessB: async (
    worklistId: number,
    request?: StartPreprocessingRequest
  ): Promise<PreprocessingResponse> => {
    const response = await apiClient.post<PreprocessingResponse>(
      `/api/preprocessing/worklists/${worklistId}/hold-b`,
      request || {}
    )
    return response.data
  },
}
