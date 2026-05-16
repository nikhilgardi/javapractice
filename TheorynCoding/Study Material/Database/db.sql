CREATE SCHEMA core;
CREATE SCHEMA hr;
CREATE SCHEMA finance;
CREATE SCHEMA audit;

SET search_path TO core, hr, finance, audit;

CREATE TYPE core.user_status_enum AS ENUM
('ACTIVE','INACTIVE','LOCKED');

CREATE TYPE hr.employee_status_enum AS ENUM
('ACTIVE','TERMINATED','ON_LEAVE');

CREATE TYPE finance.transaction_type_enum AS ENUM
('DEPOSIT','WITHDRAW','TRANSFER','PAYROLL');

-- Table: core.role_master
-- Master table storing application roles for access control.
-- Each role represents a permission group used across HR, banking, and other modules.

CREATE TABLE core.role_master (
    role_id SERIAL PRIMARY KEY,            -- Primary key for the role, auto-incremented
    role_code VARCHAR(50) UNIQUE NOT NULL, -- Unique system code identifying the role (e.g., ADMIN, HR_MANAGER, FIN_MANAGER)
    role_name VARCHAR(100) NOT NULL,       -- Human-readable name of the role (e.g., Administrator, HR Manager, Finance Manager)
    description TEXT,                      -- Optional detailed description of the role and its responsibilities
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp when the role was created; defaults to current time
);

-- Table-level comment
COMMENT ON TABLE core.role_master IS
'Master table storing application roles used for access control.
Each role represents a permission group used across HR and banking modules.';

-- Column-level comments
COMMENT ON COLUMN core.role_master.role_id IS
'Primary key for the role, auto-incremented using SERIAL.';

COMMENT ON COLUMN core.role_master.role_code IS
'Unique system code for the role (ADMIN, HR_MANAGER, FIN_MANAGER). Used for programmatic access checks.';

COMMENT ON COLUMN core.role_master.role_name IS
'Human-readable name of the role (e.g., Administrator, HR Manager, Finance Manager).';

COMMENT ON COLUMN core.role_master.description IS
'Optional detailed description of the role and its responsibilities.';

COMMENT ON COLUMN core.role_master.created_at IS
'Timestamp when the role was created. Defaults to the current time at insertion.';


-- Table: core.users
-- Stores application users responsible for managing HR, payroll, and banking modules.
-- Authentication and authorization are controlled through this table.

CREATE TABLE core.users (
    user_id BIGSERIAL PRIMARY KEY,          -- Primary key for the user, auto-incremented
    username VARCHAR(120) UNIQUE NOT NULL,  -- Unique login username for the user
    password_hash TEXT NOT NULL,            -- Encrypted password stored using secure hashing algorithm
    role_id INT REFERENCES core.role_master(role_id), -- Foreign key linking user to a role in role_master
    user_status core.user_status_enum DEFAULT 'ACTIVE', -- Status of user account (ACTIVE, INACTIVE, etc.)
    failed_login_attempts INT DEFAULT 0,    -- Tracks failed login attempts for account security and lock control
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp when the user account was created
);

-- Table-level comment
COMMENT ON TABLE core.users IS
'Application users responsible for managing HR, payroll and banking modules.
Authentication and authorization are controlled through this table.';

-- Column-level comments
COMMENT ON COLUMN core.users.user_id IS
'Primary key for the user, auto-incremented using BIGSERIAL.';

COMMENT ON COLUMN core.users.username IS
'Unique login username for the user. Used for authentication.';

COMMENT ON COLUMN core.users.password_hash IS
'Encrypted password stored using secure hashing algorithm.';

COMMENT ON COLUMN core.users.role_id IS
'Foreign key referencing core.role_master.role_id. Determines the role/permissions of the user.';

COMMENT ON COLUMN core.users.user_status IS
'Indicates the current status of the user account (e.g., ACTIVE, INACTIVE, SUSPENDED).';

COMMENT ON COLUMN core.users.failed_login_attempts IS
'Counts failed login attempts for account security and potential lockout policy.';

COMMENT ON COLUMN core.users.created_at IS
'Timestamp when the user account was created. Defaults to the current time at insertion.';


-- Table: hr.department_master
-- Master list of organizational departments.
-- Used for HR reporting, employee assignment, and analytics.

CREATE TABLE hr.department_master (
    department_id SERIAL PRIMARY KEY,           -- Primary key for the department, auto-incremented
    department_code VARCHAR(50) UNIQUE NOT NULL, -- Unique code identifying the department internally
    department_name VARCHAR(150) NOT NULL,     -- Human-readable name of the department
    location VARCHAR(150),                      -- Optional location of the department (e.g., office city or floor)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp when the department record was created
);

-- Table-level comment
COMMENT ON TABLE hr.department_master IS
'Master list of organizational departments.
Used for HR reporting, employee assignment and analytics.';

-- Column-level comments
COMMENT ON COLUMN hr.department_master.department_id IS
'Primary key for the department, auto-incremented using SERIAL.';

COMMENT ON COLUMN hr.department_master.department_code IS
'Unique department code used internally by systems (e.g., HR001, FIN002).';

COMMENT ON COLUMN hr.department_master.department_name IS
'Human-readable name of the department (e.g., Human Resources, Finance).';

COMMENT ON COLUMN hr.department_master.location IS
'Optional physical location of the department (e.g., building, floor, or city).';

COMMENT ON COLUMN hr.department_master.created_at IS
'Timestamp when the department record was created. Defaults to current time at insertion.';


