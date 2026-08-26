package learn.bookclub.repos;

import learn.bookclub.models.Book;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        final String sql = """
                insert into books (title, author, genre, when_read, link, img_link) values (
                :title, :author, :genre, :when_read, :link, :img_link
                );""";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("title", book.getTitle())
                .param("author", book.getAuthor())
                .param("genre", book.getGenre())
                .param("when_read", book.getWhenRead())
                .param("link", book.getLink())
                .param("img_link", book.getImgLink())
                .update(keyHolder, "book_id");

        if (rowsAffected == 0) {
            return null;
        }

        book.setBookId(keyHolder.getKey().intValue());

        return book;
    }

    @Override
    public boolean update(Book book) {
        final String sql = """
                update books set 
                title = :title,
                author = :author,
                genre = :genre,
                when_read = :when_read,
                link = :link,
                img_link = :img_link
                where book_id = :book_id 
                ;""";

        int rowsUpdated = jdbcClient.sql(sql)
                .param("title", book.getTitle())
                .param("author", book.getAuthor())
                .param("genre", book.getGenre())
                .param("when_read", book.getWhenRead())
                .param("link", book.getLink())
                .param("img_link", book.getImgLink())
                .param("book_id", book.getBookId())
                .update();

        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteById(int bookId) {
        return false;
    }
}
