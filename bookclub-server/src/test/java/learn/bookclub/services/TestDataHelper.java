package learn.bookclub.services;

import learn.bookclub.models.Book;

import java.util.Date;

public class TestDataHelper {
    public static Book existingBook() {
        return new Book(1,
                "Princess of Mars",
                "Edgar Rice Burroughs",
                "Science Fiction",
                new Date(2026, 7, 16),
                "https://www.goodreads.com/en/book/show/40395.A_Princess_of_Mars",
                "https://upload.wikimedia.org/wikipedia/commons/0/03/Princess_of_Mars.jpg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original");
    }
}