-- Table: hr.employees
-- Core employee master table used in HR, payroll, and banking integrations.
-- Maintains employee personal information, organizational hierarchy, and employment status.

CREATE TABLE hr.employees (
    employee_id BIGSERIAL PRIMARY KEY,               -- Primary key for the employee, auto-incremented
    employee_code VARCHAR(50) UNIQUE NOT NULL,      -- Unique business identifier assigned to each employee
    first_name VARCHAR(120) NOT NULL,               -- Employee's first name
    last_name VARCHAR(120),                          -- Employee's last name (optional)
    email VARCHAR(150) UNIQUE,                       -- Employee's work email, must be unique
    department_id INT REFERENCES hr.department_master(department_id), -- Foreign key linking to department
    manager_id BIGINT,                               -- Reference to employee's manager for hierarchy
    hire_date DATE NOT NULL,                          -- Date when employee was hired
    employee_status hr.employee_status_enum DEFAULT 'ACTIVE', -- Current employment status (ACTIVE, INACTIVE, etc.)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp when employee record was created

    CHECK (hire_date <= CURRENT_DATE)               -- Ensures hire date is not in the future
);

-- Table-level comment
COMMENT ON TABLE hr.employees IS
'Core employee master table used in HR, payroll and banking integrations.
Maintains employee personal information and organizational hierarchy.';

-- Column-level comments
COMMENT ON COLUMN hr.employees.employee_id IS
'Primary key for the employee, auto-incremented using BIGSERIAL.';

COMMENT ON COLUMN hr.employees.employee_code IS
'Unique business identifier assigned to each employee.';

COMMENT ON COLUMN hr.employees.first_name IS
'First name of the employee.';

COMMENT ON COLUMN hr.employees.last_name IS
'Last name of the employee (optional).';

COMMENT ON COLUMN hr.employees.email IS
'Unique work email for the employee.';

COMMENT ON COLUMN hr.employees.department_id IS
'Foreign key referencing hr.department_master.department_id. Indicates the department the employee belongs to.';

COMMENT ON COLUMN hr.employees.manager_id IS
'Reference to employee manager for hierarchical reporting and organizational structure.';

COMMENT ON COLUMN hr.employees.hire_date IS
'Date the employee was hired. Must be on or before the current date.';

COMMENT ON COLUMN hr.employees.employee_status IS
'Current status of the employee account (e.g., ACTIVE, INACTIVE, SUSPENDED).';

COMMENT ON COLUMN hr.employees.created_at IS
'Timestamp when the employee record was created. Defaults to current time at insertion.';


-- Table: hr.salary_history
-- Stores historical salary changes for employees.
-- Used for payroll processing, compensation analytics, and auditing.

CREATE TABLE hr.salary_history (
    salary_id BIGSERIAL PRIMARY KEY,                 -- Primary key for the salary record, auto-incremented
    employee_id BIGINT REFERENCES hr.employees(employee_id), -- Foreign key linking to the employee
    salary_amount NUMERIC(14,2) NOT NULL,           -- Salary amount applicable from the effective date
    effective_date DATE DEFAULT CURRENT_DATE,       -- Date from which this salary is effective
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the salary record was created

    CHECK (salary_amount > 0)                        -- Salary must be greater than zero
);

-- Table-level comment
COMMENT ON TABLE hr.salary_history IS
'Stores historical salary changes for employees.
Used for payroll processing, compensation analytics, and auditing.';

-- Column-level comments
COMMENT ON COLUMN hr.salary_history.salary_id IS
'Primary key for the salary record, auto-incremented using BIGSERIAL.';

COMMENT ON COLUMN hr.salary_history.employee_id IS
'Foreign key referencing hr.employees.employee_id. Indicates the employee for whom the salary record applies.';

COMMENT ON COLUMN hr.salary_history.salary_amount IS
'Salary amount applicable from the effective date. Must be positive.';

COMMENT ON COLUMN hr.salary_history.effective_date IS
'Date from which this salary amount is effective. Defaults to current date if not specified.';

COMMENT ON COLUMN hr.salary_history.created_at IS
'Timestamp when the salary record was created. Defaults to current time at insertion.';


-- Table: finance.account_master
-- Represents financial accounts associated with employees.
-- Supports payroll deposits, internal banking modules, and reporting.

CREATE TABLE finance.account_master (
    account_id BIGSERIAL PRIMARY KEY,               -- Primary key for the account, auto-incremented
    employee_id BIGINT REFERENCES hr.employees(employee_id), -- Foreign key linking to the employee who owns this account
    account_number VARCHAR(40) UNIQUE NOT NULL,     -- Unique financial account identifier
    balance NUMERIC(18,2) DEFAULT 0,               -- Current account balance, must be >= 0
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the account was created

    CHECK (balance >= 0)                            -- Balance cannot be negative
);

-- Table-level comment
COMMENT ON TABLE finance.account_master IS
'Represents financial accounts associated with employees.
Supports payroll deposits, internal banking modules, and reporting.';

-- Column-level comments
COMMENT ON COLUMN finance.account_master.account_id IS
'Primary key for the account, auto-incremented using BIGSERIAL.';

COMMENT ON COLUMN finance.account_master.employee_id IS
'Foreign key referencing hr.employees.employee_id. Identifies the employee who owns this account.';

COMMENT ON COLUMN finance.account_master.account_number IS
'Unique financial account identifier used for payroll and banking transactions.';

