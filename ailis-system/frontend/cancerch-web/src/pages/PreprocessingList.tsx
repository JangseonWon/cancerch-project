import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { DataTable } from 'primereact/datatable'
import { Column } from 'primereact/column'
import { Button } from 'primereact/button'
import { Tag } from 'primereact/tag'
import { Dropdown } from 'primereact/dropdown'
import { Paginator } from 'primereact/paginator'
import { usePreprocessings } from '../hooks/usePreprocessing'
import { preprocessingApi } from '../api/preprocessingApi'
import type {
  PreprocessingResponse,
  PreprocessingState,
} from '../types/preprocessing'
import {
  PreprocessingStateLabels,
  PreprocessingStateColors,
} from '../types/preprocessing'
import { format } from 'date-fns'

export function PreprocessingList() {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(20)
  const [statusFilter, setStatusFilter] = useState<PreprocessingState | ''>('')

  const { preprocessings, isLoading, isError, mutate } = usePreprocessings(
    page,
    size,
    statusFilter || undefined
  )

  const statusOptions = [
    { label: '전체', value: '' },
    { label: 'Process A 진행중', value: 'PENDING' },
    { label: 'Process A 보류', value: 'HOLDING' },
    { label: 'Process B 진행중', value: 'PENDING_B' },
    { label: 'Process B 보류', value: 'HOLDING_B' },
    { label: '완료', value: 'COMPLETE' },
  ]

  const statusBodyTemplate = (rowData: PreprocessingResponse) => {
    const state = rowData.state
    return (
      <Tag
        value={PreprocessingStateLabels[state]}
        severity={PreprocessingStateColors[state] as any}
      />
    )
  }

  const dateBodyTemplate = (rowData: PreprocessingResponse) => {
    return format(new Date(rowData.createdAt), 'yyyy-MM-dd HH:mm')
  }

  const handleCompleteProcessA = async (worklistId: number) => {
    try {
      await preprocessingApi.completeProcessA(worklistId, {
        completedBy: 'current-user', // TODO: Get from auth context
      })
      mutate() // Refresh data
    } catch (error) {
      console.error('Failed to complete Process A:', error)
      alert('Process A 완료 실패')
    }
  }

  const handleHoldProcessA = async (worklistId: number) => {
    try {
      await preprocessingApi.holdProcessA(worklistId, {
        startedBy: 'current-user',
      })
      mutate()
    } catch (error) {
      console.error('Failed to hold Process A:', error)
      alert('Process A 보류 실패')
    }
  }

  const handleStartProcessA = async (worklistId: number) => {
    try {
      await preprocessingApi.startProcessA(worklistId, {
        startedBy: 'current-user',
      })
      mutate()
    } catch (error) {
      console.error('Failed to start Process A:', error)
      alert('Process A 시작 실패')
    }
  }

  const handleCompleteProcessB = async (worklistId: number) => {
    try {
      await preprocessingApi.completeProcessB(worklistId, {
        completedBy: 'current-user',
      })
      mutate()
    } catch (error) {
      console.error('Failed to complete Process B:', error)
      alert('Process B 완료 실패')
    }
  }

  const handleHoldProcessB = async (worklistId: number) => {
    try {
      await preprocessingApi.holdProcessB(worklistId, {
        startedBy: 'current-user',
      })
      mutate()
    } catch (error) {
      console.error('Failed to hold Process B:', error)
      alert('Process B 보류 실패')
    }
  }

  const handleStartProcessB = async (worklistId: number) => {
    try {
      await preprocessingApi.startProcessB(worklistId, {
        startedBy: 'current-user',
      })
      mutate()
    } catch (error) {
      console.error('Failed to start Process B:', error)
      alert('Process B 시작 실패')
    }
  }

  const actionBodyTemplate = (rowData: PreprocessingResponse) => {
    const state = rowData.state

    return (
      <div style={{ display: 'flex', gap: '0.5rem' }}>
        {state === 'PENDING' && (
          <>
            <Button
              label="완료 A"
              size="small"
              severity="success"
              onClick={() => handleCompleteProcessA(rowData.worklistId)}
            />
            <Button
              label="보류 A"
              size="small"
              severity="warning"
              onClick={() => handleHoldProcessA(rowData.worklistId)}
            />
          </>
        )}

        {state === 'HOLDING' && (
          <Button
            label="재개 A"
            size="small"
            severity="info"
            onClick={() => handleStartProcessA(rowData.worklistId)}
          />
        )}

        {state === 'PENDING_B' && (
          <>
            <Button
              label="완료 B"
              size="small"
              severity="success"
              onClick={() => handleCompleteProcessB(rowData.worklistId)}
            />
            <Button
              label="보류 B"
              size="small"
              severity="warning"
              onClick={() => handleHoldProcessB(rowData.worklistId)}
            />
          </>
        )}

        {state === 'HOLDING_B' && (
          <Button
            label="재개 B"
            size="small"
            severity="info"
            onClick={() => handleStartProcessB(rowData.worklistId)}
          />
        )}

        {state === 'COMPLETE' && (
          <Tag value="완료됨" severity="success" />
        )}
      </div>
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
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: '1.5rem',
        }}
      >
        <h1 className="page-title">전처리 관리</h1>
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
          value={preprocessings?.items || []}
          loading={isLoading}
          emptyMessage="전처리 항목이 없습니다."
          stripedRows
        >
          <Column field="id" header="ID" style={{ width: '80px' }} />
          <Column field="worklistName" header="Worklist" />
          <Column field="batchNumber" header="배치 번호" />
          <Column field="sequencingBatch" header="시퀀싱 배치" />
          <Column field="index" header="Index" style={{ width: '80px' }} />
          <Column
            field="state"
            header="상태"
            body={statusBodyTemplate}
            style={{ width: '150px' }}
          />
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

        {preprocessings && (
          <Paginator
            first={page * size}
            rows={size}
            totalRecords={preprocessings.totalCount}
            rowsPerPageOptions={[10, 20, 50]}
            onPageChange={onPageChange}
          />
        )}
      </div>
    </div>
  )
}
