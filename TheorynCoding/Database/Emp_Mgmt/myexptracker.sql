-- ========================================================================
-- MyExpTracker Database Schema (MySQL-like clarity but PostgreSQL optimized)
-- All tables + triggers + audit + inline WHY explanations
-- ========================================================================

-- ------------------------------------------------------------------------
-- EXTENSIONS
-- ------------------------------------------------------------------------

-- pgcrypto is required ONLY for bcrypt hashing inside SQL using crypt()
-- We use it for creating sample admin/user accounts.
CREATE EXTENSION IF NOT EXISTS pgcrypto;    -- required for crypt() + gen_salt('bf')


-- ------------------------------------------------------------------------
-- CLEANUP (safe in development)
-- These DROP commands avoid conflicts if tables already exist.
-- ------------------------------------------------------------------------
DROP TRIGGER IF EXISTS trg_audit_receipts ON receipts CASCADE;
DROP TRIGGER IF EXISTS trg_audit_expense ON expense CASCADE;
DROP TRIGGER IF EXISTS trg_audit_users ON users CASCADE;

DROP TABLE IF EXISTS audit CASCADE;
DROP TABLE IF EXISTS receipts CASCADE;
DROP TABLE IF EXISTS expense CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TYPE IF EXISTS user_role CASCADE;



-- =========================================================================
-- ENUMS
-- =========================================================================

-- WHY?  
-- Using ENUM ensures only valid roles can be used.  
-- Helps guarantee consistent values (USER, ADMIN, AUDITOR, MANAGER).
CREATE TYPE user_role AS ENUM ('USER','ADMIN','AUDITOR','MANAGER');

COMMENT ON TYPE user_role IS 'Allowed roles for system users. Controls permission levels.';



-- =========================================================================
-- USERS TABLE
-- =========================================================================

-- WHY?  
-- Stores login/user accounts, password hashes, status, and audit info.
CREATE TABLE users (
  id              SERIAL PRIMARY KEY,              -- auto-increment user ID
  email           VARCHAR(255) UNIQUE NOT NULL,    -- used as login username
  password_hash   TEXT NOT NULL,                   -- bcrypt hash stored here
  full_name       VARCHAR(255),                    -- user full name
  role            user_role NOT NULL DEFAULT 'USER', -- access level
  is_active       BOOLEAN NOT NULL DEFAULT TRUE,   -- deactivate user without deleting
  last_login_at   TIMESTAMP WITH TIME ZONE,        -- login audit
  created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  created_by      INTEGER,                         -- ID of user who created this user
  updated_at      TIMESTAMP WITH TIME ZONE,
  updated_by      INTEGER,
  deleted_at      TIMESTAMP WITH TIME ZONE,
  is_deleted      BOOLEAN NOT NULL DEFAULT FALSE   -- soft deletion instead of hard delete
);

-- Detailed comments for documentation and pgAdmin auto-doc
COMMENT ON TABLE users IS 'User accounts with roles, hashed passwords, and audit details.';
COMMENT ON COLUMN users.id              IS 'Primary key.';
COMMENT ON COLUMN users.email           IS 'Unique login email.';
COMMENT ON COLUMN users.password_hash   IS 'Password stored as bcrypt hash.';
COMMENT ON COLUMN users.full_name       IS 'User display name.';
COMMENT ON COLUMN users.role            IS 'Role-based authorization.';
COMMENT ON COLUMN users.is_active       IS 'Deactivate user without deleting.';
COMMENT ON COLUMN users.last_login_at   IS 'Last login timestamp.';
COMMENT ON COLUMN users.created_at      IS 'When record was created.';
COMMENT ON COLUMN users.created_by      IS 'User who created this record.';
COMMENT ON COLUMN users.updated_at      IS 'Record last update timestamp.';
COMMENT ON COLUMN users.updated_by      IS 'User who updated this record.';
COMMENT ON COLUMN users.deleted_at      IS 'Soft delete timestamp.';
COMMENT ON COLUMN users.is_deleted      IS 'Soft delete flag.';

-- WHY INDEX?  
-- Searching by email is the #1 most common operation in login.
CREATE INDEX idx_users_email ON users(email);

