--Database:emp_mgmt

CREATE DATABASE IF NOT EXISTS emp_mgmt;

create TYPE office_type_enum as ENUM('HOD','Region','Branch')


CREATE TABLE offices (
    office_id SERIAL PRIMARY KEY,                               
    name VARCHAR(100) NOT NULL,                                  
    office_category office_type_enum NOT NULL,                   
    parent_office_id INT REFERENCES offices(office_id)          
        ON DELETE SET NULL                                      
);

insert into offices(name,office_category,parent_office_id) values('Western Regtion','HOD',NULL)

select * from offices;

-- Comments
COMMENT ON TABLE offices IS 'Stores office information with hierarchical structure (HOD -> Region -> Branch)';
COMMENT ON COLUMN offices.office_id IS 'Unique ID of Office';
COMMENT ON COLUMN offices.name IS 'Name of office';
COMMENT ON COLUMN offices.office_category IS 'Office Type (HOD, Region, Branch)';
COMMENT ON COLUMN offices.parent_office_id IS 'Parent office ID (HOD for Region, Region for Branch)';


create table departments(
	department_id serial primary key,
	department_name varchar(100) NOT NULL,
	location varchar(100),
	created_at timestamp default current_timestamp
);
comment on table departments IS 'Stores all department details including name and location';
comment on column departments.department_id IS 'Unique ID of the department';
comment on column departments.department_name IS 'Name of the department';
comment on column departments.location IS 'Location of the department';
comment on column departments.created_at IS 'Recored creation timestamp';


create table employees
(
	emp_id bigserial primary key,
	password VARCHAR(255) NOT NULL, 
	first_name varchar(50),
	last_name varchar(50),
	email varchar(100) unique,
	doj date NULL,
	dob date NULL,
	hire_date date NOT NULL,
	department_id int NULL references departments(department_id),
	salary numeric(10,2) default 0.00,
	is_active boolean default true,
	created_at timestamp default current_timestamp,
	created_by bigint references employees(emp_id) ON DELETE RESTRICT
);

comment on table employees IS 'Stores employee details including personal info, departments, salary';
comment on column employees.emp_id IS 'Unique ID of the employee';
comment ON column employees.password IS 'Hashed password';
comment on column employees.first_name IS 'Firstname of the employee';
comment on column employees.last_name IS 'Lastname of the employee';
comment on column employees.email IS 'Email of the employee';
comment on column employees.doj IS 'Employee date of joining';
comment on column employees.dob IS 'Employee date of birth';
comment on column employees.hire_date IS 'Date employee was hired';
comment on column employees.department_id IS 'Foreign Key referencing department';
comment on column employees.salary IS 'Employee Monthly Salary';
comment on column employees.is_active IS 'Flag indicating if employee is currently active or deactive';
comment on column employees.created_at IS 'Recored creation timestamp';
comment on column employees.created_by IS 'Recored created by another employee';

--First, enable the extension in your database:

CREATE EXTENSION IF NOT EXISTS pgcrypto;


--You need superuser privileges to run this.

--Once enabled, crypt() and gen_salt() become available.
	


select * from employees;




create TYPE employee_role_enum as ENUM('System','SuperAdmin','Admin','VendorService','VendorCanteen');


create table employee_roles(
		role_id bigserial primary key,
		role_name employee_role_enum NOT NULL,
		employee_id bigint references employees(emp_id) on delete cascade
);

comment on table employee_roles IS 'Storing employee role';
comment on column employee_roles.role_id IS 'Unique id for role';
comment on column employee_roles.role_name IS 'Role Type (System,SuperAdmin,Admin,VendorService,VendorCanteen)';


--Do Block
DO $$
	Declare
		sys_emp_id BIGINT;
	BEGIN		
		select employee_id into sys_emp_id from employee_roles where role_name='System'::employee_role_enum limit 1;
		IF sys_emp_id IS NULL THEN
			INSERT INTO employees(
			    password, first_name, last_name, email, hire_date, salary, is_active, created_by
			)
			VALUES (
			    crypt('Welcome123', gen_salt('bf')),
			    'System', 'Admin',
			    'system@company.com',
			    CURRENT_DATE,
			    0.00,
			    TRUE,
			    NULL
			)

			returning emp_id into sys_emp_id;

			INSERT INTO employee_roles(role_name,employee_id) 
				values ('System'::employee_role_enum,sys_emp_id);
		END IF;
	END
$$;




select * from employee_roles where role_name='System'::employee_role_enum limit 1;


select * from employees


create table audit_log
(
	audit_id bigserial primary key,
	table_name text not null,
	operation varchar(10) not null,
	changed_by bigint references employees(emp_id) on delete set null,
	changed_at timestamp default current_timestamp not null,
	old_data JSONB,
	new_data JSONB
);
comment on table audit_log IS 'Stores audit history for all changes in tracked tables';
comment on column audit_log.audit_id IS 'Unique id of the table';
comment on column audit_log.table_name IS 'Table name where changes occured';
comment on column audit_log.operation IS 'Types of operation:INSERT,UPDATE,DELETE';
comment on column audit_log.changed_by IS 'USER ID who made the operation';
comment on column audit_log.changed_at IS 'Timstamp of change';
comment on column audit_log.old_data IS 'Previous data before operation';
comment on column audit_log.new_data IS 'New Data after operation';


