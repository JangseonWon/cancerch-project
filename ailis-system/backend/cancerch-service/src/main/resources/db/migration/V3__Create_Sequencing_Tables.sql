-- Preprocessing 테이블
CREATE TABLE preprocessing (
    worklist_id BIGINT NOT NULL,
    index INTEGER NOT NULL,
    sequencing_batch VARCHAR(100),
    state VARCHAR(50) NOT NULL,
    create_by VARCHAR(100) NOT NULL,
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modify_by VARCHAR(100),
    last_modify_at TIMESTAMP,
    PRIMARY KEY (worklist_id, index),
    CONSTRAINT fk_preprocessing_worklist FOREIGN KEY (worklist_id) REFERENCES worklist(id) ON DELETE CASCADE,
    CONSTRAINT chk_preprocessing_state CHECK (state IN ('PENDING', 'HOLDING', 'PENDING_B', 'HOLDING_B', 'COMPLETE'))
);

-- Sequencing 테이블
CREATE TABLE sequencing (
    id BIGSERIAL PRIMARY KEY,
    worklist_id BIGINT NOT NULL,
    index INTEGER NOT NULL,
    sample_id VARCHAR(100) NOT NULL,
    barcode VARCHAR(100) NOT NULL,
    index_name VARCHAR(50),
    qc VARCHAR(50),
    state VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sequencing_worklist FOREIGN KEY (worklist_id) REFERENCES worklist(id) ON DELETE CASCADE
);

-- Index 테이블 (96-well plate)
CREATE TABLE plate_index (
    plate VARCHAR(50) NOT NULL,
    well VARCHAR(10) NOT NULL,
    index_name VARCHAR(50) NOT NULL,
    sequence VARCHAR(100) NOT NULL,
    PRIMARY KEY (plate, well)
);

-- Indexes
CREATE INDEX idx_preprocessing_state ON preprocessing(state);
CREATE INDEX idx_sequencing_worklist ON sequencing(worklist_id);
CREATE INDEX idx_sequencing_sample ON sequencing(sample_id);

-- Comments
COMMENT ON TABLE preprocessing IS 'Preprocessing 상태 관리';
COMMENT ON TABLE sequencing IS 'Sequencing 배치 데이터';
COMMENT ON TABLE plate_index IS '96-well plate Index 정보';