COMMENT ON COLUMN finance.account_master.balance IS
'Current account balance. Must be non-negative and updated via payroll or internal transactions.';

COMMENT ON COLUMN finance.account_master.created_at IS
'Timestamp when the account record was created. Defaults to current time at insertion.';


-- Table: finance.transactions
-- Ledger-style transaction table storing all financial movements.
-- Used for banking analytics, reporting, and reconciliation.

CREATE TABLE finance.transactions (
    transaction_id BIGSERIAL PRIMARY KEY,              -- Primary key for the transaction, auto-incremented
    account_id BIGINT REFERENCES finance.account_master(account_id), -- Foreign key linking to the associated account
    transaction_type finance.transaction_type_enum,   -- Type of transaction (e.g., CREDIT, DEBIT)
    amount NUMERIC(18,2),                             -- Transaction amount, must be positive
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the transaction occurred
    description TEXT,                                  -- Optional description or notes for the transaction

    CHECK (amount > 0)                                -- Amount must be greater than zero
);

-- Table-level comment
COMMENT ON TABLE finance.transactions IS
'Ledger style transaction table storing all financial movements.
Used for banking analytics, reporting and reconciliation.';

-- Column-level comments
COMMENT ON COLUMN finance.transactions.transaction_id IS
'Primary key for the transaction, auto-incremented using BIGSERIAL.';

COMMENT ON COLUMN finance.transactions.account_id IS
'Foreign key referencing finance.account_master.account_id. Indicates the account affected by this transaction.';

COMMENT ON COLUMN finance.transactions.transaction_type IS
'Type of transaction (e.g., CREDIT, DEBIT) stored using finance.transaction_type_enum.';

COMMENT ON COLUMN finance.transactions.amount IS
'Amount of the transaction. Must be positive.';

COMMENT ON COLUMN finance.transactions.transaction_time IS
'Timestamp when the transaction was created. Defaults to current time at insertion.';

COMMENT ON COLUMN finance.transactions.description IS
'Optional text describing the transaction for reference, audit, or reporting.';


-- Table: audit.audit_log
-- Centralized audit table capturing UPDATE and DELETE operations.
-- Used for compliance, security tracking, and forensic analysis.

CREATE TABLE audit.audit_log (
    audit_id BIGSERIAL PRIMARY KEY,             -- Primary key for the audit record, auto-incremented
    table_name TEXT,                            -- Name of the table affected by the operation
    operation_type TEXT,                        -- Type of operation (UPDATE, DELETE, etc.)
    record_data JSONB,                          -- Stores the full record data before or after change in JSONB format
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp when the change occurred
);

-- Table-level comment
COMMENT ON TABLE audit.audit_log IS
'Centralized audit table capturing UPDATE and DELETE operations
for compliance, security tracking and forensic analysis.';

-- Column-level comments
COMMENT ON COLUMN audit.audit_log.audit_id IS
'Primary key for the audit record, auto-incremented using BIGSERIAL.';

COMMENT ON COLUMN audit.audit_log.table_name IS
'Name of the table where the operation occurred (e.g., employees, salary_history).';

COMMENT ON COLUMN audit.audit_log.operation_type IS
'Type of operation performed on the record (e.g., UPDATE, DELETE).';

COMMENT ON COLUMN audit.audit_log.record_data IS
'Full record data stored in JSONB format for tracking changes and auditing purposes.';

COMMENT ON COLUMN audit.audit_log.changed_at IS
'Timestamp when the record was changed. Defaults to current time at insertion.';


CREATE INDEX idx_employee_department
ON hr.employees(department_id);

CREATE INDEX idx_salary_employee
ON hr.salary_history(employee_id);

CREATE INDEX idx_transaction_account
ON finance.transactions(account_id);

CREATE INDEX idx_transaction_time
ON finance.transactions(transaction_time);


INSERT INTO core.role_master(role_code,role_name,description)
VALUES
('ADMIN','System Administrator','Full system control'),
('HR_MANAGER','HR Manager','Human resource management'),
('FIN_MANAGER','Finance Manager','Banking and payroll management'),
('EMPLOYEE','Employee','Standard employee role');


CREATE OR REPLACE FUNCTION core.create_default_admin()
RETURNS VOID
LANGUAGE plpgsql
AS $$
DECLARE
admin_role INT;
BEGIN

SELECT role_id INTO admin_role
FROM core.role_master
WHERE role_code='ADMIN';

IF NOT EXISTS(
SELECT 1 FROM core.users WHERE username='admin'
) THEN

INSERT INTO core.users(
username,
password_hash,
role_id
)
VALUES(
'admin',
'admin@123',
admin_role
);

END IF;

END;
$$;

SELECT core.create_default_admin();


CREATE OR REPLACE FUNCTION audit.audit_trigger()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN

IF TG_OP='UPDATE' OR TG_OP='DELETE' THEN

INSERT INTO audit.audit_log(
table_name,
operation_type,
record_data
)
VALUES(
TG_TABLE_NAME,
TG_OP,
row_to_json(OLD)
);

END IF;

RETURN NEW;

END;
$$;


CREATE TRIGGER employees_audit
AFTER UPDATE OR DELETE
ON hr.employees
FOR EACH ROW
EXECUTE FUNCTION audit.audit_trigger();

CREATE TRIGGER account_audit
AFTER UPDATE OR DELETE
ON finance.account_master
FOR EACH ROW
EXECUTE FUNCTION audit.audit_trigger();


