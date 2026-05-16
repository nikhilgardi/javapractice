create table user_type_master
(
	user_type_id Serial PRIMARY KEY,
	user_type_code varchar(30) unique NOT NULL,
	description varchar(100) 
);

comment on table user_type_master is 'Master table for system user role';
comment on column user_type_master.user_type_id is 'Primary key for user role';
comment on column user_type_master.user_type_code is 'User Type Code ie. Admin, Receptionist';
comment on column user_type_master.description is 'Description of user role';

insert into user_type_master (user_type_code,description) VALUES ('ADMIN','Clinic administrator');
insert into user_type_master (user_type_code,description) VALUES ('RECEPTIONIST','Clinic receptionist');

select * from user_type_master

Explain select * from user_type_master

EXPLAIN ANALYZE SELECT * FROM user_type_master;

EXPLAIN (ANALYZE, FORMAT JSON)
SELECT * FROM user_type_master;

EXPLAIN (ANALYZE, VERBOSE, BUFFERS)
SELECT * FROM user_type_master;
