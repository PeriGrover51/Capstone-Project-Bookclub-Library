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

select * from meetings m join books b on m.book_id = b.book_id;