select * from users

-- =========================================================================
-- EXPENSE TABLE
-- =========================================================================

-- WHY?  
-- Stores individual expense entries submitted by a user.  
-- One user → many expenses.
CREATE TABLE expense (
  id              SERIAL PRIMARY KEY,
  user_id         INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  -- If user is deleted, their expenses should be deleted too (CASCADE)

  title           VARCHAR(500) NOT NULL,            -- expense title
  category        VARCHAR(100),                     -- optional classification
  amount          NUMERIC(14,2) NOT NULL DEFAULT 0, -- stores money safely
  currency        VARCHAR(10) NOT NULL DEFAULT 'INR', -- default Indian Rupees
  date            DATE NOT NULL DEFAULT CURRENT_DATE, -- expense date
  notes           TEXT,                             -- optional additional notes
  status          VARCHAR(50) NOT NULL DEFAULT 'NEW', -- NEW, APPROVED, REJECTED, etc.

  tags            TEXT[],                           -- useful for analytics/search

  is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,   -- soft delete pattern
  created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  created_by      INTEGER,
  updated_at      TIMESTAMP WITH TIME ZONE,
  updated_by      INTEGER,
  deleted_at      TIMESTAMP WITH TIME ZONE
);

COMMENT ON TABLE expense IS 'Expense entries linked to a specific user.';
COMMENT ON COLUMN expense.user_id IS 'FK → users.id (owner of this expense).';
COMMENT ON COLUMN expense.amount IS 'Monetary value of the expense.';
COMMENT ON COLUMN expense.status IS 'Workflow stage of the expense.';
COMMENT ON COLUMN expense.tags IS 'Searchable keywords for expense grouping.';

-- WHY INDEXES?  
-- Lookups by user, date, status are extremely common in reports.
CREATE INDEX idx_expense_userid  ON expense(user_id);
CREATE INDEX idx_expense_status  ON expense(status);
CREATE INDEX idx_expense_date    ON expense(date);

-- GIN index is needed for fast tag searching
CREATE INDEX idx_expense_tags    ON expense USING GIN (tags);



-- =========================================================================
-- RECEIPTS TABLE  (NO CIRCULAR FK)
-- =========================================================================

-- WHY?  
-- Stores uploaded receipt metadata.  
-- Each receipt belongs to ONE expense.  
-- Files are NOT stored in DB (only references to filesystem/S3 path).
CREATE TABLE receipts (
  id              SERIAL PRIMARY KEY,
  expense_id      INTEGER REFERENCES expense(id) ON DELETE CASCADE,
  -- If expense is deleted → delete all its receipts

  filename        VARCHAR(500) NOT NULL,     -- original uploaded filename
  content_type    VARCHAR(100),              -- MIME type
  file_size       BIGINT,                    -- file size in bytes
  storage_path    TEXT,                      -- absolute or relative file path
  uploaded_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  uploaded_by     INTEGER                    -- who uploaded receipt (user id)
);

COMMENT ON TABLE receipts IS 'Metadata for uploaded expense receipts.';
COMMENT ON COLUMN receipts.expense_id IS 'FK → expense.id (receipt belongs to an expense).';
COMMENT ON COLUMN receipts.storage_path IS 'Path on disk or cloud bucket.';

CREATE INDEX idx_receipts_expense ON receipts(expense_id);
-- WHY index?  
-- Fetch receipts by expense is extremely common (1 expense → many receipts).



-- =========================================================================
-- AUDIT TABLE
-- =========================================================================

-- WHY AUDIT?  
-- Every INSERT/UPDATE/DELETE is recorded.  
-- This is critical for:
--  ✔ security  
--  ✔ compliance  
--  ✔ investigating user mistakes  
--  ✔ tracking operations made by backend services  
CREATE TABLE audit (
  id              BIGSERIAL PRIMARY KEY,
  table_name      TEXT NOT NULL,
  operation       VARCHAR(10) NOT NULL,   -- INSERT, UPDATE, DELETE
  record_id       TEXT,                   -- store PK of affected row
  changed_by      TEXT,                   -- stored via set_config('app.current_user')
  changed_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  old_row         JSONB,                  -- previous data
  new_row         JSONB,                  -- new data
  statement       TEXT                    -- optional SQL text
);

