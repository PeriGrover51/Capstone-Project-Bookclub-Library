package learn.bookclub.repos;

import learn.bookclub.models.Book;
import learn.bookclub.models.Meeting;
import learn.bookclub.services.TestDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MeetingJdbcClientRepositoryTest {

    @Autowired
    private MeetingJdbcClientRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }


    @Test
    void findAllHappy() {
        List<Meeting> meetings = repository.findAll();

        assertTrue(meetings.size() == 1);
    }

    @Test
    void findByIdHappy() {
        Meeting meeting = repository.findById(1);
        assertNotNull(meeting);
        assertNotNull(meeting.getBook());
    }

    @Test
    void findByIdInvalid() {
        Meeting meeting = repository.findById(999);
        assertNull(meeting);
    }

    //create + update tests:
    @Test
    void shouldCreate() {
        Meeting toCreate = TestDataHelper.meetingToCreate();
        Meeting actual = repository.create(toCreate);

        assertEquals(2, actual.getMeetingId());
        assertEquals(actual, repository.findById(2));
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void shouldUpdate() {
        Meeting toUpdate = TestDataHelper.meetingToUpdate();
        assertTrue(repository.update(toUpdate));
        assertEquals("UPDATE",repository.findById(1).getReadingGoal());
    }

}