create or replace function audit_trigger_fn() returns trigger as $$
begin
	if TG_OP='INSERT' THEN
		Insert into audit_log(table_name,operation,changed_by,new_data)
		VALUES (TG_TABLE_NAME,TG_OP,current_setting('app.current_user_id',true)::INT,row_to_json(NEW));
		RETURN NEW;
	elseif TG_OP='UPDATE' THEN
		Insert into audit_log(table_name,operation,changed_by,old_data,new_data)
		VALUES (TG_TABLE_NAME,TG_OP,current_setting('app.current_user_id',true)::INT,row_to_json(OLD),row_to_json(NEW));
		RETURN NEW;
	elseif TG_OP='DELETE' THEN
		Insert into audit_log(table_name,operation,changed_by,old_data)
		VALUES (TG_TABLE_NAME,TG_OP,current_setting('app.current_user_id',true)::INT,row_to_json(OLD));
		RETURN OLD;
	end if;

end;

$$ language plpgsql;



create trigger office_audit
after insert or update or delete on offices
for each row execute function audit_trigger_fn();

create trigger departments_audit
after insert or update or delete on departments
for each row execute function audit_trigger_fn();

create trigger employees_audit
after insert or update or delete on employees
for each row execute function audit_trigger_fn();

create trigger employee_roles_audit
after insert or update or delete on employee_roles
for each row execute function audit_trigger_fn();

select * from audit_log




Sure! Let’s go step by step and explain REFERENCES ... ON DELETE behavior in PostgreSQL, along with simple examples for each type.

1️⃣ Background

When you create a foreign key in PostgreSQL, you are linking one table’s column to another table’s primary key (or unique key).

FOREIGN KEY (child_column) REFERENCES parent_table(parent_column)


The ON DELETE clause defines what happens to the child table when a row in the parent table is deleted.

2️⃣ Types of ON DELETE
Action	Behavior	Example
RESTRICT	Prevents deletion in parent table if any child row exists.	You cannot delete department if any employee belongs to it.
CASCADE	Automatically deletes all child rows when parent is deleted.	Deleting a department will delete all employees in that department.
SET NULL	Sets the foreign key column in child to NULL.	Deleting a department will set department_id in employees to NULL.
SET DEFAULT	Sets the foreign key column in child to its default value.	If department_id default is 1, deleting department 2 will set it to 1 for employees.
NO ACTION	Similar to RESTRICT but checks at end of statement/transaction.	Works like RESTRICT, but allows multi-step operations in a transaction.

Note: RESTRICT is checked immediately, NO ACTION is deferred until the end of the statement/transaction.

3️⃣ Simple Example
Parent Table
CREATE TABLE departments (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(50)
);

Child Table Examples
a) RESTRICT
CREATE TABLE employees_restrict (
    emp_id SERIAL PRIMARY KEY,
    emp_name VARCHAR(50),
    department_id INT REFERENCES departments(department_id) ON DELETE RESTRICT
);


Try to delete a department with employees → ❌ fails.

b) CASCADE
CREATE TABLE employees_cascade (
    emp_id SERIAL PRIMARY KEY,
    emp_name VARCHAR(50),
    department_id INT REFERENCES departments(department_id) ON DELETE CASCADE
);


Delete a department → ✅ all employees in that department also deleted automatically.

c) SET NULL
CREATE TABLE employees_setnull (
    emp_id SERIAL PRIMARY KEY,
    emp_name VARCHAR(50),
    department_id INT REFERENCES departments(department_id) ON DELETE SET NULL
);


Delete a department → ✅ employees remain, but department_id becomes NULL.

d) SET DEFAULT
CREATE TABLE employees_setdefault (
    emp_id SERIAL PRIMARY KEY,
    emp_name VARCHAR(50),
    department_id INT DEFAULT 1 REFERENCES departments(department_id) ON DELETE SET DEFAULT
);


Delete a department → ✅ employees remain, department_id becomes default 1.

e) NO ACTION
CREATE TABLE employees_noaction (
    emp_id SERIAL PRIMARY KEY,
    emp_name VARCHAR(50),
    department_id INT REFERENCES departments(department_id) ON DELETE NO ACTION
);


Behaves like RESTRICT for a single statement, but allows deferred multi-step transactions.

4️⃣ Notes

If you want safe deletion, usually SET NULL or RESTRICT is preferred.

If you want automatic cleanup, CASCADE is useful.

SET DEFAULT is rarely used unless you have a sensible default.

If you want, I can draw a quick table diagram showing departments → employees for each ON DELETE type, so you can visualize what happens.

Do you want me to do that?