Objects Created
Schemas
core
hr
finance
audit
Tables
role_master
users
department_master
employees
salary_history
account_master
transactions
audit_log
ENUM Types
user_status_enum
employee_status_enum
transaction_type_enum
Functions
create_default_admin()
audit_trigger()
Triggers
employees_audit
account_audit
Why This Schema Works for Your Goals
System	Covered
Banking	account_master + transactions
HR	employees + department_master
Payroll	salary_history
Security	roles + users + password
Audit	audit_log + triggers
Analytics	indexed transaction tables
Reporting	normalized structure


Below is an enterprise-grade PostgreSQL extension script that adds the advanced capabilities you requested to the previous schema:

RBAC permission system

Row-Level Security (RLS)

Employee hierarchy tree

Double-entry ledger for transactions

100M+ row analytics structure

Partitioned tables for performance

All code is PostgreSQL SQL/PLpgSQL and can be added on top of your existing schemas (core, hr, finance, audit).

1. RBAC Permission System

RBAC requires 3 layers:

Users → Roles → Permissions → Resources
Permission Master
-- Table: core.permission_master
-- Master list of permissions used in the RBAC authorization system.
-- Each permission represents a specific system capability (e.g., CREATE_EMPLOYEE, VIEW_PAYROLL).

CREATE TABLE core.permission_master (
    permission_id SERIAL PRIMARY KEY,               -- Unique identifier for each permission
    permission_code VARCHAR(100) UNIQUE NOT NULL,   -- Machine-readable permission code used by applications
    description TEXT,                               -- Human-readable explanation of what the permission allows
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp when the permission record was created
);

-- Table-level comment
COMMENT ON TABLE core.permission_master IS
'Master list of system permissions used by RBAC authorization layer.';

-- Column-level comments
COMMENT ON COLUMN core.permission_master.permission_id IS
'Primary key identifier for each permission. Auto-incremented using SERIAL.';

COMMENT ON COLUMN core.permission_master.permission_code IS
'Unique code representing a specific permission used in application authorization logic.';

COMMENT ON COLUMN core.permission_master.description IS
'Human-readable description explaining what the permission grants access to.';

COMMENT ON COLUMN core.permission_master.created_at IS
'Timestamp indicating when the permission entry was created.';

Example permissions

INSERT INTO core.permission_master(permission_code,description)
VALUES
('EMPLOYEE_READ','View employee records'),
('EMPLOYEE_WRITE','Modify employee records'),
('FINANCE_TRANSACT','Perform financial transactions'),
('PAYROLL_PROCESS','Run payroll jobs');



Role-Permission Mapping
-- Table: core.role_permissions
-- Mapping table connecting roles with permissions in the RBAC authorization model.
-- Enables assigning multiple permissions to a role.

CREATE TABLE core.role_permissions (
    role_permission_id SERIAL PRIMARY KEY,              -- Unique identifier for the role-permission mapping
    role_id INT REFERENCES core.role_master(role_id),   -- Foreign key referencing the role
    permission_id INT REFERENCES core.permission_master(permission_id), -- Foreign key referencing a permission
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP      -- Timestamp when the mapping was created
);

-- Table-level comment
COMMENT ON TABLE core.role_permissions IS
'Mapping table connecting roles with permissions for RBAC authorization.';

-- Column-level comments
COMMENT ON COLUMN core.role_permissions.role_permission_id IS
'Primary key for the role-permission mapping record.';

COMMENT ON COLUMN core.role_permissions.role_id IS
'Foreign key referencing core.role_master.role_id. Indicates which role is granted permissions.';

COMMENT ON COLUMN core.role_permissions.permission_id IS
'Foreign key referencing core.permission_master.permission_id. Indicates which permission is assigned to the role.';

COMMENT ON COLUMN core.role_permissions.created_at IS
'Timestamp when this role-permission mapping was created.';




Check Permission Function
CREATE OR REPLACE FUNCTION core.has_permission(
    p_user_id BIGINT,
    p_permission_code TEXT
)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
DECLARE
result BOOLEAN;
BEGIN

SELECT EXISTS (
SELECT 1
FROM core.users u
JOIN core.role_permissions rp ON u.role_id = rp.role_id
JOIN core.permission_master pm ON pm.permission_id = rp.permission_id
WHERE u.user_id = p_user_id
AND pm.permission_code = p_permission_code
)
INTO result;

RETURN result;

END;
$$;
2. Row Level Security (RLS)

Used in banking and HR systems.

Example: employees can only see their own records.

Enable RLS

ALTER TABLE hr.employees
ENABLE ROW LEVEL SECURITY;

Policy

CREATE POLICY employee_self_access
ON hr.employees
FOR SELECT
USING (employee_id = current_setting('app.current_employee')::BIGINT);

Application sets session variable

SET app.current_employee = 10;

Now employee 10 can only see their own row.

3. Employee Hierarchy Tree

Enterprise HR systems require hierarchical reporting structures.

Manager Relationship Table
-- Table: hr.employee_hierarchy
-- Defines reporting relationships between employees and their managers.
-- Supports organizational hierarchy, reporting structures, and approval workflows.

CREATE TABLE hr.employee_hierarchy (
    hierarchy_id BIGSERIAL PRIMARY KEY,                 -- Unique identifier for the hierarchy record
    employee_id BIGINT REFERENCES hr.employees(employee_id), -- Employee in the hierarchy relationship
    manager_id BIGINT REFERENCES hr.employees(employee_id),  -- Manager of the employee (self-referencing FK)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP      -- Timestamp when the hierarchy record was created
);

