-- V8: Add Missing Foreign Keys for Data Integrity
--
-- This migration adds foreign key constraints that were missing in previous migrations
-- to ensure referential integrity across tables.

-- ============================================================================
-- 1. Add Foreign Keys to analysis_result table
-- ============================================================================

-- Add FK to service table
ALTER TABLE analysis_result
ADD CONSTRAINT fk_analysis_result_service
FOREIGN KEY (service_code) REFERENCES service(code)
ON DELETE RESTRICT
ON UPDATE CASCADE;

-- ============================================================================
-- 2. Add Foreign Keys to analysis_qc table
-- ============================================================================

-- Add FK to service table
ALTER TABLE analysis_qc
ADD CONSTRAINT fk_analysis_qc_service
FOREIGN KEY (service_code) REFERENCES service(code)
ON DELETE RESTRICT
ON UPDATE CASCADE;

-- ============================================================================
-- 3. Add Foreign Keys to report table
-- ============================================================================

-- Add FK to service table
ALTER TABLE report
ADD CONSTRAINT fk_report_service
FOREIGN KEY (service_code) REFERENCES service(code)
ON DELETE RESTRICT
ON UPDATE CASCADE;

-- ============================================================================
-- 4. Add Foreign Keys to work_sample table
-- ============================================================================

-- Add FK to service table
ALTER TABLE work_sample
ADD CONSTRAINT fk_work_sample_service
FOREIGN KEY (service_code) REFERENCES service(code)
ON DELETE RESTRICT
ON UPDATE CASCADE;

-- ============================================================================
-- 5. Add Foreign Keys to sequencing table
-- ============================================================================

-- Note: sample_id in sequencing is VARCHAR(100), not referencing sample table's BIGSERIAL
-- This is intentional as sequencing uses external sample identifiers
-- If referential integrity is needed in the future, sample table structure should be updated first

-- ============================================================================
-- Summary
-- ============================================================================
-- - 4 foreign key constraints added
-- - All constraints use ON DELETE RESTRICT to prevent accidental data loss
-- - All constraints use ON UPDATE CASCADE to propagate updates
-- - Data integrity is now enforced at the database level
