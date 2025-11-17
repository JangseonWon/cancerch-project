import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from 'primereact/button'
import { InputText } from 'primereact/inputtext'
import { InputNumber } from 'primereact/inputnumber'
import { Panel } from 'primereact/panel'
import { Message } from 'primereact/message'
import { worklistApi } from '../api/worklistApi'
import type { CreateWorklistRequest } from '../types/worklist'

export function CreateWorklist() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const [formData, setFormData] = useState<CreateWorklistRequest>({
    name: '',
    batchPrefix: 'WL',
    batchIndex: 0,
    createdBy: 'admin',
  })

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    try {
      const result = await worklistApi.createWorklist(formData)
      navigate(`/worklists/${result.id}`)
    } catch (err: any) {
      setError(err.response?.data?.message || '생성 중 오류가 발생했습니다.')
      setLoading(false)
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h1 className="page-title">새 Worklist 생성</h1>
        <Button
          label="취소"
          icon="pi pi-times"
          outlined
          onClick={() => navigate('/')}
        />
      </div>

      <Panel header="Worklist 정보" className="card">
        <form onSubmit={handleSubmit}>
          {error && (
            <Message
              severity="error"
              text={error}
              style={{ marginBottom: '1rem', width: '100%' }}
            />
          )}

          <div style={{ marginBottom: '1rem' }}>
            <label htmlFor="name" style={{ display: 'block', marginBottom: '0.5rem' }}>
              <strong>Worklist 이름 *</strong>
            </label>
            <InputText
              id="name"
              value={formData.name}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
              required
              style={{ width: '100%' }}
              placeholder="예: Morning Batch 2025-01-17"
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '1rem', marginBottom: '1rem' }}>
            <div>
              <label htmlFor="batchPrefix" style={{ display: 'block', marginBottom: '0.5rem' }}>
                <strong>배치 접두사 *</strong>
              </label>
              <InputText
                id="batchPrefix"
                value={formData.batchPrefix}
                onChange={(e) =>
                  setFormData({ ...formData, batchPrefix: e.target.value })
                }
                required
                placeholder="예: WL"
              />
            </div>

            <div>
              <label htmlFor="batchIndex" style={{ display: 'block', marginBottom: '0.5rem' }}>
                <strong>배치 인덱스 *</strong>
              </label>
              <InputNumber
                id="batchIndex"
                value={formData.batchIndex}
                onValueChange={(e) =>
                  setFormData({ ...formData, batchIndex: e.value || 0 })
                }
                required
                min={0}
                useGrouping={false}
                placeholder="예: 20250117001"
              />
            </div>
          </div>

          <div style={{ marginBottom: '1rem' }}>
            <label htmlFor="createdBy" style={{ display: 'block', marginBottom: '0.5rem' }}>
              <strong>생성자 *</strong>
            </label>
            <InputText
              id="createdBy"
              value={formData.createdBy}
              onChange={(e) =>
                setFormData({ ...formData, createdBy: e.target.value })
              }
              required
              placeholder="예: admin"
            />
          </div>

          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
            <Button
              type="button"
              label="취소"
              severity="secondary"
              outlined
              onClick={() => navigate('/')}
            />
            <Button
              type="submit"
              label="생성"
              icon="pi pi-check"
              loading={loading}
            />
          </div>
        </form>
      </Panel>
    </div>
  )
}
