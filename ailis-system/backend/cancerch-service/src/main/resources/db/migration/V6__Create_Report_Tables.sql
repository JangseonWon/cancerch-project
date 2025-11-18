-- Report 테이블
CREATE TABLE report (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    sample_id VARCHAR(100) NOT NULL,
    service_code VARCHAR(50) NOT NULL,

    -- Report Info
    batch VARCHAR(50),
    row_number INTEGER,
    report_name VARCHAR(200),
    file_path VARCHAR(500),
    file_size BIGINT,
    language VARCHAR(10) DEFAULT 'ko',

    -- Status
    status VARCHAR(50) DEFAULT 'PENDING',
    is_printed BOOLEAN DEFAULT false,

    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    published_at TIMESTAMP,
    published_by VARCHAR(100),

    -- Metadata
    version INTEGER DEFAULT 1,
    description TEXT,
    result_info JSONB,

    CONSTRAINT uq_report UNIQUE (sample_id, service_code, created_at)
);

-- Report Log 테이블
CREATE TABLE report_log (
    id BIGSERIAL PRIMARY KEY,
    report_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    CONSTRAINT fk_report_log_report FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_report_sample ON report(sample_id);
CREATE INDEX idx_report_service ON report(service_code);
CREATE INDEX idx_report_status ON report(status);
CREATE INDEX idx_report_created ON report(created_at DESC);
CREATE INDEX idx_report_log_report ON report_log(report_id);

-- Comments
COMMENT ON TABLE report IS '보고서 메타데이터';
COMMENT ON TABLE report_log IS '보고서 생성/발행 로그';
