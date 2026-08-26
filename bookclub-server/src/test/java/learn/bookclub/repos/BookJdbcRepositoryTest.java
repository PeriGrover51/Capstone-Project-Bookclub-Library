package learn.bookclub.repos;

import learn.bookclub.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookJdbcRepositoryTest {

    @Autowired
    private BookJdbcRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }


    @Test
    void findAllHappy() {
        List<Book> books = repository.findAll();

        assertTrue(books.size() == 1);
    }

    @Test
    void findByIdHappy() {
        Book book = repository.findById(1);
        assertNotNull(book);
    }

    @Test
    void findByIdInvalid() {
        Book book = repository.findById(999);
        assertNull(book);
    }

}