package learn.bookclub.repos;

import learn.bookclub.models.Nomination;
import learn.bookclub.services.TestDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NominationJdbcClientRepositoryTest {

    @Autowired
    private NominationJdbcClientRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }


    @Test
    void findAllHappy() {
        List<Nomination> nominations = repository.findAll();

        assertTrue(nominations.size() == 1);
    }

    @Test
    void findByIdHappy() {
        Nomination nomination = repository.findById(1);
        assertNotNull(nomination);
        assertNotNull(nomination.getUser());
    }

    @Test
    void findByIdInvalid() {
        Nomination nomination = repository.findById(999);
        assertNull(nomination);
    }


    //create/update tests:
    @Test
    void createHappy() {
        Nomination toCreate = TestDataHelper.nominationToCreate();
        Nomination actual = repository.create(toCreate);

        assertEquals(2, actual.getNominationId());
        assertEquals(actual, repository.findById(2));
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void updateHappy() {
        Nomination toUpdate = TestDataHelper.nominationToUpdate();
        assertTrue(repository.update(toUpdate));
        assertEquals("UPDATE",repository.findById(1).getTitle());
    }

    @Test
    void deleteInvalidId() {
        assertFalse(repository.deleteById(999));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void deleteHappy() {
        assertTrue(repository.deleteById(1));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void deleteAllHappy() {
        assertTrue(repository.deleteAll());
        assertEquals(0, repository.findAll().size());
    }

}