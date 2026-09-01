use bookclub;

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

insert into votes (user_id, nomination_id, score) values 
	(2, 1, 3),
	(1, 2, 4);


insert into favorites (user_id, book_id) values 
	(1, 1),
	(2, 2);



select * from favorites join books on favorites.book_id = books.book_id;

select b.book_id, title, author, genre, when_read, link, img_link from books b join favorites on b.book_id = favorites.book_id;

select meeting_id, reading_goal, meeting_date, meeting_notes, b.book_id, title, author, genre, when_read, link, img_link
	from meetings m join books b 
	on m.book_id = b.book_id;

select v.vote_id, v.score, voter.user_id as voter_id, voter.username as voter_username, n.nomination_id, n.title, n.author, n.genre, nominator.user_id as nominator_id, nominator.username as nominator_username
	from votes v join user voter on v.user_id = voter.user_id
	join nominations n on v.nomination_id = n.nomination_id
	join user nominator on n.user_id = nominator.user_id;

