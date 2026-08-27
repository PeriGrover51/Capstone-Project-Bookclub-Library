package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.models.Meeting;
import learn.bookclub.models.Nomination;
import learn.bookclub.models.User;

import java.time.LocalDate;
//import java.util.Date;

public class TestDataHelper {
    public static Book existingBook() {
        return new Book(1,
                "Princess of Mars",
                "Edgar Rice Burroughs",
                "Science Fiction",
                LocalDate.of(2026, 7, 16),
                "https://www.goodreads.com/en/book/show/40395.A_Princess_of_Mars",
                "https://upload.wikimedia.org/wikipedia/commons/0/03/Princess_of_Mars.jpg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original");
    }

    public static Meeting existingMeeting() {
        return new Meeting(
                1,
                "chs 1-12",
                LocalDate.of(2026, 7, 16),
                "start the barsoom trilogy, can be found in public domain",
                existingBook()
        );
    }

    public static Book bookToCreate() {
        return new Book(0,
                "Gods of Mars",
                "Edgar Rice Burroughs",
                "Science Fiction",
                LocalDate.of(2026, 7, 30),
                "https://www.goodreads.com/en/book/show/841973.The_Gods_of_Mars",
                "https://upload.wikimedia.org/wikipedia/commons/1/19/Gods_of_Mars-1918.jpg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original");
    }

    public static Book bookToUpdate() {
        return new Book(1,
                "UPDATE",
                "Edgar Rice Burroughs",
                "Science Fiction",
                LocalDate.of(2026, 7, 16),
                "https://www.goodreads.com/en/book/show/40395.A_Princess_of_Mars",
                "https://upload.wikimedia.org/wikipedia/commons/0/03/Princess_of_Mars.jpg?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=original");
    }

    public static Meeting meetingToCreate() {
        return new Meeting(
                0,
                "chs 12-24",
                LocalDate.of(2026, 7, 23),
                "finish second half of Princess of Mars",
                existingBook()
        );
    }

    public static Meeting meetingToUpdate() {
        return new Meeting(
                1,
                "UPDATE",
                LocalDate.of(2026, 7, 23),
                "start the barsoom trilogy, can be found in public domain",
                existingBook()
        );
    }

    public static User existingUser() {
        return new User(
                1,
                "a",
                null
        );
    }

    public static Nomination existingNomination() {
        return new Nomination(
                1,
                "Green City Wars",
                "Adrian Tchaikovsky",
                "Science Fiction",
                existingUser()
        );
    }
}
