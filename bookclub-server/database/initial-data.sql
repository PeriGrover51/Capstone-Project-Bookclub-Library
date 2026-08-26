use bookclub;

insert into books (title, author, genre, when_read, link, img_link) values 
	("Princess of Mars", 
		"Edgar Rice Burroughs", 
		"Science Fiction", 
		'2026-07-16', 
		"https://www.goodreads.com/en/book/show/40395.A_Princess_of_Mars",
		"https://upload.wikimedia.org/wikipedia/commons/0/03/Princess_of_Mars.jpg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original");

select * from books;