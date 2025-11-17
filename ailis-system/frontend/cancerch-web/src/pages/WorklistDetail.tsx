import { useParams, useNavigate } from 'react-router-dom'
import { Button } from 'primereact/button'
import { Tag } from 'primereact/tag'
import { DataTable } from 'primereact/datatable'
import { Column } from 'primereact/column'
import { Panel } from 'primereact/panel'
import { useWorklist } from '../hooks/useWorklists'
import { WorklistStatusLabels, WorklistStatusColors } from '../types/worklist'
import { format } from 'date-fns'

export function WorklistDetail() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { worklist, isLoading, isError } = useWorklist(id ? parseInt(id) : null)

  if (isLoading) {
    return (
      <div className="card">
        <p>로딩중...</p>
      </div>
    )
  }

  if (isError || !worklist) {
    return (
      <div className="card">
        <p style={{ color: 'red' }}>Worklist를 찾을 수 없습니다.</p>
        <Button
          label="목록으로 돌아가기"
          icon="pi pi-arrow-left"
          onClick={() => navigate('/')}
        />
      </div>
    )
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h1 className="page-title">Worklist 상세</h1>
        <Button
          label="목록으로"
          icon="pi pi-arrow-left"
          outlined
          onClick={() => navigate('/')}
        />
      </div>

      <Panel header="기본 정보" className="card" style={{ marginBottom: '1.5rem' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '1rem' }}>
          <div>
            <strong>ID:</strong> {worklist.id}
          </div>
          <div>
            <strong>UUID:</strong> {worklist.uuid}
          </div>
          <div>
            <strong>이름:</strong> {worklist.name}
          </div>
          <div>
            <strong>배치 번호:</strong> {worklist.batchNumber}
          </div>
          <div>
            <strong>상태:</strong>{' '}
            <Tag
              value={WorklistStatusLabels[worklist.status]}
              severity={WorklistStatusColors[worklist.status] as any}
            />
          </div>
          <div>
            <strong>검체 수:</strong> {worklist.sampleCount}
          </div>
          <div>
            <strong>생성일시:</strong>{' '}
            {format(new Date(worklist.createdAt), 'yyyy-MM-dd HH:mm:ss')}
          </div>
          <div>
            <strong>생성자:</strong> {worklist.createdBy}
          </div>
          <div>
            <strong>수정일시:</strong>{' '}
            {format(new Date(worklist.updatedAt), 'yyyy-MM-dd HH:mm:ss')}
          </div>
          <div>
            <strong>수정자:</strong> {worklist.updatedBy}
          </div>
          <div>
            <strong>버전:</strong> {worklist.version}
          </div>
        </div>
      </Panel>

      <Panel header="검체 목록" className="card">
        {worklist.samples.length === 0 ? (
          <p>검체가 없습니다.</p>
        ) : (
          <DataTable value={worklist.samples} stripedRows>
            <Column
              field="rowNumber"
              header="행 번호"
              style={{ width: '100px' }}
            />
            <Column field="sampleId" header="검체 ID" />
            <Column field="barcode" header="바코드" />
            <Column field="serviceCode" header="서비스 코드" />
          </DataTable>
        )}
      </Panel>
    </div>
  )
}
