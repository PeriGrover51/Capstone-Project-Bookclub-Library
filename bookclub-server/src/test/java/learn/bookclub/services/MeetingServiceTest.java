package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.models.Meeting;
import learn.bookclub.repos.BookRepository;
import learn.bookclub.repos.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MeetingServiceTest {

    @Autowired
    MeetingService service;

    @MockBean
    MeetingRepository repository;

    @MockBean
    BookRepository bookRepository;

    @Test
    void findByIdHappy() {
        when(repository.findById(1)).thenReturn(TestDataHelper.existingMeeting());
        Result<Meeting> expected = new Result<>();
        expected.setpayload(TestDataHelper.existingMeeting());

        Result<Meeting> actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void findByIdFailsToFind() {
        when(repository.findById(999)).thenReturn(null);
        Result<Meeting> expected = new Result<>();
        expected.setpayload(null);
        expected.addErrorMessage("Meeting not found", ResultType.NOT_FOUND);

        Result<Meeting> actual = service.findById(999);

        assertEquals(expected, actual);
    }



    //update tests:
    //happy, blank/null fields, unset id, invalid id, dupe test
    //invalid book
    @Test
    void shouldNotUpdateNullFields() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));

        assertTrue(service.update(null).getErrorMessages().contains("meeting cannot be null"));

        Meeting nullFields = new Meeting(1, null, null, null, null);
        Result<Meeting> result = service.update(nullFields);

        assertTrue(result.getErrorMessages().size() == 4);

        verify(repository, never()).update(nullFields);
    }

    @Test
    void shouldNotUpdateBlankFields() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));

        Meeting blankFields = new Meeting(1, "", null, "", null);
        Result<Meeting> result = service.update(blankFields);

        assertTrue(result.getErrorMessages().size() == 4);

        verify(repository, never()).update(blankFields);
    }

    @Test
    void shouldNotUpdateUnsetId() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());

        Meeting unsetId = TestDataHelper.meetingToUpdate();
        unsetId.setMeetingId(0);
        Result<Meeting> result = service.update(unsetId);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("id must be set"));
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).update(unsetId);
    }

    @Test
    void shouldNotUpdateInvalidId() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.findById(999)).thenReturn(null);

        Meeting badId = TestDataHelper.meetingToUpdate();
        badId.setMeetingId(999);
        Result<Meeting> result = service.update(badId);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("meeting not in db"));
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).update(badId);
    }

    @Test
    void shouldNotUpdateDupe() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());

        Meeting duplicate = TestDataHelper.existingMeeting();
        //this id doesn't exist, but it will not reach that check before the result is returned
        duplicate.setMeetingId(2);
        Result<Meeting> result = service.update(duplicate);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("meeting dates must be unique"));

        verify(repository, never()).update(duplicate);
    }

    @Test
    void shouldNotUpdateInvalidBook() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));
        when(bookRepository.findById(999)).thenReturn(null);

        Meeting invalidBook = TestDataHelper.meetingToUpdate();
        Book invalid = TestDataHelper.bookToCreate();
        invalid.setBookId(999);
        invalidBook.setBook(invalid);

        Result<Meeting> result = service.update(invalidBook);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("book must exist in db"));

        verify(repository, never()).update(invalidBook);
    }

    @Test
    void updateHappy() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.findById(1)).thenReturn(TestDataHelper.existingMeeting());
        when(repository.update(TestDataHelper.meetingToUpdate())).thenReturn(true);

        Meeting toUpdate = TestDataHelper.meetingToUpdate();
        Result<Meeting> result = service.update(toUpdate);

        assertTrue(result.isSuccess());
        assertEquals(TestDataHelper.meetingToUpdate(), result.getpayload());

        verify(repository, times(1)).update(toUpdate);
    }


    //create tests:
    //happy, set id, dupe test
    @Test
    void shouldNotCreateSetId() {
        Meeting setId = TestDataHelper.meetingToCreate();
        setId.setMeetingId(2);

        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));

        Result<Meeting> result = service.create(setId);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("meeting id must be unset"));

        verify(repository, never()).create(setId);
    }

    @Test
    void shouldNotCreateDupe() {
        Meeting dupe = TestDataHelper.existingMeeting();
        dupe.setMeetingId(0);

        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        Result<Meeting> result = service.create(dupe);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("meeting dates must be unique"));

        verify(repository, never()).create(dupe);
    }

    @Test
    void shouldCreateHappy() {
        Meeting toCreate = TestDataHelper.meetingToCreate();
        Meeting afterCreate = TestDataHelper.meetingToCreate();
        afterCreate.setMeetingId(2);

        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingMeeting()));
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.create(toCreate)).thenReturn(afterCreate);
        Result<Meeting> result = service.create(toCreate);

        assertTrue(result.isSuccess());
        assertEquals(afterCreate, result.getpayload());

        verify(repository, times(1)).create(toCreate);
    }

}