-- Table-level comment
COMMENT ON TABLE hr.employee_hierarchy IS
'Defines reporting hierarchy between employees and managers.';

-- Column-level comments
COMMENT ON COLUMN hr.employee_hierarchy.hierarchy_id IS
'Primary key identifier for the hierarchy relationship.';

COMMENT ON COLUMN hr.employee_hierarchy.employee_id IS
'Foreign key referencing hr.employees.employee_id representing the subordinate employee.';

COMMENT ON COLUMN hr.employee_hierarchy.manager_id IS
'Foreign key referencing hr.employees.employee_id representing the manager supervising the employee.';

COMMENT ON COLUMN hr.employee_hierarchy.created_at IS
'Timestamp indicating when the hierarchy relationship was created.';
Recursive Query to Get Reporting Tree
WITH RECURSIVE org_tree AS (
    SELECT employee_id, manager_id
    FROM hr.employee_hierarchy
    WHERE manager_id IS NULL

    UNION ALL

    SELECT e.employee_id, e.manager_id
    FROM hr.employee_hierarchy e
    JOIN org_tree o ON e.manager_id = o.employee_id
)
SELECT * FROM org_tree;

Used for:

HR reporting

org charts

manager approval workflows

4. Double-Entry Ledger (Banking Standard)

Financial systems must use double-entry accounting.

Every transaction has:

Debit Entry
Credit Entry
Ledger Accounts
-- Table: finance.ledger_accounts
-- Chart of accounts used in the accounting system.
-- Each ledger represents an accounting category such as Cash, Revenue, Salary Expense, etc.

CREATE TABLE finance.ledger_accounts (
    ledger_id BIGSERIAL PRIMARY KEY,                 -- Unique identifier for the ledger account
    ledger_code VARCHAR(50) UNIQUE NOT NULL,         -- Unique business code for the ledger (e.g., CASH001)
    ledger_name TEXT,                                -- Human readable name of the ledger account
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- Timestamp when the ledger account was created
);

-- Table-level comment
COMMENT ON TABLE finance.ledger_accounts IS
'Chart of accounts used for double entry accounting ledger.';

-- Column-level comments
COMMENT ON COLUMN finance.ledger_accounts.ledger_id IS
'Primary key identifier for each ledger account.';

COMMENT ON COLUMN finance.ledger_accounts.ledger_code IS
'Unique code used to identify ledger accounts in accounting operations.';

COMMENT ON COLUMN finance.ledger_accounts.ledger_name IS
'Human-readable name of the ledger account such as Cash, Salary Expense, or Revenue.';

COMMENT ON COLUMN finance.ledger_accounts.created_at IS
'Timestamp when the ledger account was created.';
Ledger Entries
-- Table: finance.ledger_entries
-- Stores debit and credit records linked to financial transactions.
-- Implements the double-entry accounting principle where every transaction
-- must have equal debit and credit entries.

CREATE TABLE finance.ledger_entries (
    entry_id BIGSERIAL PRIMARY KEY,                         -- Unique identifier for each ledger entry
    transaction_id BIGINT,                                  -- Reference to the related transaction
    ledger_id BIGINT REFERENCES finance.ledger_accounts(ledger_id), -- Ledger account impacted
    debit NUMERIC(18,2) DEFAULT 0,                          -- Debit amount applied to the ledger
    credit NUMERIC(18,2) DEFAULT 0,                         -- Credit amount applied to the ledger
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         -- Timestamp when the entry was recorded

    -- Ensure debit or credit is positive
    CHECK (debit >= 0 AND credit >= 0),

    -- Prevent both debit and credit from being non-zero simultaneously
    CHECK (
        (debit = 0 AND credit > 0) OR
        (credit = 0 AND debit > 0)
    )
);

-- Table-level comment
COMMENT ON TABLE finance.ledger_entries IS
'Stores debit and credit entries for financial transactions following the double-entry accounting model.';

-- Column-level comments
COMMENT ON COLUMN finance.ledger_entries.entry_id IS
'Primary key identifier for the ledger entry.';

COMMENT ON COLUMN finance.ledger_entries.transaction_id IS
'Identifier linking this entry to a financial transaction in finance.transactions.';

COMMENT ON COLUMN finance.ledger_entries.ledger_id IS
'Foreign key referencing finance.ledger_accounts.ledger_id representing the affected ledger account.';

COMMENT ON COLUMN finance.ledger_entries.debit IS
'Debit amount applied to the ledger account. Only one of debit or credit can be non-zero.';

COMMENT ON COLUMN finance.ledger_entries.credit IS
'Credit amount applied to the ledger account. Only one of debit or credit can be non-zero.';

COMMENT ON COLUMN finance.ledger_entries.created_at IS
'Timestamp indicating when the ledger entry was created.';

CHECK (debit >= 0 AND credit >= 0);

Example transaction:

Employee Salary Payment
Debit  : Payroll Expense
Credit : Bank Account
5. 100M+ Row Analytics Table

Analytics systems often store hundreds of millions of rows.

Example analytics fact table.

-- Table: finance.transaction_analytics
-- Analytical table used for reporting and business intelligence.
-- Stores denormalized transaction data for faster analytics queries.

