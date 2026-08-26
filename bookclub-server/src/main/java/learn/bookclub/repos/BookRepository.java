package learn.bookclub.repos;

import learn.bookclub.models.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    Book findById(int bookId);

    Book create(Book book);

    boolean update(Book book);

    boolean deleteById(int bookId);
}
