-- Service 코드 데이터
INSERT INTO service (code, name, category, description) VALUES
('N201', 'AVOID', 'NGS', 'AVOID 암 검진 검사'),
('N203', 'Cancerch (Genome)', 'NGS', 'Cancerch 암 검진 - Genome'),
('N204', 'Cancerch Plus (Genome)', 'NGS', 'Cancerch Plus - Genome'),
('N205', 'Cancerch Expanded (Genome)', 'NGS', 'Cancerch 확장형 - Genome'),
('N206', 'Cancerch Comprehensive (Genome)', 'NGS', 'Cancerch 종합형 - Genome'),
('N256', 'Cancerch (Gangbuk)', 'NGS', 'Cancerch - 강북삼성'),
('J001', 'Tumor DNA (Gangbuk)', 'NGS', '종양 DNA 검사 - 강북삼성'),
('J002', 'Tumor DNA Plus (Gangbuk)', 'NGS', '종양 DNA Plus - 강북삼성'),
('J024', 'Cancerch (Japan)', 'NGS', 'Cancerch - 일본'),
('ON203', 'Cancerch (English)', 'NGS', 'Cancerch - 영문'),
('ON204', 'DNACX', 'NGS', 'DNACX 검사'),
('ON206', 'DNACT', 'NGS', 'DNACT 검사'),
('ON256', 'Tumor DNA (English)', 'NGS', '종양 DNA - 영문'),
('PCR001', 'PCR Test 1', 'PCR', 'PCR 기본 검사'),
('PCR002', 'PCR Test 2', 'PCR', 'PCR 확장 검사'),
('NGS001', 'NGS Basic', 'NGS', 'NGS 기본 검사'),
('NGS002', 'NGS Advanced', 'NGS', 'NGS 고급 검사');

-- Plate Index 샘플 데이터 (96-well plate)
INSERT INTO plate_index (plate, well, index_name, sequence) VALUES
('A', 'A01', 'IDX001', 'ATCACG'),
('A', 'A02', 'IDX002', 'CGATGT'),
('A', 'A03', 'IDX003', 'TTAGGC'),
('A', 'A04', 'IDX004', 'TGACCA'),
('A', 'A05', 'IDX005', 'ACAGTG'),
('A', 'A06', 'IDX006', 'GCCAAT'),
('A', 'A07', 'IDX007', 'CAGATC'),
('A', 'A08', 'IDX008', 'ACTTGA');

-- 테스트 Patient 데이터
INSERT INTO patient (patient_id, name, birth_date, gender) VALUES
('P000001', '김철수', '1980-01-15', 'M'),
('P000002', '이영희', '1985-03-22', 'F'),
('P000003', '박민수', '1975-07-10', 'M');

-- 테스트 Sample 데이터
INSERT INTO sample (sample_id, patient_id, barcode, sample_type, collected_at) VALUES
('S202501170001', 1, 'BC202501170001', 'BLOOD', '2025-01-17 09:00:00'),
('S202501170002', 1, 'BC202501170002', 'BLOOD', '2025-01-17 09:00:00'),
('S202501170003', 2, 'BC202501170003', 'BLOOD', '2025-01-17 10:00:00'),
('S202501160001', 3, 'BC202501160001', 'BLOOD', '2025-01-16 14:00:00');

-- 테스트 Request 데이터
INSERT INTO request (sample_id, service_code, requested_at, requested_by, status) VALUES
(1, 'NGS001', '2025-01-17 09:30:00', 'doctor1', 'APPROVED'),
(2, 'NGS001', '2025-01-17 09:30:00', 'doctor1', 'APPROVED'),
(3, 'PCR001', '2025-01-17 10:30:00', 'doctor2', 'APPROVED'),
(4, 'PCR002', '2025-01-16 14:30:00', 'doctor3', 'COMPLETED');
