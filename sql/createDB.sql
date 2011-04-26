drop database if exists dvrm;
create database dvrm;

use dvrm;

drop table if exists tasks;
create table tasks(
	id int primary key not null auto_increment,
	uuid varchar(80),
	cpu int,
	mem int, 
	hdd int
	);
	
drop table if exists history;
create table history(
	id int primary key not null auto_increment,
	uuid varchar(80),
	cpu int,
	mem int, 
	hdd int
	);
