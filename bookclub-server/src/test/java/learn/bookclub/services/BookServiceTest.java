package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.repos.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookServiceTest {

    @Autowired
    BookService service;

    @MockBean
    BookRepository repository;

    @Test
    void findByIdHappy() {
        when(repository.findById(1)).thenReturn(TestDataHelper.existingBook());
        Result<Book> expected = new Result<>();
        expected.setpayload(TestDataHelper.existingBook());

        Result<Book> actual = service.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void findByIdFailsToFind() {
        when(repository.findById(999)).thenReturn(null);
        Result<Book> expected = new Result<>();
        expected.setpayload(null);
        expected.addErrorMessage("Book not found", ResultType.NOT_FOUND);

        Result<Book> actual = service.findById(999);

        assertEquals(expected, actual);
    }


    //update tests:
    //happy, blank / null fields, unset id, invalid id, dupe test
    @Test
    void shouldNotUpdateNullFields() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingBook()));

        assertTrue(service.update(null).getErrorMessages().contains("book cannot be null"));

        Book nullFields = new Book(1, null, null, null, null, null, null);
        Result<Book> result = service.update(nullFields);

        assertTrue(result.getErrorMessages().size() == 4);

        verify(repository, never()).update(nullFields);
    }

    @Test
    void shouldNotUpdateBlankFields() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingBook()));

        Book blankFields = new Book(1, "", "", "", null, null, null);
        Result<Book> result = service.update(blankFields);

        assertTrue(result.getErrorMessages().size() == 4);

        verify(repository, never()).update(blankFields);
    }

    @Test
    void shouldNotUpdateUnsetId() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingBook()));

        Book unsetId = TestDataHelper.bookToUpdate();
        unsetId.setBookId(0);
        Result<Book> result = service.update(unsetId);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("bookId is required."));
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).update(unsetId);
    }

    @Test
    void shouldNotUpdateInvalidId() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingBook()));
        when(repository.findById(999)).thenReturn(null);

        Book badId = TestDataHelper.bookToUpdate();
        badId.setBookId(999);
        Result<Book> result = service.update(badId);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("book not in db"));
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).update(badId);
    }

    @Test
    void shouldNotUpdateDuplicate() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingBook()));

        Book duplicate = TestDataHelper.existingBook();
        duplicate.setBookId(2);
        Result<Book> result = service.update(duplicate);
        //although this id doesnt exist in the db, the validation should add dupes err + return result before calling findById

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("book cannot be duplicate"));

        verify(repository, never()).update(duplicate);
    }

    @Test
    void updateHappy() {
        when(repository.findAll()).thenReturn(List.of(TestDataHelper.existingBook()));
        when(repository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.update(TestDataHelper.bookToUpdate())).thenReturn(true);

        Book toUpdate = TestDataHelper.bookToUpdate();
        Result<Book> result = service.update(toUpdate);

        assertTrue(result.isSuccess());
        assertEquals(TestDataHelper.bookToUpdate(), result.getpayload());

        verify(repository, times(1)).update(toUpdate);
    }

    //create tests:
    //happy, blank / null fields, set id, dupe test

}