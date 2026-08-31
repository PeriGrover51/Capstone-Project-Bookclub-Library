package learn.bookclub.services;

import learn.bookclub.models.Vote;
import learn.bookclub.repos.NominationRepository;
import learn.bookclub.repos.UserRepository;
import learn.bookclub.repos.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class VoteServiceTest {

    @Autowired
    VoteService service;

    @MockBean
    VoteRepository repository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    NominationRepository nominationRepository;

    //tests:
    //invalid score, invalid user, invalid nomination
    //happy path create, happy path update
    @Test
    void shouldNotSaveInvalidScore() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(nominationRepository.findById(1)).thenReturn(TestDataHelper.existingNomination());
        when(repository.findByUserIdAndNominationId(1, 1)).thenReturn(null); //'doesnt exist, create new vote' - shouldn't matter here
        Result<Vote> result = service.saveVote("a", 1, 999);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().size() == 1);
        assertTrue(result.getErrorMessages().contains("score must be between 1 and 5"));

        verify(repository, never()).saveVote(any(Vote.class));
    }

    @Test
    void shouldNotSaveInvalidUsername() {
        when(userRepository.findByUsername("invalid")).thenReturn(null);
        when(nominationRepository.findById(1)).thenReturn(TestDataHelper.existingNomination());
        Result<Vote> result = service.saveVote("invalid", 1, 1);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().size() == 1);
        assertTrue(result.getErrorMessages().contains("user not in db"));

        verify(repository, never()).saveVote(any(Vote.class));
    }

    @Test
    void shouldNotSaveInvalidNomination() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(nominationRepository.findById(999)).thenReturn(null);
        Result<Vote> result = service.saveVote("a", 999, 1);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().size() == 1);
        assertTrue(result.getErrorMessages().contains("nomination not in db"));

        verify(repository, never()).saveVote(any(Vote.class));
    }

    @Test
    void saveUpdateHappy() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(nominationRepository.findById(1)).thenReturn(TestDataHelper.existingNomination());
        when(repository.findByUserIdAndNominationId(1, 1)).thenReturn(TestDataHelper.existingVote()); //'exists, so update mode'
        when(repository.saveVote(new Vote(1,1, 1, 1))).thenReturn(new Vote(1,1, 1, 1));
        Result<Vote> result = service.saveVote("a", 1, 1);

        assertTrue(result.isSuccess());
        assertEquals(new Vote(1,1, 1, 1), result.getpayload());

        verify(repository).saveVote(new Vote(1,1, 1, 1));
    }

    @Test
    void saveCreateHappy() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(nominationRepository.findById(1)).thenReturn(TestDataHelper.existingNomination());
        when(repository.findByUserIdAndNominationId(1, 1)).thenReturn(null); //'null, so create mode'
        when(repository.saveVote(new Vote(0,1, 1, 1))).thenReturn(new Vote(2,1, 1, 1));

        Result<Vote> result = service.saveVote("a", 1, 1);

        assertTrue(result.isSuccess());
        assertEquals(new Vote(2,1, 1, 1), result.getpayload());

        verify(repository).saveVote(new Vote(0,1, 1, 1));
    }

}