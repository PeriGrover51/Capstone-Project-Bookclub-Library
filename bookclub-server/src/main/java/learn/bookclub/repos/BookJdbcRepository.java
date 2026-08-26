package learn.bookclub.repos;

import learn.bookclub.models.Book;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookJdbcRepository implements BookRepository{

    private final JdbcClient jdbcClient;

    public BookJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Book> findAll() {
        return jdbcClient.sql("Select * from books")
                .query(Book.class)
                .list();
    }

    @Override
    public Book findById(int bookId) {
        return jdbcClient.sql("Select * from books where book_id = ?")
                .param(bookId)
                .query(Book.class)
                .optional().orElse(null);
    }

    @Override
    public Book create(Book book) {
        return null;
    }

    @Override
    public boolean update(Book book) {
        return false;
    }

    @Override
    public boolean deleteById(int bookId) {
        return false;
    }
}
