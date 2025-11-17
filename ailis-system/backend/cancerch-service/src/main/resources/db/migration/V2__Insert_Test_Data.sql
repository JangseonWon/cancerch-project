-- Test Worklists
INSERT INTO worklist (uuid, name, batch_prefix, batch_index, status, created_by, updated_by, version)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Morning Batch 2025-01-17', 'WL', 20250117001, 'PENDING', 'admin', 'admin', 0),
    ('550e8400-e29b-41d4-a716-446655440002', 'Afternoon Batch 2025-01-17', 'WL', 20250117002, 'IN_PROGRESS', 'admin', 'admin', 0),
    ('550e8400-e29b-41d4-a716-446655440003', 'Evening Batch 2025-01-16', 'WL', 20250116001, 'COMPLETED', 'admin', 'admin', 0);

-- Test Work Samples for Morning Batch
INSERT INTO work_sample (worklist_id, sample_id, barcode, service_code, row_number)
SELECT
    w.id,
    'S202501170001',
    'BC202501170001',
    'NGS001',
    0
FROM worklist w WHERE w.uuid = '550e8400-e29b-41d4-a716-446655440001';

INSERT INTO work_sample (worklist_id, sample_id, barcode, service_code, row_number)
SELECT
    w.id,
    'S202501170002',
    'BC202501170002',
    'NGS001',
    1
FROM worklist w WHERE w.uuid = '550e8400-e29b-41d4-a716-446655440001';

INSERT INTO work_sample (worklist_id, sample_id, barcode, service_code, row_number)
SELECT
    w.id,
    'S202501170003',
    'BC202501170003',
    'PCR001',
    2
FROM worklist w WHERE w.uuid = '550e8400-e29b-41d4-a716-446655440001';

-- Test Work Samples for Afternoon Batch
INSERT INTO work_sample (worklist_id, sample_id, barcode, service_code, row_number)
SELECT
    w.id,
    'S202501170004',
    'BC202501170004',
    'NGS002',
    0
FROM worklist w WHERE w.uuid = '550e8400-e29b-41d4-a716-446655440002';

INSERT INTO work_sample (worklist_id, sample_id, barcode, service_code, row_number)
SELECT
    w.id,
    'S202501170005',
    'BC202501170005',
    'NGS002',
    1
FROM worklist w WHERE w.uuid = '550e8400-e29b-41d4-a716-446655440002';

-- Test Work Samples for Completed Evening Batch
INSERT INTO work_sample (worklist_id, sample_id, barcode, service_code, row_number)
SELECT
    w.id,
    'S202501160001',
    'BC202501160001',
    'PCR002',
    0
FROM worklist w WHERE w.uuid = '550e8400-e29b-41d4-a716-446655440003';

INSERT INTO work_sample (worklist_id, sample_id, barcode, service_code, row_number)
SELECT
    w.id,
    'S202501160002',
    'BC202501160002',
    'PCR002',
    1
FROM worklist w WHERE w.uuid = '550e8400-e29b-41d4-a716-446655440003';

INSERT INTO work_sample (worklist_id, sample_id, barcode, service_code, row_number)
SELECT
    w.id,
    'S202501160003',
    'BC202501160003',
    'NGS001',
    2
FROM worklist w WHERE w.uuid = '550e8400-e29b-41d4-a716-446655440003';
