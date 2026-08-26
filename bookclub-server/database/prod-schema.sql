drop database if exists bookclub;
create database bookclub;
use bookclub;

create table user (
                      user_id int primary key auto_increment,
                      username text,
                      password text
);

create table books (
	book_id int primary key auto_increment,
	title text,
	author text,
	genre text,
	when_read date,
	link text,
	img_link text
);

create table meetings (
	meeting_id int primary key auto_increment,
	book_id int,
	reading_goal text,
	meeting_date date,
	meeting_notes text,
	constraint fk_book_id
		foreign key (book_id)
		references books (book_id)
);