-- Worklist 테이블
CREATE TABLE worklist (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    batch_prefix VARCHAR(50) NOT NULL,
    batch_index INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT chk_batch_index CHECK (batch_index >= 0),
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
);

-- Work Sample 테이블
CREATE TABLE work_sample (
    id BIGSERIAL PRIMARY KEY,
    worklist_id BIGINT NOT NULL,
    sample_id VARCHAR(100) NOT NULL,
    barcode VARCHAR(100) NOT NULL,
    service_code VARCHAR(50) NOT NULL,
    row_number INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_worklist FOREIGN KEY (worklist_id) REFERENCES worklist(id) ON DELETE CASCADE,
    CONSTRAINT chk_row_number CHECK (row_number >= 0 AND row_number < 96),
    CONSTRAINT uq_worklist_sample UNIQUE (worklist_id, sample_id)
);

-- Indexes for performance
CREATE INDEX idx_worklist_status ON worklist(status);
CREATE INDEX idx_worklist_batch ON worklist(batch_prefix, batch_index);
CREATE INDEX idx_worklist_created_at ON worklist(created_at DESC);
CREATE INDEX idx_work_sample_worklist_id ON work_sample(worklist_id);
CREATE INDEX idx_work_sample_barcode ON work_sample(barcode);
CREATE INDEX idx_work_sample_service_code ON work_sample(service_code);

-- Comments
COMMENT ON TABLE worklist IS 'Worklist 엔터티 - 검사 작업 목록';
COMMENT ON COLUMN worklist.uuid IS '글로벌 고유 식별자';
COMMENT ON COLUMN worklist.batch_prefix IS '배치 접두사 (예: WL)';
COMMENT ON COLUMN worklist.batch_index IS '배치 인덱스 (예: 20250117001)';
COMMENT ON COLUMN worklist.version IS '낙관적 잠금을 위한 버전 필드';

COMMENT ON TABLE work_sample IS 'Work Sample - Worklist에 포함된 검체';
COMMENT ON COLUMN work_sample.row_number IS 'Worklist 내에서의 행 번호 (0-95)';
