export interface AnalysisResultResponse {
  id: number
  sampleId: string
  serviceCode: string
  result?: string
  comment?: string
  status: AnalysisStatus
  analyzedAt?: string
  analyzedBy?: string
  createdAt: string
  updatedAt: string
  version: number
}

export interface PagedAnalysisResponse {
  items: AnalysisResultResponse[]
  totalCount: number
  page: number
  size: number
  totalPages: number
}

export interface UpdateAnalysisRequest {
  result?: string
  comment?: string
  updatedBy: string
}

export type AnalysisStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED'

export const AnalysisStatusLabels: Record<AnalysisStatus, string> = {
  PENDING: '대기',
  IN_PROGRESS: '분석중',
  COMPLETED: '완료',
  FAILED: '실패',
}

export const AnalysisStatusColors: Record<AnalysisStatus, string> = {
  PENDING: 'info',
  IN_PROGRESS: 'warning',
  COMPLETED: 'success',
  FAILED: 'danger',
}
