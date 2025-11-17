import { apiClient } from './client'
import type {
  WorklistResponse,
  PagedWorklistResponse,
  CreateWorklistRequest,
  UpdateWorklistRequest,
} from '../types/worklist'

export const worklistApi = {
  // Get all worklists with pagination and filtering
  getWorklists: async (
    page: number = 0,
    size: number = 20,
    status?: string
  ): Promise<PagedWorklistResponse> => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    })
    if (status) {
      params.append('status', status)
    }
    const response = await apiClient.get<PagedWorklistResponse>(
      `/api/worklists?${params}`
    )
    return response.data
  },

  // Get single worklist by ID
  getWorklist: async (id: number): Promise<WorklistResponse> => {
    const response = await apiClient.get<WorklistResponse>(`/api/worklists/${id}`)
    return response.data
  },

  // Create new worklist
  createWorklist: async (
    request: CreateWorklistRequest
  ): Promise<WorklistResponse> => {
    const response = await apiClient.post<WorklistResponse>(
      '/api/worklists',
      request
    )
    return response.data
  },

  // Update existing worklist
  updateWorklist: async (
    id: number,
    request: UpdateWorklistRequest
  ): Promise<WorklistResponse> => {
    const response = await apiClient.patch<WorklistResponse>(
      `/api/worklists/${id}`,
      request
    )
    return response.data
  },
}
