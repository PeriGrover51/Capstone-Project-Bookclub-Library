package learn.bookclub.repos;

import learn.bookclub.models.Book;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FavoriteJdbcClientRepository implements FavoriteRepository {

    private final JdbcClient jdbcClient;

    public FavoriteJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Book> findByUser(int userId) {
        final String sql = """
                select b.book_id, title, author, genre, when_read, link, img_link 
                from books b join favorites on b.book_id = favorites.book_id 
                where favorites.user_id = ?
                ;""";

        return jdbcClient.sql(sql)
                .param(userId)
                .query(Book.class)
                .list();
    }

    @Override
    public boolean create(int userId, int bookId) {
        final String sql = """
                insert into favorites (user_id, book_id) values (
                :user_id,
                :book_id
                );""";

        //not using KeyHolder here - don't think I need it?
        return jdbcClient.sql(sql)
                .param("user_id", userId)
                .param("book_id", bookId)
                .update() > 0;
    }

    @Override
    public boolean deleteById(int userId, int bookId) {
        final String sql = """
                delete from favorites
                where user_id = :user_id
                and book_id = :book_id
                ;""";

        return jdbcClient.sql(sql)
                .param("user_id", userId)
                .param("book_id", bookId)
                .update() > 0;
    }
}
