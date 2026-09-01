package learn.bookclub.repos;

import learn.bookclub.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FavoriteJdbcClientRepositoryTest {

    @Autowired
    private FavoriteJdbcClientRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }


    @Test
    void findByUserHappy() {
        List<Book> books = repository.findByUser(1);
        assertTrue(books.size() == 1);
    }

    @Test
    void findByUserInvalid() {
        List<Book> books = repository.findByUser(999);
        assertTrue(books.size() == 0);
    }

    @Test
    void createHappy() {
        assertTrue(repository.create(2, 1));
        assertTrue(repository.findByUser(2).size() == 1);
    }

    @Test
    void createNotUnique() {
        assertThrows(DuplicateKeyException.class, () -> { repository.create(1, 1); });
        assertTrue(repository.findByUser(1).size() == 1);
    }

    @Test
    void deleteByIdHappy() {
        assertTrue(repository.deleteById(1, 1));
        assertTrue(repository.findByUser(1).size() == 0);
    }

}