export interface SequencingResponse {
  id: number
  worklistId: number
  worklistName: string
  batchNumber: string
  status: PreprocessingStatus
  startedAt?: string
  completedAt?: string
  startedBy?: string
  completedBy?: string
  createdAt: string
  updatedAt: string
  version: number
}

export interface PagedSequencingResponse {
  items: SequencingResponse[]
  totalCount: number
  page: number
  size: number
  totalPages: number
}

export interface UpdateSequencingRequest {
  status: PreprocessingStatus
  updatedBy: string
}

export type PreprocessingStatus = 'PENDING' | 'HOLDING' | 'IN_PROGRESS' | 'COMPLETE' | 'FAILED'

export const PreprocessingStatusLabels: Record<PreprocessingStatus, string> = {
  PENDING: '대기',
  HOLDING: '보류',
  IN_PROGRESS: '진행중',
  COMPLETE: '완료',
  FAILED: '실패',
}

export const PreprocessingStatusColors: Record<PreprocessingStatus, string> = {
  PENDING: 'info',
  HOLDING: 'warning',
  IN_PROGRESS: 'primary',
  COMPLETE: 'success',
  FAILED: 'danger',
}
