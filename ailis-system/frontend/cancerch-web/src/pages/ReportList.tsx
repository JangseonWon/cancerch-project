import { useState } from 'react'
import { DataTable } from 'primereact/datatable'
import { Column } from 'primereact/column'
import { Button } from 'primereact/button'
import { Tag } from 'primereact/tag'
import { Dropdown } from 'primereact/dropdown'
import { Paginator } from 'primereact/paginator'
import { Dialog } from 'primereact/dialog'
import { InputText } from 'primereact/inputtext'
import { useReportList } from '../hooks/useReports'
import { reportApi } from '../api/reportApi'
import type { ReportResponse, ReportStatus } from '../types/report'
import { ReportStatusLabels, ReportStatusColors } from '../types/report'
import { format } from 'date-fns'

export function ReportList() {
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(20)
  const [statusFilter, setStatusFilter] = useState<ReportStatus | ''>('')
  const [createDialog, setCreateDialog] = useState(false)
  const [sampleId, setSampleId] = useState('')
  const [reportType, setReportType] = useState('STANDARD')

  const { reportList, isLoading, isError, mutate } = useReportList(
    page,
    size,
    statusFilter || undefined
  )

  const statusOptions = [
    { label: '전체', value: '' },
    { label: '초안', value: 'DRAFT' },
    { label: '생성됨', value: 'GENERATED' },
    { label: '발행됨', value: 'PUBLISHED' },
    { label: '실패', value: 'FAILED' },
  ]

  const statusBodyTemplate = (rowData: ReportResponse) => {
    const status = rowData.status
    return (
      <Tag
        value={ReportStatusLabels[status]}
        severity={ReportStatusColors[status] as any}
      />
    )
  }

  const dateBodyTemplate = (rowData: ReportResponse) => {
    return format(new Date(rowData.createdAt), 'yyyy-MM-dd HH:mm')
  }

  const publishedDateBodyTemplate = (rowData: ReportResponse) => {
    return rowData.publishedAt
      ? format(new Date(rowData.publishedAt), 'yyyy-MM-dd HH:mm')
      : '-'
  }

  const actionBodyTemplate = (rowData: ReportResponse) => {
    const canPublish = rowData.status === 'GENERATED'

    return (
      <Button
        label="발행"
        icon="pi pi-send"
        size="small"
        severity="success"
        disabled={!canPublish}
        onClick={() => handlePublish(rowData.id)}
      />
    )
  }

  const handlePublish = async (id: number) => {
    try {
      await reportApi.publishReport(id, { publishedBy: 'current_user' })
      mutate()
    } catch (error) {
      console.error('Failed to publish report:', error)
    }
  }

  const handleCreateReport = async () => {
    try {
      await reportApi.createReport({
        sampleId,
        reportType,
        createdBy: 'current_user',
      })
      mutate()
      setCreateDialog(false)
      setSampleId('')
      setReportType('STANDARD')
    } catch (error) {
      console.error('Failed to create report:', error)
    }
  }

  const onPageChange = (event: any) => {
    setPage(event.page)
    setSize(event.rows)
  }

  if (isError) {
    return (
      <div className="card">
        <p style={{ color: 'red' }}>
          오류가 발생했습니다. 나중에 다시 시도해주세요.
        </p>
      </div>
    )
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h1 className="page-title">Report 관리</h1>
        <Button
          label="보고서 생성"
          icon="pi pi-plus"
          onClick={() => setCreateDialog(true)}
        />
      </div>

      <div className="card">
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="status-filter" style={{ marginRight: '0.5rem' }}>
            상태 필터:
          </label>
          <Dropdown
            id="status-filter"
            value={statusFilter}
            options={statusOptions}
            onChange={(e) => setStatusFilter(e.value)}
            placeholder="상태 선택"
          />
        </div>

        <DataTable
          value={reportList?.items || []}
          loading={isLoading}
          emptyMessage="보고서가 없습니다."
          stripedRows
        >
          <Column field="id" header="ID" style={{ width: '80px' }} />
          <Column field="sampleId" header="샘플 ID" />
          <Column field="serviceCode" header="서비스 코드" />
          <Column field="reportType" header="보고서 타입" />
          <Column
            field="status"
            header="상태"
            body={statusBodyTemplate}
            style={{ width: '120px' }}
          />
          <Column
            field="createdAt"
            header="생성일시"
            body={dateBodyTemplate}
            style={{ width: '180px' }}
          />
          <Column
            field="publishedAt"
            header="발행일시"
            body={publishedDateBodyTemplate}
            style={{ width: '180px' }}
          />
          <Column field="publishedBy" header="발행자" style={{ width: '120px' }} />
          <Column
            header="액션"
            body={actionBodyTemplate}
            style={{ width: '120px' }}
          />
        </DataTable>

        {reportList && (
          <Paginator
            first={page * size}
            rows={size}
            totalRecords={reportList.totalCount}
            rowsPerPageOptions={[10, 20, 50]}
            onPageChange={onPageChange}
          />
        )}
      </div>

      <Dialog
        header="보고서 생성"
        visible={createDialog}
        style={{ width: '500px' }}
        onHide={() => setCreateDialog(false)}
      >
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div>
            <label htmlFor="sample-id" style={{ display: 'block', marginBottom: '0.5rem' }}>
              샘플 ID:
            </label>
            <InputText
              id="sample-id"
              value={sampleId}
              onChange={(e) => setSampleId(e.target.value)}
              style={{ width: '100%' }}
            />
          </div>
          <div>
            <label htmlFor="report-type" style={{ display: 'block', marginBottom: '0.5rem' }}>
              보고서 타입:
            </label>
            <Dropdown
              id="report-type"
              value={reportType}
              options={[
                { label: 'Standard', value: 'STANDARD' },
                { label: 'Detailed', value: 'DETAILED' },
                { label: 'Summary', value: 'SUMMARY' },
              ]}
              onChange={(e) => setReportType(e.value)}
              style={{ width: '100%' }}
            />
          </div>
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.5rem' }}>
            <Button
              label="취소"
              severity="secondary"
              onClick={() => setCreateDialog(false)}
            />
            <Button
              label="생성"
              onClick={handleCreateReport}
              disabled={!sampleId}
            />
          </div>
        </div>
      </Dialog>
    </div>
  )
}
