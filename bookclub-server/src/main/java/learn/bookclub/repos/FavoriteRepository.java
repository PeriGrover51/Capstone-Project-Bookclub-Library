package learn.bookclub.repos;

import learn.bookclub.models.Book;

import java.util.List;

public interface FavoriteRepository {
    List<Book> findByUser(int userId);

    boolean create(int userId, int bookId);

    boolean deleteById(int userId, int bookId);
}
