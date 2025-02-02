-- removing current database
-- ----------------------------------------- 
-- drop user if exists csdbadm;
-- drop database if exists csdb;

-- creating database and schemas
-- -----------------------------------------
-- create database csdb;

-- creating main user
create user csdbadm with password 'obiwan';

-- grant access
grant all privileges on database csdb to csdbadm;

-- creating schema
-- drop schema if exists shopping;
create schema shopping;

-- creating user
drop user if exists shoppingadm;
create user shoppingadm with password 'obiwan';

grant all privileges on schema shopping to shoppingadm;