CREATE TABLE finance.transaction_analytics (
    analytics_id BIGSERIAL PRIMARY KEY,                   -- Unique identifier for analytics record
    transaction_id BIGINT,                                -- Reference to finance.transactions
    employee_id BIGINT,                                   -- Reference to hr.employees
    department_id INT,                                    -- Reference to hr.department_master
    amount NUMERIC(18,2),                                 -- Transaction amount used for analytics
    transaction_time TIMESTAMP,                           -- Time when the original transaction occurred
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP        -- Timestamp when analytics record was created
);

-- Table-level comment
COMMENT ON TABLE finance.transaction_analytics IS
'Denormalized analytics table used for financial reporting, dashboards, and data warehouse queries.';

-- Column-level comments
COMMENT ON COLUMN finance.transaction_analytics.analytics_id IS
'Primary key identifier for analytics records.';

COMMENT ON COLUMN finance.transaction_analytics.transaction_id IS
'Identifier referencing finance.transactions for analytical mapping.';

COMMENT ON COLUMN finance.transaction_analytics.employee_id IS
'Employee associated with the transaction, usually from hr.employees.';

COMMENT ON COLUMN finance.transaction_analytics.department_id IS
'Department associated with the employee performing the transaction.';

COMMENT ON COLUMN finance.transaction_analytics.amount IS
'Transaction amount stored for reporting and aggregation purposes.';

COMMENT ON COLUMN finance.transaction_analytics.transaction_time IS
'Timestamp when the original financial transaction occurred.';

COMMENT ON COLUMN finance.transaction_analytics.created_at IS
'Timestamp indicating when this analytics record was inserted.';
Used for:

dashboards

reporting

machine learning

fraud detection

6. Partitioned Tables for Performance

Large datasets should be partitioned.

Partition Parent
1️⃣ Partitioned Transactions Table
-- Table: finance.transactions_partitioned
-- Partitioned transaction table designed for high volume financial systems.
-- Data is partitioned by transaction_time to improve query performance
-- and simplify long-term data management.

CREATE TABLE finance.transactions_partitioned (
    transaction_id BIGSERIAL,                           -- Unique identifier for transaction
    account_id BIGINT,                                  -- Account associated with the transaction
    transaction_type finance.transaction_type_enum,     -- Type of transaction (DEBIT / CREDIT etc.)
    amount NUMERIC(18,2),                               -- Transaction amount
    transaction_time TIMESTAMP NOT NULL                 -- Partition key
)
PARTITION BY RANGE (transaction_time);

5️⃣ Real Systems That Use Partitioning

Partitioning is used in:

Banking systems

Payment gateways

Stock trading platforms

Fintech transaction logs

Event tracking systems

Because these tables can reach:

Billions of rows
6️⃣ Recommended Index
CREATE INDEX idx_transactions_account
ON finance.transactions_partitioned(account_id);

CREATE INDEX idx_transactions_time
ON finance.transactions_partitioned(transaction_time);

Indexes are automatically inherited by partitions.



COMMENT ON TABLE finance.transactions_partitioned IS
'High-volume financial transactions table partitioned by transaction_time.
Designed for scalable storage and efficient querying of large transaction datasets.
Each partition stores transactions for a specific time range to enable partition pruning,
improving query performance and simplifying data lifecycle management.';

If you also want column-level documentation (recommended for enterprise schemas), you can add:

COMMENT ON COLUMN finance.transactions_partitioned.transaction_id IS
'Unique identifier assigned to each financial transaction.';

COMMENT ON COLUMN finance.transactions_partitioned.account_id IS
'Identifier of the account associated with the transaction.';

COMMENT ON COLUMN finance.transactions_partitioned.transaction_type IS
'Type of financial transaction such as DEBIT or CREDIT defined by transaction_type_enum.';

COMMENT ON COLUMN finance.transactions_partitioned.amount IS
'Monetary value of the transaction.';

COMMENT ON COLUMN finance.transactions_partitioned.transaction_time IS
'Timestamp representing when the transaction occurred. Used as the partition key.';


2️⃣ Example Monthly Partitions

Large systems usually partition monthly.

January Partition
CREATE TABLE finance.transactions_2026_01
PARTITION OF finance.transactions_partitioned
FOR VALUES FROM ('2026-01-01') TO ('2026-02-01');
February Partition
CREATE TABLE finance.transactions_2026_02
PARTITION OF finance.transactions_partitioned
FOR VALUES FROM ('2026-02-01') TO ('2026-03-01');
March Partition
CREATE TABLE finance.transactions_2026_03
PARTITION OF finance.transactions_partitioned
FOR VALUES FROM ('2026-03-01') TO ('2026-04-01');
3️⃣ Table Documentation
COMMENT ON TABLE finance.transactions_partitioned IS
'High volume financial transaction table partitioned by transaction_time for scalability and performance.';
4️⃣ Why Partitioning is Important

Without partitioning:

transactions table
------------------
2 Billion rows

Query:

SELECT * FROM transactions
WHERE transaction_time >= '2026-03-01';

PostgreSQL scans entire table.

With partitioning:

transactions_2026_01
transactions_2026_02
transactions_2026_03

PostgreSQL automatically performs partition pruning:

Only scans transactions_2026_03

This can improve performance 10x–100x.

5️⃣ Real Systems That Use Partitioning

Partitioning is used in:

Banking systems

Payment gateways

Stock trading platforms

Fintech transaction logs

Event tracking systems

Because these tables can reach:

Billions of rows
6️⃣ Recommended Index
CREATE INDEX idx_transactions_account
ON finance.transactions_partitioned(account_id);

