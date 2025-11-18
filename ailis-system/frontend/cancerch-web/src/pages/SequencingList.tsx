import { useState } from 'react'
import { DataTable } from 'primereact/datatable'
import { Column } from 'primereact/column'
import { Button } from 'primereact/button'
import { Tag } from 'primereact/tag'
import { Dropdown } from 'primereact/dropdown'
import { Paginator } from 'primereact/paginator'
import { useSequencingList } from '../hooks/useSequencing'
import { sequencingApi } from '../api/sequencingApi'
import type { SequencingResponse, PreprocessingStatus } from '../types/sequencing'
import { PreprocessingStatusLabels, PreprocessingStatusColors } from '../types/sequencing'
import { format } from 'date-fns'

export function SequencingList() {
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(20)
  const [statusFilter, setStatusFilter] = useState<PreprocessingStatus | ''>('')

  const { sequencingList, isLoading, isError, mutate } = useSequencingList(
    page,
    size,
    statusFilter || undefined
  )

  const statusOptions = [
    { label: '전체', value: '' },
    { label: '대기', value: 'PENDING' },
    { label: '보류', value: 'HOLDING' },
    { label: '진행중', value: 'IN_PROGRESS' },
    { label: '완료', value: 'COMPLETE' },
    { label: '실패', value: 'FAILED' },
  ]

  const statusBodyTemplate = (rowData: SequencingResponse) => {
    const status = rowData.status
    return (
      <Tag
        value={PreprocessingStatusLabels[status]}
        severity={PreprocessingStatusColors[status] as any}
      />
    )
  }

  const dateBodyTemplate = (rowData: SequencingResponse) => {
    return format(new Date(rowData.createdAt), 'yyyy-MM-dd HH:mm')
  }

  const actionBodyTemplate = (rowData: SequencingResponse) => {
    const canStart = rowData.status === 'PENDING' || rowData.status === 'HOLDING'
    const canComplete = rowData.status === 'IN_PROGRESS'

    return (
      <div style={{ display: 'flex', gap: '0.5rem' }}>
        <Button
          label="Start A"
          icon="pi pi-play"
          size="small"
          disabled={!canStart}
          onClick={() => handleStart(rowData.id)}
        />
        <Button
          label="Complete A"
          icon="pi pi-check"
          size="small"
          severity="success"
          disabled={!canComplete}
          onClick={() => handleComplete(rowData.id)}
        />
      </div>
    )
  }

  const handleStart = async (id: number) => {
    try {
      await sequencingApi.startSequencing(id, 'current_user')
      mutate()
    } catch (error) {
      console.error('Failed to start sequencing:', error)
    }
  }

  const handleComplete = async (id: number) => {
    try {
      await sequencingApi.completeSequencing(id, 'current_user')
      mutate()
    } catch (error) {
      console.error('Failed to complete sequencing:', error)
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
        <h1 className="page-title">Preprocessing 관리</h1>
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
          value={sequencingList?.items || []}
          loading={isLoading}
          emptyMessage="Preprocessing 항목이 없습니다."
          stripedRows
        >
          <Column field="id" header="ID" style={{ width: '80px' }} />
          <Column field="worklistName" header="Worklist" />
          <Column field="batchNumber" header="배치 번호" />
          <Column
            field="status"
            header="상태"
            body={statusBodyTemplate}
            style={{ width: '120px' }}
          />
          <Column field="startedBy" header="시작자" style={{ width: '120px' }} />
          <Column field="completedBy" header="완료자" style={{ width: '120px' }} />
          <Column
            field="createdAt"
            header="생성일시"
            body={dateBodyTemplate}
            style={{ width: '180px' }}
          />
          <Column
            header="액션"
            body={actionBodyTemplate}
            style={{ width: '250px' }}
          />
        </DataTable>

        {sequencingList && (
          <Paginator
            first={page * size}
            rows={size}
            totalRecords={sequencingList.totalCount}
            rowsPerPageOptions={[10, 20, 50]}
            onPageChange={onPageChange}
          />
        )}
      </div>
    </div>
  )
}
