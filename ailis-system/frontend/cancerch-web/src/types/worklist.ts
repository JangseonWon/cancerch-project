export interface WorkSampleResponse {
  sampleId: string
  barcode: string
  serviceCode: string
  rowNumber: number
}

export interface WorklistResponse {
  id: number
  uuid: string
  name: string
  batchNumber: string
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED'
  sampleCount: number
  samples: WorkSampleResponse[]
  createdAt: string
  createdBy: string
  updatedAt: string
  updatedBy: string
  version: number
}

export interface PagedWorklistResponse {
  items: WorklistResponse[]
  totalCount: number
  page: number
  size: number
  totalPages: number
}

export interface CreateWorklistRequest {
  name: string
  batchPrefix: string
  batchIndex: number
  createdBy: string
}

export interface UpdateWorklistRequest {
  name: string
  updatedBy: string
}

export type WorklistStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED'

export const WorklistStatusLabels: Record<WorklistStatus, string> = {
  PENDING: '대기',
  IN_PROGRESS: '진행중',
  COMPLETED: '완료',
  CANCELLED: '취소',
}

export const WorklistStatusColors: Record<WorklistStatus, string> = {
  PENDING: 'info',
  IN_PROGRESS: 'warning',
  COMPLETED: 'success',
  CANCELLED: 'danger',
}
