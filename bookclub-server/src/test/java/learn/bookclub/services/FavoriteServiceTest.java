package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.repos.BookRepository;
import learn.bookclub.repos.FavoriteRepository;
import learn.bookclub.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FavoriteServiceTest {

    @Autowired
    FavoriteService service;

    @MockBean
    FavoriteRepository repository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    BookRepository bookRepository;


    @Test
    void shouldNotFindInvalidUsername() {
        when(userRepository.findByUsername("invalid")).thenReturn(null);
        Result<List<Book>> result = service.findByUsername("invalid");

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).findByUser(anyInt());
    }

    @Test
    void shouldFindByUsernameHappy() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(repository.findByUser(1)).thenReturn(List.of(TestDataHelper.existingBook()));
        Result<List<Book>> result = service.findByUsername("a");

        assertTrue(result.isSuccess());
        assertEquals(List.of(TestDataHelper.existingBook()), result.getpayload());
    }

    @Test
    void shouldNotCreateInvalidUser() {
        when(userRepository.findByUsername("invalid")).thenReturn(null);
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());

        Result<Book> result = service.create("invalid", 1);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("user not in db"));
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).create(anyInt(), anyInt());
    }

    @Test
    void shouldNotCreateInvalidBook() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(bookRepository.findById(999)).thenReturn(null);

        Result<Book> result = service.create("a", 999);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("book not in db"));
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).create(anyInt(), anyInt());
    }

    @Test
    void shouldNotCreateDuplicate() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.findByUser(1)).thenReturn(List.of(TestDataHelper.existingBook()));

        Result<Book> result = service.create("a", 1);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("book is already a favorite"));
        assertTrue(result.getErrorMessages().size() == 1);

        verify(repository, never()).create(anyInt(), anyInt());
    }

    @Test
    void shouldCreateHappy() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.findByUser(1)).thenReturn(List.of());

        Result<Book> result = service.create("a", 1);

        assertTrue(result.isSuccess());
        assertEquals(TestDataHelper.existingBook(), result.getpayload());

        verify(repository, times(1)).create(1, 1);
    }

    @Test
    void shouldNotDeleteFavoriteDoesNotExist() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.deleteById(1, 1)).thenReturn(false);

        Result<Book> result = service.deleteById("a", 1);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("could not delete"));
    }

    @Test
    void shouldDeleteHappy() {
        when(userRepository.findByUsername("a")).thenReturn(TestDataHelper.existingUser());
        when(bookRepository.findById(1)).thenReturn(TestDataHelper.existingBook());
        when(repository.deleteById(1, 1)).thenReturn(true);

        Result<Book> result = service.deleteById("a", 1);
        assertTrue(result.isSuccess());
    }

}