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

-- Note: idx_preprocessing_state already created in V3, skipping duplicate

-- Composite index for worklist_id and index (common query pattern)
CREATE INDEX idx_preprocessing_worklist_index ON preprocessing(worklist_id, index);

-- ============================================================================
-- 5. Indexes for report table
-- ============================================================================

-- Note: idx_report_status already created in V6, skipping duplicate
-- Note: idx_report_created (DESC) already created in V6, skipping duplicate idx_report_created_at

-- Composite index for sample and service queries
CREATE INDEX idx_report_sample_service ON report(sample_id, service_code);

-- ============================================================================
-- 6. Indexes for request table
-- ============================================================================

-- Index on requested_at for time-based queries
CREATE INDEX idx_request_requested_at ON request(requested_at);

-- Note: idx_request_status already created in V5, skipping duplicate

-- ============================================================================
-- Summary
-- ============================================================================
-- Total indexes added: 9 (reduced from 13 to avoid duplicates)
-- - analysis_result: 2 indexes
-- - analysis_qc: 1 index
-- - sequencing: 2 indexes
-- - preprocessing: 2 indexes (1 skipped - already in V3)
-- - report: 1 index (2 skipped - already in V6)
-- - request: 1 index (1 skipped - already in V5)
--
-- These indexes will significantly improve query performance for:
-- - Batch-based queries
-- - Sample/Service lookups
-- - Status filtering
-- - Time-based sorting
-- - Barcode searches
