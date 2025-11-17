import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { DataTable } from 'primereact/datatable'
import { Column } from 'primereact/column'
import { Button } from 'primereact/button'
import { Tag } from 'primereact/tag'
import { Dropdown } from 'primereact/dropdown'
import { Paginator } from 'primereact/paginator'
import { useWorklists } from '../hooks/useWorklists'
import type { WorklistResponse, WorklistStatus } from '../types/worklist'
import { WorklistStatusLabels, WorklistStatusColors } from '../types/worklist'
import { format } from 'date-fns'

export function WorklistList() {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(20)
  const [statusFilter, setStatusFilter] = useState<WorklistStatus | ''>('')

  const { worklists, isLoading, isError } = useWorklists(
    page,
    size,
    statusFilter || undefined
  )

  const statusOptions = [
    { label: '전체', value: '' },
    { label: '대기', value: 'PENDING' },
    { label: '진행중', value: 'IN_PROGRESS' },
    { label: '완료', value: 'COMPLETED' },
    { label: '취소', value: 'CANCELLED' },
  ]

  const statusBodyTemplate = (rowData: WorklistResponse) => {
    const status = rowData.status
    return (
      <Tag
        value={WorklistStatusLabels[status]}
        severity={WorklistStatusColors[status] as any}
      />
    )
  }

  const dateBodyTemplate = (rowData: WorklistResponse) => {
    return format(new Date(rowData.createdAt), 'yyyy-MM-dd HH:mm')
  }

  const actionBodyTemplate = (rowData: WorklistResponse) => {
    return (
      <Button
        icon="pi pi-eye"
        rounded
        text
        severity="info"
        onClick={() => navigate(`/worklists/${rowData.id}`)}
      />
    )
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
        <h1 className="page-title">Worklist 관리</h1>
        <Button
          label="새 Worklist 생성"
          icon="pi pi-plus"
          onClick={() => navigate('/worklists/new')}
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
          value={worklists?.items || []}
          loading={isLoading}
          emptyMessage="Worklist가 없습니다."
          stripedRows
        >
          <Column field="id" header="ID" style={{ width: '80px' }} />
          <Column field="name" header="이름" />
          <Column field="batchNumber" header="배치 번호" />
          <Column
            field="status"
            header="상태"
            body={statusBodyTemplate}
            style={{ width: '120px' }}
          />
          <Column
            field="sampleCount"
            header="검체 수"
            style={{ width: '100px' }}
          />
          <Column
            field="createdAt"
            header="생성일시"
            body={dateBodyTemplate}
            style={{ width: '180px' }}
          />
          <Column field="createdBy" header="생성자" style={{ width: '120px' }} />
          <Column
            header="액션"
            body={actionBodyTemplate}
            style={{ width: '100px' }}
          />
        </DataTable>

        {worklists && (
          <Paginator
            first={page * size}
            rows={size}
            totalRecords={worklists.totalCount}
            rowsPerPageOptions={[10, 20, 50]}
            onPageChange={onPageChange}
          />
        )}
      </div>
    </div>
  )
}
