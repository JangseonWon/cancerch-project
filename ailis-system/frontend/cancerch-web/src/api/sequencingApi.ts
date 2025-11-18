import { apiClient } from './client'
import type {
  SequencingResponse,
  PagedSequencingResponse,
  UpdateSequencingRequest,
} from '../types/sequencing'

export const sequencingApi = {
  // Get all preprocessing items with pagination and filtering
  getSequencingList: async (
    page: number = 0,
    size: number = 20,
    status?: string
  ): Promise<PagedSequencingResponse> => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    })
    if (status) {
      params.append('status', status)
    }
    const response = await apiClient.get<PagedSequencingResponse>(
      `/api/preprocessing?${params}`
    )
    return response.data
  },

  // Get single preprocessing item by ID
  getSequencing: async (id: number): Promise<SequencingResponse> => {
    const response = await apiClient.get<SequencingResponse>(`/api/preprocessing/${id}`)
    return response.data
  },

  // Start preprocessing (Start A)
  startSequencing: async (
    id: number,
    startedBy: string
  ): Promise<SequencingResponse> => {
    const response = await apiClient.post<SequencingResponse>(
      `/api/preprocessing/${id}/start`,
      { startedBy }
    )
    return response.data
  },

  // Complete preprocessing (Complete A)
  completeSequencing: async (
    id: number,
    completedBy: string
  ): Promise<SequencingResponse> => {
    const response = await apiClient.post<SequencingResponse>(
      `/api/preprocessing/${id}/complete`,
      { completedBy }
    )
    return response.data
  },

  // Update preprocessing status
  updateSequencing: async (
    id: number,
    request: UpdateSequencingRequest
  ): Promise<SequencingResponse> => {
    const response = await apiClient.patch<SequencingResponse>(
      `/api/preprocessing/${id}`,
      request
    )
    return response.data
  },
}
