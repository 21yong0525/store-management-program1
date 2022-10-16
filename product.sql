drop database if exists productdb;
drop table if exists product;

create database if not exists productdb;

create database productdb;

use productdb;

create table product(
    no char(3) not null primary key,
    name char(9) not null,
    kind char(2) not null,
    price mediumint unsigned not null,
    stock smallint not null,
    date char(10) not null
);

