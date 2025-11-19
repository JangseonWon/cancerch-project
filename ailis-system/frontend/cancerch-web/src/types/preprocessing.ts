export interface PreprocessingResponse {
  id: number
  uuid: string
  worklistId: number
  worklistName: string
  batchNumber: string
  index: number
  sequencingBatch: string
  state: PreprocessingState
  status: PreprocessingState
  createdAt: string
  updatedAt: string
  version: number
  startedBy?: string
  startedAt?: string
  completedBy?: string
  completedAt?: string
}

export interface PagedPreprocessingResponse {
  items: PreprocessingResponse[]
  totalCount: number
  page: number
  size: number
  totalPages: number
}

export interface CreatePreprocessingRequest {
  worklistId: number
  index: number
  sequencingBatch: string
}

export interface UpdatePreprocessingRequest {
  index?: number
  sequencingBatch?: string
}

export interface StartPreprocessingRequest {
  startedBy?: string
}

export interface CompletePreprocessingRequest {
  completedBy?: string
}

export type PreprocessingState = 'PENDING' | 'HOLDING' | 'PENDING_B' | 'HOLDING_B' | 'COMPLETE'

export const PreprocessingStateLabels: Record<PreprocessingState, string> = {
  PENDING: 'Process A 진행중',
  HOLDING: 'Process A 보류',
  PENDING_B: 'Process B 진행중',
  HOLDING_B: 'Process B 보류',
  COMPLETE: '완료',
}

export const PreprocessingStateColors: Record<PreprocessingState, string> = {
  PENDING: 'warning',
  HOLDING: 'secondary',
  PENDING_B: 'info',
  HOLDING_B: 'secondary',
  COMPLETE: 'success',
}
