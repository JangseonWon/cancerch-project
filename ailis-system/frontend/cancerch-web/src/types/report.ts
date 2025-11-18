export interface ReportResponse {
  id: number
  sampleId: string
  serviceCode: string
  reportType: string
  status: ReportStatus
  generatedAt?: string
  publishedAt?: string
  publishedBy?: string
  filePath?: string
  createdAt: string
  updatedAt: string
  version: number
}

export interface PagedReportResponse {
  items: ReportResponse[]
  totalCount: number
  page: number
  size: number
  totalPages: number
}

export interface CreateReportRequest {
  sampleId: string
  reportType: string
  createdBy: string
}

export interface PublishReportRequest {
  publishedBy: string
}

export type ReportStatus = 'DRAFT' | 'GENERATED' | 'PUBLISHED' | 'FAILED'

export const ReportStatusLabels: Record<ReportStatus, string> = {
  DRAFT: '초안',
  GENERATED: '생성됨',
  PUBLISHED: '발행됨',
  FAILED: '실패',
}

export const ReportStatusColors: Record<ReportStatus, string> = {
  DRAFT: 'secondary',
  GENERATED: 'info',
  PUBLISHED: 'success',
  FAILED: 'danger',
}