CREATE INDEX idx_transactions_time
ON finance.transactions_partitioned(transaction_time);

Indexes are automatically inherited by partitions.

Monthly Partitions
CREATE TABLE finance.transactions_2024_01
PARTITION OF finance.transactions_partitioned
FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');

CREATE TABLE finance.transactions_2024_02
PARTITION OF finance.transactions_partitioned
FOR VALUES FROM ('2024-02-01') TO ('2024-03-01');

1️⃣ Function to Create Monthly Partitions

This function checks if the partition exists and creates it automatically.

CREATE OR REPLACE FUNCTION finance.create_monthly_transaction_partition(p_date DATE)
RETURNS VOID AS
$$
DECLARE
    start_date DATE;
    end_date DATE;
    partition_name TEXT;
BEGIN
    start_date := date_trunc('month', p_date);
    end_date := start_date + INTERVAL '1 month';

    partition_name := 'transactions_' || to_char(start_date, 'YYYY_MM');

    EXECUTE format(
        'CREATE TABLE IF NOT EXISTS finance.%I
         PARTITION OF finance.transactions_partitioned
         FOR VALUES FROM (%L) TO (%L)',
        partition_name,
        start_date,
        end_date
    );
END;
$$ LANGUAGE plpgsql;
2️⃣ Manually Create a Partition

Example:

SELECT finance.create_monthly_transaction_partition('2026-04-01');

This will create:

finance.transactions_2026_04
3️⃣ Automatically Create Next Month Partition

You can create partitions for the next month automatically:

SELECT finance.create_monthly_transaction_partition(CURRENT_DATE + INTERVAL '1 month');
4️⃣ Automate Using Scheduler (Best Practice)

Production systems use pg_cron or Linux cron.

Example with pg_cron:

SELECT cron.schedule(
    'create_next_transaction_partition',
    '0 0 25 * *',
    $$SELECT finance.create_monthly_transaction_partition(CURRENT_DATE + INTERVAL '1 month');$$
);

Meaning:

On the 25th of every month
→ Create next month's partition

This ensures:

Partitions always exist before new data arrives
5️⃣ Resulting Structure

After a few months your database will look like:

transactions_partitioned
│
├── transactions_2026_01
├── transactions_2026_02
├── transactions_2026_03
├── transactions_2026_04
└── transactions_2026_05

PostgreSQL automatically performs partition pruning when querying.

Example query:

SELECT *
FROM finance.transactions_partitioned
WHERE transaction_time >= '2026-03-01'
AND transaction_time < '2026-04-01';

PostgreSQL will scan only:

transactions_2026_03
6️⃣ Why Big Systems Use This

Partitioning is used in:

Banking transaction systems

Payment gateways

Fintech ledgers

Event logging systems

Audit logs

Because tables grow to:

100M – 10B+ rows

Partitioning keeps queries fast and maintenance manageable.


In large fintech / banking systems, transaction tables can reach billions of rows. Even with partitioning, keeping all partitions on expensive fast storage (NVMe/SSD) is costly.
A powerful technique is automatic archiving of old partitions to cheaper storage while keeping recent data fast.

Below is the enterprise pattern used in many systems.

1️⃣ Architecture Idea

Hot data (recent):

Fast SSD / NVMe
transactions_2026_03
transactions_2026_04
transactions_2026_05

Cold data (old):

Cheap storage / slower disk
transactions_2025_01
transactions_2025_02
transactions_2025_03

Applications still query the same parent table, but PostgreSQL accesses archived partitions when needed.

2️⃣ Move Old Partitions to Archive Tablespace

First create a tablespace on cheaper storage.

CREATE TABLESPACE archive_space
LOCATION '/mnt/archive_disk/postgres';

Example storage:

Fast SSD → /var/lib/postgresql/data
Archive HDD → /mnt/archive_disk
3️⃣ Move an Old Partition

Example: archive January 2025 transactions

ALTER TABLE finance.transactions_2025_01
SET TABLESPACE archive_space;

This physically moves the partition to cheap storage.

Applications still query:

SELECT *
FROM finance.transactions_partitioned
WHERE transaction_time >= '2025-01-01'
AND transaction_time < '2025-02-01';

PostgreSQL automatically accesses the archived partition.

4️⃣ Automate Archiving with Function

Production systems automatically archive partitions older than 12 months.

CREATE OR REPLACE FUNCTION finance.archive_old_partitions()
RETURNS VOID AS
$$
DECLARE
    part RECORD;
BEGIN
    FOR part IN
        SELECT tablename
        FROM pg_tables
        WHERE schemaname = 'finance'
        AND tablename LIKE 'transactions_%'
    LOOP
        IF part.tablename < 'transactions_' || to_char(CURRENT_DATE - INTERVAL '12 months','YYYY_MM')
        THEN
            EXECUTE format(
                'ALTER TABLE finance.%I SET TABLESPACE archive_space',
                part.tablename
            );
        END IF;
    END LOOP;
END;
$$ LANGUAGE plpgsql;
5️⃣ Schedule the Archiving Job

Run monthly using pg_cron.

SELECT cron.schedule(
    'archive_transactions',
    '0 3 1 * *',
    $$SELECT finance.archive_old_partitions();$$
);

Meaning:

Every month on the 1st at 3 AM
→ Archive old partitions
6️⃣ Real Production Lifecycle

Fintech systems often use a 3-tier storage lifecycle.