COMMENT ON TABLE audit IS 'Row-level audit log storing before/after JSON states.';
COMMENT ON COLUMN audit.changed_by IS 'Application user ID/email (NOT DB account).';

CREATE INDEX idx_audit_table      ON audit(table_name);
CREATE INDEX idx_audit_record     ON audit(record_id);
CREATE INDEX idx_audit_changed_at ON audit(changed_at);



-- =========================================================================
-- AUDIT TRIGGER FUNCTION
-- =========================================================================

-- WHY?  
-- Automatically logs data changes without modifying your Java code everywhere.
CREATE OR REPLACE FUNCTION fn_audit_trigger()
RETURNS TRIGGER AS $$
DECLARE
  v_record_id TEXT;
  v_user TEXT;
BEGIN
  -- app.current_user is set from Java via:
  -- SELECT set_config('app.current_user', '<id>:<email>', true);
  v_user := current_setting('app.current_user', true);

  IF v_user IS NULL THEN
    v_user := current_user;  -- fallback to DB user (rarely used)
  END IF;

  IF TG_OP = 'INSERT' THEN
    v_record_id := NEW.id::text;
    INSERT INTO audit(table_name, operation, record_id, changed_by, old_row, new_row, statement)
    VALUES (TG_TABLE_NAME, 'INSERT', v_record_id, v_user, NULL, row_to_json(NEW), current_query());
    RETURN NEW;

  ELSIF TG_OP = 'UPDATE' THEN
    v_record_id := NEW.id::text;
    INSERT INTO audit(table_name, operation, record_id, changed_by, old_row, new_row, statement)
    VALUES (TG_TABLE_NAME, 'UPDATE', v_record_id, v_user, row_to_json(OLD), row_to_json(NEW), current_query());
    RETURN NEW;

  ELSIF TG_OP = 'DELETE' THEN
    v_record_id := OLD.id::text;
    INSERT INTO audit(table_name, operation, record_id, changed_by, old_row, new_row, statement)
    VALUES (TG_TABLE_NAME, 'DELETE', v_record_id, v_user, row_to_json(OLD), NULL, current_query());
    RETURN OLD;
  END IF;

  RETURN NULL;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION fn_audit_trigger() IS 'Generic audit logger for INSERT/UPDATE/DELETE.';



-- =========================================================================
-- ATTACH AUDIT TRIGGERS
-- =========================================================================

-- WHY?  
-- These triggers will track all changes automatically.
CREATE TRIGGER trg_audit_users
AFTER INSERT OR UPDATE OR DELETE ON users
FOR EACH ROW EXECUTE FUNCTION fn_audit_trigger();

CREATE TRIGGER trg_audit_expense
AFTER INSERT OR UPDATE OR DELETE ON expense
FOR EACH ROW EXECUTE FUNCTION fn_audit_trigger();

CREATE TRIGGER trg_audit_receipts
AFTER INSERT OR UPDATE OR DELETE ON receipts
FOR EACH ROW EXECUTE FUNCTION fn_audit_trigger();



-- =========================================================================
-- SAMPLE ADMIN + USER
-- =========================================================================

-- WHY crypt('password', gen_salt('bf'))?  
-- → bcrypt hashing inside PostgreSQL using pgcrypto.
INSERT INTO users (email, password_hash, full_name, role)
VALUES ('admin@example.com', crypt('adminpass', gen_salt('bf')), 'Administrator', 'ADMIN');

INSERT INTO users (email, password_hash, full_name, role)
VALUES ('user@example.com', crypt('password', gen_salt('bf')), 'Demo User', 'USER');

-- Sample expense
INSERT INTO expense (user_id, title, category, amount, currency, notes, status, created_by)
VALUES (
  (SELECT id FROM users WHERE email='user@example.com'),
  'Laptop purchase',
  'Equipment',
  55000.00,
  'INR',
  'Development laptop',
  'APPROVED',
  (SELECT id FROM users WHERE email='user@example.com')
);
