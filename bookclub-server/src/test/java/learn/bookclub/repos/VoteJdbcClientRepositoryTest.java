package learn.bookclub.repos;

import learn.bookclub.models.Vote;
import learn.bookclub.services.TestDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class VoteJdbcClientRepositoryTest {

    @Autowired
    private VoteJdbcClientRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }



    @Test
    void findByUserAndNominationHappy() {
        Vote vote = repository.findByUserIdAndNominationId(2, 1);
        assertNotNull(vote);
    }

    @Test
    void findByUserAndNominationInvalid() {
        Vote vote = repository.findByUserIdAndNominationId(999, 999);
        assertNull(vote);
    }

    @Test
    void findByNominationHappy() {
        List<Vote> votes = repository.findByNominationId(1);
        assertTrue(votes.size() == 1);
    }

    @Test
    void findByNominationInvalid() {
        List<Vote> votes = repository.findByNominationId(999);
        assertTrue(votes.size() == 0);
    }

    @Test
    void findByUserHappy() {
        List<Vote> votes = repository.findByUserId(2);
        assertTrue(votes.size() == 1);
    }

    @Test
    void findByUserInvalid() {
        List<Vote> votes = repository.findByUserId(999);
        assertTrue(votes.size() == 0);
    }


    //save vote repo tests: create + update
    @Test
    void saveNewVoteHappy() {
        repository.saveVote(TestDataHelper.voteToCreate());
        assertTrue(repository.findByNominationId(1).size() == 2);
        assertTrue(repository.findByUserId(1).size() == 1);
    }

    @Test
    void saveUpdatedVoteHappy() {
        repository.saveVote(TestDataHelper.voteToUpdate());
        assertTrue(repository.findByNominationId(1).size() == 1);
        assertTrue(repository.findByUserId(1).size() == 0);
        assertTrue(repository.findByUserId(2).size() == 1);
    }

}