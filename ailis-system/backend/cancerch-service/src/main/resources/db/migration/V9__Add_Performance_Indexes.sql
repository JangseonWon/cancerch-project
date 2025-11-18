-- V9: Add Performance Indexes
--
-- This migration adds indexes to columns that are frequently queried
-- to improve database query performance.

-- ============================================================================
-- 1. Indexes for analysis_result table
-- ============================================================================

-- Index on batch column for batch-based queries
CREATE INDEX idx_analysis_result_batch ON analysis_result(batch);

-- Composite index for sample and service queries (most common query pattern)
CREATE INDEX idx_analysis_result_sample_service_batch ON analysis_result(sample_id, service_code, batch);

-- ============================================================================
-- 2. Indexes for analysis_qc table
-- ============================================================================

-- Index on batch column for batch-based queries
CREATE INDEX idx_analysis_qc_batch ON analysis_qc(batch);

-- ============================================================================
-- 3. Indexes for sequencing table
-- ============================================================================

-- Index on barcode for quick barcode lookups (frequently used in lab workflows)
CREATE INDEX idx_sequencing_barcode ON sequencing(barcode);

-- Index on sample_id for sample-based queries
CREATE INDEX idx_sequencing_sample_id ON sequencing(sample_id);

-- ============================================================================
-- 4. Indexes for preprocessing table
-- ============================================================================

-- Index on sequencing_batch for batch-based queries
CREATE INDEX idx_preprocessing_batch ON preprocessing(sequencing_batch);

-- Index on state for status-based filtering
CREATE INDEX idx_preprocessing_state ON preprocessing(state);

-- Composite index for worklist_id and index (common query pattern)
CREATE INDEX idx_preprocessing_worklist_index ON preprocessing(worklist_id, index);

-- ============================================================================
-- 5. Indexes for report table
-- ============================================================================

-- Index on status for status-based filtering
CREATE INDEX idx_report_status ON report(status);

-- Index on created_at for time-based queries and sorting
CREATE INDEX idx_report_created_at ON report(created_at);

-- Composite index for sample and service queries
CREATE INDEX idx_report_sample_service ON report(sample_id, service_code);

-- ============================================================================
-- 6. Indexes for request table
-- ============================================================================

-- Index on requested_at for time-based queries
CREATE INDEX idx_request_requested_at ON request(requested_at);

-- Index on status for status filtering
CREATE INDEX idx_request_status ON request(status);

-- ============================================================================
-- Summary
-- ============================================================================
-- Total indexes added: 13
-- - analysis_result: 2 indexes
-- - analysis_qc: 1 index
-- - sequencing: 2 indexes
-- - preprocessing: 3 indexes
-- - report: 3 indexes
-- - request: 2 indexes
--
-- These indexes will significantly improve query performance for:
-- - Batch-based queries
-- - Sample/Service lookups
-- - Status filtering
-- - Time-based sorting
-- - Barcode searches