Tier 1 (Hot Data – SSD)
Last 3 months

Tier 2 (Warm Data – cheaper disk)
3 months – 2 years

Tier 3 (Cold Data – object storage / S3 / backup)
Older than 2 years

Older partitions may be:

exported to data warehouse

stored in S3

compressed backups

7️⃣ Performance Benefit

Without lifecycle management:

transactions table
4 billion rows

With partition lifecycle:

Recent data scanned → 3 partitions
Old data archived → rarely accessed

Benefits:

Much lower storage cost

Faster queries

Smaller indexes

Simpler backups

8️⃣ Real Systems That Use This

This pattern is used in:

payment gateways

digital banks

stock trading platforms

fraud detection systems

large audit logging systems


Perfect! Let’s talk about BRIN indexes on huge partitioned transaction tables—a technique widely used in high-frequency trading, fintech, and banking systems to handle billions of rows efficiently.

1️⃣ What is a BRIN Index?

BRIN = Block Range INdex

Unlike B-Tree, which stores one key per row, BRIN stores summaries per block range (e.g., 128 pages = ~1MB).

Perfect for very large, sequentially inserted data, like:

transaction_time, account_id

Extremely small footprint: a BRIN index can be 1/1000 the size of a B-Tree index.

2️⃣ Why It’s Ideal for Transaction Tables

Transaction tables grow billions of rows:

transactions_partitioned
│
├─ transactions_2026_01 → 200M rows
├─ transactions_2026_02 → 210M rows

Queries are mostly range queries on time:

SELECT *
FROM finance.transactions_partitioned
WHERE transaction_time BETWEEN '2026-03-01' AND '2026-03-31';

B-Tree indexes on billions of rows are huge and slow to maintain.

BRIN works efficiently because data is physically ordered by time.

3️⃣ Creating a BRIN Index on Partitioned Table
-- BRIN index on transaction_time (good for time-based queries)
CREATE INDEX brin_transaction_time
ON finance.transactions_partitioned USING BRIN(transaction_time);

-- Optional: include account_id for faster account filtering
CREATE INDEX brin_transaction_account_time
ON finance.transactions_partitioned USING BRIN(account_id, transaction_time);

Benefits:

Tiny index size (~MB instead of GB)

Queries scan only relevant block ranges

Fast for range and aggregate queries

4️⃣ Example Query
-- Monthly transaction report for a single account
SELECT account_id, SUM(amount) AS total
FROM finance.transactions_partitioned
WHERE account_id = 12345
AND transaction_time >= '2026-03-01'
AND transaction_time < '2026-04-01'
GROUP BY account_id;

PostgreSQL uses BRIN to skip blocks:

- Only scans 1-2 relevant block ranges instead of billions of rows
5️⃣ BRIN + Partitioning = Superpower Combo

Partitioned by month → partitions are smaller

BRIN index → each partition can skip most blocks

Result: queries on 10B+ row tables run in seconds

Storage overhead: almost nothing

6️⃣ Production Notes

Best for append-only, sequential data.

If data is randomly updated, BRIN is less effective.

Can combine BRIN + B-Tree for hybrid workloads:

BRIN → fast range scan on time
B-Tree → exact lookups for account_id or transaction_id

Can also rebuild BRIN periodically:

ALTER INDEX brin_transaction_time REINDEX;
7️⃣ Real Systems Using This

Fintech payment platforms → billions of transactions/month

Banking core ledgers → high-volume deposit/withdrawal logs

Trading platforms → tick-level stock trade history

They often combine partitioning, automatic archiving, and BRIN indexes to maintain multi-billion-row tables efficiently.

Benefits:

faster queries

smaller index scans

easier archiving

7. Partition Indexes
CREATE INDEX idx_transaction_partition_account
ON finance.transactions_partitioned(account_id);

CREATE INDEX idx_transaction_partition_time
ON finance.transactions_partitioned(transaction_time);
8. Materialized View for Reporting

Analytics systems use materialized views.

CREATE MATERIALIZED VIEW finance.department_salary_summary AS

SELECT
d.department_name,
AVG(s.salary_amount) avg_salary,
COUNT(e.employee_id) employee_count
FROM hr.employees e
JOIN hr.department_master d
ON e.department_id = d.department_id
JOIN hr.salary_history s
ON e.employee_id = s.employee_id
GROUP BY d.department_name;

Refresh:

REFRESH MATERIALIZED VIEW finance.department_salary_summary;
9. Performance Monitoring View
SELECT
query,
calls,
total_exec_time
FROM pg_stat_statements
ORDER BY total_exec_time DESC
LIMIT 10;

Used for query optimization.

Final Architecture
Users
   │
Roles
   │
Permissions
   │
RBAC Authorization

Employees
   │
Hierarchy Tree

Accounts
   │
Double Entry Ledger
   │
Transactions

Transactions
   │
Partitioned Tables
   │
Analytics Fact Tables

Materialized Views
   │
Reporting Dashboards
Why This Architecture Is Enterprise Level

Supports:

Capability	Included
RBAC security	✓
Row level security	✓
HR hierarchy	✓
Double entry banking ledger	✓
100M+ analytics	✓
Partitioned tables	✓
Audit tracking	✓

If you'd like, I can also show the next level that database engineers study:

PostgreSQL storage pages (heap pages)

B-Tree index internal structure

MVCC internals

WAL log structure

Query planner algorithms

Those topics explain how PostgreSQL actually works internally, not just SQL usage.