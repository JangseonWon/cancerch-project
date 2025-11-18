-- V10: Standardize Column Names for Consistency
--
-- This migration renames columns to follow a consistent naming convention
-- across all tables (created_at, updated_at, created_by, updated_by).

-- ============================================================================
-- 1. Standardize preprocessing table column names
-- ============================================================================

-- Rename create_by to created_by
ALTER TABLE preprocessing
RENAME COLUMN create_by TO created_by;

-- Rename create_at to created_at
ALTER TABLE preprocessing
RENAME COLUMN create_at TO created_at;

-- Rename last_modify_by to updated_by
ALTER TABLE preprocessing
RENAME COLUMN last_modify_by TO updated_by;

-- Rename last_modify_at to updated_at
ALTER TABLE preprocessing
RENAME COLUMN last_modify_at TO updated_at;

-- ============================================================================
-- 2. Verify batch column consistency (informational)
-- ============================================================================

-- Note: The following inconsistencies exist but require data migration:
-- - worklist.batch_prefix: VARCHAR(50)
-- - analysis_result.batch: VARCHAR(50)
-- - preprocessing.sequencing_batch: VARCHAR(100)
--
-- If these represent the same data type, consider standardizing to VARCHAR(50)
-- in a future migration after confirming data compatibility.

-- ============================================================================
-- Summary
-- ============================================================================
-- - 4 column renames in preprocessing table
-- - All timestamp/audit columns now follow standard naming convention:
--   * created_at (not create_at)
--   * updated_at (not last_modify_at)
--   * created_by (not create_by)
--   * updated_by (not last_modify_by)
--
-- This improves code maintainability and reduces confusion when working
-- with audit columns across different tables.
