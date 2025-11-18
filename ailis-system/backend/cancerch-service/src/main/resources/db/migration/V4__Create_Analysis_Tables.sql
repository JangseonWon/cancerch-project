-- Analysis Result 테이블
CREATE TABLE analysis_result (
    id BIGSERIAL PRIMARY KEY,
    sample_id VARCHAR(100) NOT NULL,
    service_code VARCHAR(50) NOT NULL,
    batch VARCHAR(50) NOT NULL,
    row_number INTEGER NOT NULL,

    -- Analysis Results
    cad_ensemble_prob DECIMAL(10, 6),
    too5_pred VARCHAR(100),
    too6_pred VARCHAR(100),
    iscore DECIMAL(10, 6),
    result VARCHAR(50),
    comment TEXT,

    -- Coverage & Quality
    fems_cov_bc DECIMAL(10, 4),
    fems_bc DECIMAL(10, 4),
    cov_bc DECIMAL(10, 4),
    cfdna_concentration DECIMAL(10, 4),

    -- Metadata
    analyzed_at TIMESTAMP,
    analyzed_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_analysis_result UNIQUE (sample_id, service_code, batch, row_number)
);

-- Analysis QC 테이블
CREATE TABLE analysis_qc (
    id BIGSERIAL PRIMARY KEY,
    sample_id VARCHAR(100) NOT NULL,
    service_code VARCHAR(50) NOT NULL,
    batch VARCHAR(50) NOT NULL,
    row_number INTEGER NOT NULL,

    -- QC Metrics
    freemix DECIMAL(10, 6),
    raw_reads_millions DECIMAL(10, 2),
    dup_rate DECIMAL(10, 4),
    gc DECIMAL(10, 4),
    total_reads BIGINT,
    mean DECIMAL(10, 2),
    median DECIMAL(10, 2),
    qc VARCHAR(50),

    -- Sex Prediction
    chrx_cnt INTEGER,
    chry_cnt INTEGER,
    pred_sex VARCHAR(10),

    -- Metadata
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_analysis_qc UNIQUE (sample_id, service_code, batch, row_number)
);

-- Indexes
CREATE INDEX idx_analysis_result_sample ON analysis_result(sample_id);
CREATE INDEX idx_analysis_result_service ON analysis_result(service_code);
CREATE INDEX idx_analysis_qc_sample ON analysis_qc(sample_id);

-- Comments
COMMENT ON TABLE analysis_result IS '분석 결과 데이터';
COMMENT ON TABLE analysis_qc IS '분석 QC 데이터';
