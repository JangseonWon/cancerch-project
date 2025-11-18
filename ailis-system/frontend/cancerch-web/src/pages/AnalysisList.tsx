import { useState } from 'react'
import { DataTable } from 'primereact/datatable'
import { Column } from 'primereact/column'
import { Button } from 'primereact/button'
import { Tag } from 'primereact/tag'
import { InputText } from 'primereact/inputtext'
import { Paginator } from 'primereact/paginator'
import { Dialog } from 'primereact/dialog'
import { useAnalysisList } from '../hooks/useAnalysis'
import { analysisApi } from '../api/analysisApi'
import type { AnalysisResultResponse } from '../types/analysis'
import { AnalysisStatusLabels, AnalysisStatusColors } from '../types/analysis'
import { format } from 'date-fns'

export function AnalysisList() {
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(20)
  const [search, setSearch] = useState('')
  const [searchInput, setSearchInput] = useState('')
  const [editDialog, setEditDialog] = useState(false)
  const [selectedAnalysis, setSelectedAnalysis] = useState<AnalysisResultResponse | null>(null)
  const [editResult, setEditResult] = useState('')
  const [editComment, setEditComment] = useState('')

  const { analysisList, isLoading, isError, mutate } = useAnalysisList(
    page,
    size,
    search
  )

  const statusBodyTemplate = (rowData: AnalysisResultResponse) => {
    const status = rowData.status
    return (
      <Tag
        value={AnalysisStatusLabels[status]}
        severity={AnalysisStatusColors[status] as any}
      />
    )
  }

  const dateBodyTemplate = (rowData: AnalysisResultResponse) => {
    return format(new Date(rowData.createdAt), 'yyyy-MM-dd HH:mm')
  }

  const actionBodyTemplate = (rowData: AnalysisResultResponse) => {
    return (
      <Button
        icon="pi pi-pencil"
        rounded
        text
        severity="info"
        onClick={() => handleEdit(rowData)}
      />
    )
  }

  const handleSearch = () => {
    setSearch(searchInput)
    setPage(0)
  }

  const handleEdit = (analysis: AnalysisResultResponse) => {
    setSelectedAnalysis(analysis)
    setEditResult(analysis.result || '')
    setEditComment(analysis.comment || '')
    setEditDialog(true)
  }

  const handleSaveEdit = async () => {
    if (!selectedAnalysis) return

    try {
      await analysisApi.updateAnalysis(selectedAnalysis.id, {
        result: editResult,
        comment: editComment,
        updatedBy: 'current_user',
      })
      mutate()
      setEditDialog(false)
      setSelectedAnalysis(null)
    } catch (error) {
      console.error('Failed to update analysis:', error)
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
        <h1 className="page-title">Analysis Result 관리</h1>
      </div>

      <div className="card">
        <div style={{ marginBottom: '1rem', display: 'flex', gap: '0.5rem' }}>
          <InputText
            placeholder="sampleId 또는 serviceCode 검색"
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            style={{ width: '300px' }}
          />
          <Button
            label="검색"
            icon="pi pi-search"
            onClick={handleSearch}
          />
          {search && (
            <Button
              label="초기화"
              icon="pi pi-times"
              severity="secondary"
              onClick={() => {
                setSearch('')
                setSearchInput('')
                setPage(0)
              }}
            />
          )}
        </div>

        <DataTable
          value={analysisList?.items || []}
          loading={isLoading}
          emptyMessage="분석 결과가 없습니다."
          stripedRows
        >
          <Column field="id" header="ID" style={{ width: '80px' }} />
          <Column field="sampleId" header="샘플 ID" />
          <Column field="serviceCode" header="서비스 코드" />
          <Column field="result" header="결과" />
          <Column field="comment" header="코멘트" />
          <Column
            field="status"
            header="상태"
            body={statusBodyTemplate}
            style={{ width: '120px' }}
          />
          <Column field="analyzedBy" header="분석자" style={{ width: '120px' }} />
          <Column
            field="createdAt"
            header="생성일시"
            body={dateBodyTemplate}
            style={{ width: '180px' }}
          />
          <Column
            header="액션"
            body={actionBodyTemplate}
            style={{ width: '100px' }}
          />
        </DataTable>

        {analysisList && (
          <Paginator
            first={page * size}
            rows={size}
            totalRecords={analysisList.totalCount}
            rowsPerPageOptions={[10, 20, 50]}
            onPageChange={onPageChange}
          />
        )}
      </div>

      <Dialog
        header="분석 결과 편집"
        visible={editDialog}
        style={{ width: '500px' }}
        onHide={() => setEditDialog(false)}
      >
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div>
            <label htmlFor="edit-result" style={{ display: 'block', marginBottom: '0.5rem' }}>
              결과:
            </label>
            <InputText
              id="edit-result"
              value={editResult}
              onChange={(e) => setEditResult(e.target.value)}
              style={{ width: '100%' }}
            />
          </div>
          <div>
            <label htmlFor="edit-comment" style={{ display: 'block', marginBottom: '0.5rem' }}>
              코멘트:
            </label>
            <InputText
              id="edit-comment"
              value={editComment}
              onChange={(e) => setEditComment(e.target.value)}
              style={{ width: '100%' }}
            />
          </div>
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.5rem' }}>
            <Button
              label="취소"
              severity="secondary"
              onClick={() => setEditDialog(false)}
            />
            <Button
              label="저장"
              onClick={handleSaveEdit}
            />
          </div>
        </div>
      </Dialog>
    </div>
  )
}
