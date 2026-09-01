drop database if exists bookclub_test;
create database bookclub_test;
use bookclub_test;

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

create table nominations (
	nomination_id int primary key auto_increment,
	user_id int,
	title text,
	author text,
	genre text,
	constraint fk_user_id
		foreign key (user_id)
		references user (user_id)
);

create table votes (
	vote_id int primary key auto_increment,
	user_id int,
	nomination_id int,
	score int,
	constraint fk_vote_user_id
		foreign key (user_id)
		references user (user_id),
	constraint fk_nomination_id
		foreign key (nomination_id)
		references nominations (nomination_id) on delete cascade,
	constraint uq_user_nomination
		unique (user_id, nomination_id)
);

create table favorites (
	favorite_id int primary key auto_increment,
	user_id int,
	book_id int,
	constraint fk_favorite_user_id
		foreign key (user_id)
		references user (user_id) on delete cascade,
	constraint fk_favorite_book_id
		foreign key (book_id)
		references books (book_id) on delete cascade,
	constraint uq_user_book
		unique (user_id, book_id)
);


delimiter //
create procedure set_known_good_state()
begin
delete from favorites;
alter table favorites auto_increment = 1;
delete from votes;
alter table votes auto_increment = 1;
delete from nominations;
alter table nominations auto_increment = 1;
delete from user;
alter table user auto_increment = 1;
delete from meetings;
alter table meetings auto_increment = 1;
delete from books;
alter table books auto_increment = 1;

insert into user (username, password) values
                                       ("a", "128"),
                                       ("b", "129");
insert into books (title, author, genre, when_read, link, img_link) values 
	("Princess of Mars", 
		"Edgar Rice Burroughs", 
		"Science Fiction", 
		'2026-07-16', 
		"https://www.goodreads.com/en/book/show/40395.A_Princess_of_Mars",
		"https://upload.wikimedia.org/wikipedia/commons/0/03/Princess_of_Mars.jpg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original");
insert into meetings (book_id, reading_goal, meeting_date, meeting_notes) values 
		(1,
		"chs 1-12",
		'2026-07-16',
		"start the barsoom trilogy, can be found in public domain");
insert into nominations (user_id, title, author, genre) values (
	1,
	"Green City Wars",
	"Adrian Tchaikovsky",
	"Science Fiction"
);
insert into votes(user_id, nomination_id, score) values (
	2,
	1,
	3
);
insert into favorites (user_id, book_id) values
	(1, 1),
	(2, 1);

end //
delimiter ;