package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.models.Meeting;
import learn.bookclub.repos.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MeetingServiceTest {

    @Autowired
    MeetingService service;

    @MockBean
    MeetingRepository repository;

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

}