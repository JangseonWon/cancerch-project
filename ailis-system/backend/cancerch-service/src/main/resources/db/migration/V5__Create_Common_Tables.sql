-- Patient 테이블
CREATE TABLE patient (
    id BIGSERIAL PRIMARY KEY,
    patient_id VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100),
    birth_date DATE,
    gender VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Sample 테이블
CREATE TABLE sample (
    id BIGSERIAL PRIMARY KEY,
    sample_id VARCHAR(100) NOT NULL UNIQUE,
    patient_id BIGINT,
    barcode VARCHAR(100),
    sample_type VARCHAR(50),
    collected_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sample_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
);

-- Service 테이블 (검사 코드)
CREATE TABLE service (
    code VARCHAR(50) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Request 테이블 (의뢰)
CREATE TABLE request (
    id BIGSERIAL PRIMARY KEY,
    sample_id BIGINT NOT NULL,
    service_code VARCHAR(50) NOT NULL,
    requested_at TIMESTAMP NOT NULL,
    requested_by VARCHAR(100),
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_request_sample FOREIGN KEY (sample_id) REFERENCES sample(id),
    CONSTRAINT fk_request_service FOREIGN KEY (service_code) REFERENCES service(code),
    CONSTRAINT uq_request UNIQUE (sample_id, service_code)
);

-- Indexes
CREATE INDEX idx_sample_patient ON sample(patient_id);
CREATE INDEX idx_request_sample ON request(sample_id);
CREATE INDEX idx_request_service ON request(service_code);
CREATE INDEX idx_request_status ON request(status);

-- Comments
COMMENT ON TABLE patient IS '환자 정보';
COMMENT ON TABLE sample IS '검체 정보';
COMMENT ON TABLE service IS '검사 서비스 코드';
COMMENT ON TABLE request IS '검사 의뢰';
