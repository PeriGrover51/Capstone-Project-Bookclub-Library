package learn.bookclub.services;

import learn.bookclub.models.Meeting;
import learn.bookclub.models.Nomination;
import learn.bookclub.models.User;
import learn.bookclub.repos.NominationRepository;
import learn.bookclub.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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


    //update tests:
    //null fields, blank fields, unset id, invalid id, dupe test, invalid user, user mismatch
    @Test
    void shouldNotUpdateNullFields() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));

        assertTrue(service.update(null).getErrorMessages().contains("nomination cannot be null"));

        Nomination nullFields = new Nomination(1, null, null, null, null);
        Result<Nomination> result = service.update(nullFields);

        assertTrue(result.getErrorMessages().size() == 4);

        verify(repository, never()).update(nullFields);
    }

    @Test
    void shouldNotUpdateBlankFields() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));

        Nomination blankFields = new Nomination(1, "", "", "", null);
        Result<Nomination> result = service.update(blankFields);

        assertTrue(result.getErrorMessages().size() == 4);

        verify(repository, never()).update(blankFields);
    }

    @Test
    void shouldNotUpdateUnsetId() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(userRepository.findByUsername(TestDataHelper.nominationToUpdate().getUser().getUsername())).thenReturn(TestDataHelper.existingUser());

        Nomination unsetId = TestDataHelper.nominationToUpdate();
        unsetId.setNominationId(0);
        Result<Nomination> result = service.update(unsetId);

        assertTrue(result.getErrorMessages().contains("id must be set"));
        assertTrue(result.getErrorMessages().size() == 1);
        verify(repository, never()).update(unsetId);
    }

    @Test
    void shouldNotUpdateInvalidId() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(userRepository.findByUsername(TestDataHelper.nominationToUpdate().getUser().getUsername())).thenReturn(TestDataHelper.existingUser());

        Nomination badId = TestDataHelper.nominationToUpdate();
        badId.setNominationId(999);
        Result<Nomination> result = service.update(badId);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("nomination not in db"));
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).update(badId);
    }

    @Test
    void shouldNotUpdateDupe() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(userRepository.findByUsername(TestDataHelper.nominationToUpdate().getUser().getUsername())).thenReturn(TestDataHelper.existingUser());

        Nomination dupe = TestDataHelper.existingNomination();
        dupe.setNominationId(2);

        Result<Nomination> result = service.update(dupe);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("nomination cannot be duplicate"));

        verify(repository, never()).update(dupe);
    }

    @Test
    void shouldNotUpdateInvalidUser() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(userRepository.findByUsername("invalid")).thenReturn(null);

        Nomination invalidUser = TestDataHelper.nominationToUpdate();
        invalidUser.setUser(new User(999, "invalid", "doesntmatter"));

        Result<Nomination> result = service.update(invalidUser);

        assertTrue(result.getErrorMessages().contains("user does not exist in db"));
        verify(repository, never()).update(invalidUser);
    }

    @Test
    void shouldNotUpdateUserMismatch() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(repository.findById(1)).thenReturn(TestDataHelper.existingNomination());
        when(userRepository.findByUsername("b")).thenReturn(new User(2, "b", "130"));

        Nomination userMismatch = TestDataHelper.nominationToUpdate();
        userMismatch.setUser(new User(2, "b", "130"));

        Result<Nomination> result = service.update(userMismatch);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("cannot change nomination you don't own"));
        verify(repository, never()).update(userMismatch);
    }

    @Test
    void updateHappy() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(repository.findById(1)).thenReturn(TestDataHelper.existingNomination());
        when(userRepository.findByUsername(TestDataHelper.nominationToUpdate().getUser().getUsername())).thenReturn(TestDataHelper.existingUser());
        when(repository.update(TestDataHelper.nominationToUpdate())).thenReturn(true);

        Nomination toUpdate = TestDataHelper.nominationToUpdate();
        Result<Nomination> result = service.update(toUpdate);

        assertTrue(result.isSuccess());
        assertEquals(TestDataHelper.nominationToUpdate(), result.getpayload());

        verify(repository, times(1)).update(toUpdate);
    }


    //create tests:
    //set id, dupe test
    @Test
    void shouldNotCreateSetId() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(userRepository.findByUsername(TestDataHelper.nominationToUpdate().getUser().getUsername())).thenReturn(TestDataHelper.existingUser());

        Nomination setId = TestDataHelper.nominationToCreate();
        setId.setNominationId(3);
        Result<Nomination> result = service.create(setId);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("id cannot be set"));
        verify(repository, never()).create(setId);
    }

    @Test
    void shouldNotCreateDupe() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(userRepository.findByUsername(TestDataHelper.nominationToUpdate().getUser().getUsername())).thenReturn(TestDataHelper.existingUser());

        Nomination dupe = TestDataHelper.existingNomination();
        dupe.setNominationId(0);
        Result<Nomination> result = service.create(dupe);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("nomination cannot be duplicate"));
        verify(repository, never()).create(dupe);
    }

    @Test
    void CreateHappy() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingNomination()));
        when(userRepository.findByUsername(TestDataHelper.nominationToCreate().getUser().getUsername())).thenReturn(TestDataHelper.existingUser());

        Nomination toCreate = TestDataHelper.nominationToCreate();
        Nomination afterCreate = TestDataHelper.nominationToCreate();
        afterCreate.setNominationId(2);
        when(repository.create(toCreate)).thenReturn(afterCreate);

        Result<Nomination> result = service.create(toCreate);

        assertTrue(result.isSuccess());
        assertEquals(afterCreate, result.getpayload());

        verify(repository, times(1)).create(toCreate);
    }

}