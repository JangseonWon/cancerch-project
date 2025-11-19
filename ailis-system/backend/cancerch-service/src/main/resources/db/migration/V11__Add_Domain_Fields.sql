-- V11: Add Missing Domain Fields to Database
--
-- This migration adds domain model fields that were missing from the database schema
-- to enable full domain-driven design implementation with database persistence.

-- ============================================================================
-- 1. preprocessing table - Add domain tracking fields
-- ============================================================================

-- Add UUID column for preprocessing records
ALTER TABLE preprocessing
ADD COLUMN uuid UUID NOT NULL DEFAULT gen_random_uuid();

-- Add version column for optimistic locking
ALTER TABLE preprocessing
ADD COLUMN version INTEGER NOT NULL DEFAULT 1;

-- Add workflow tracking fields
ALTER TABLE preprocessing
ADD COLUMN started_by VARCHAR(100);

ALTER TABLE preprocessing
ADD COLUMN started_at TIMESTAMP;

ALTER TABLE preprocessing
ADD COLUMN completed_by VARCHAR(100);

ALTER TABLE preprocessing
ADD COLUMN completed_at TIMESTAMP;

-- Add unique constraint on UUID
ALTER TABLE preprocessing
ADD CONSTRAINT uq_preprocessing_uuid UNIQUE (uuid);

-- ============================================================================
-- 2. analysis_result table - Add status and version
-- ============================================================================

-- Add status column for analysis workflow state
ALTER TABLE analysis_result
ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'PENDING';

-- Add version column for optimistic locking
ALTER TABLE analysis_result
ADD COLUMN version INTEGER NOT NULL DEFAULT 1;

-- Add status check constraint
ALTER TABLE analysis_result
ADD CONSTRAINT chk_analysis_result_status
CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED'));

-- ============================================================================
-- 3. analysis_qc table - Add updated_at and version
-- ============================================================================

-- Add updated_at timestamp
ALTER TABLE analysis_qc
ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add version column for optimistic locking
ALTER TABLE analysis_qc
ADD COLUMN version INTEGER NOT NULL DEFAULT 1;

-- ============================================================================
-- 4. report table - Add generated_at and updated_at
-- ============================================================================

-- Add generated_at timestamp (when report file was generated)
ALTER TABLE report
ADD COLUMN generated_at TIMESTAMP;

-- Add updated_at timestamp
ALTER TABLE report
ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- ============================================================================
-- Summary
-- ============================================================================
-- preprocessing: 6 columns added (uuid, version, started_by, started_at, completed_by, completed_at)
-- analysis_result: 2 columns added (status, version)
-- analysis_qc: 2 columns added (updated_at, version)
-- report: 2 columns added (generated_at, updated_at)
--
-- Total: 12 new columns to support:
-- - Optimistic locking (version columns)
-- - Workflow state tracking (status, started_*, completed_*)
-- - Entity identification (uuid)
-- - Audit trail (generated_at, updated_at)
--
-- These additions enable:
-- 1. Full DDD implementation with database persistence
-- 2. Concurrent modification detection
-- 3. Complete workflow tracking
-- 4. Production-ready repository implementations (JPA, R2DBC, MyBatis)
