package learn.bookclub.services;

import learn.bookclub.models.Nomination;
import learn.bookclub.repos.NominationRepository;
import learn.bookclub.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NominationServiceTest {

    @Autowired
    NominationService service;

    @MockBean
    NominationRepository repository;

    @MockBean
    UserRepository userRepository;

    @Test
    void findByIdHappy() {
        when(repository.findById(1)).thenReturn(TestDataHelper.existingNomination());
        Result<Nomination> expected = new Result<>();
        expected.setpayload(TestDataHelper.existingNomination());

        Result<Nomination> actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void findByIdInvalid() {
        when(repository.findById(999)).thenReturn(null);

        Result<Nomination> actual = service.findById(999);

        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().size() == 1);
        assertNull(actual.getpayload());
    }

}