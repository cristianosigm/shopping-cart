-- drop database if exists csdb;
-- create database csdb;
-- use csdb;

-- create user shoppingadm with password 'obiwan';

create schema shopping_cart;
create schema purchase_order;
create schema product;

grant all privileges on database csdb to shoppingadm;
grant all privileges on schema product to shoppingadm;