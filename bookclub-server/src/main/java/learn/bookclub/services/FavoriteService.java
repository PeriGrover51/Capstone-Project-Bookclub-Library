package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.models.User;
import learn.bookclub.repos.BookRepository;
import learn.bookclub.repos.FavoriteRepository;
import learn.bookclub.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private FavoriteRepository repository;
    private UserRepository userRepository;
    private BookRepository bookRepository;

    public FavoriteService(FavoriteRepository repository, UserRepository userRepository, BookRepository bookRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Result<List<Book>> findByUsername(String username) {
        Result<List<Book>> result = new Result<>();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            result.addErrorMessage("user not in db", ResultType.INVALID);
            return result;
        }

        result.setpayload(repository.findByUser(user.getUserId()));
        return result;
    }

    public Result<Book> create(String username, int bookId) {
        Result<Book> result = new Result<>();
        User user = userRepository.findByUsername(username);
        Book book = bookRepository.findById(bookId);

        if (user == null) {
            result.addErrorMessage("user not in db", ResultType.INVALID);
        }
        if (book == null) {
            result.addErrorMessage("book not in db", ResultType.NOT_FOUND);
        }
        if (!result.isSuccess()) {
            return result;
        }

        //checks if this book is already favorited by this user
        List<Book> existingFavorites = repository.findByUser(user.getUserId());
        boolean exists = existingFavorites.stream()
                .anyMatch(favorite -> favorite.getBookId() == bookId);
        if (exists) {
            result.addErrorMessage("book is already a favorite", ResultType.INVALID);
            return result;
        }

        repository.create(user.getUserId(), bookId);
        result.setpayload(book);
        return result;
    }

    public Result<Book> deleteById(String username, int bookId) {
        Result<Book> result = new Result<>();
        User user = userRepository.findByUsername(username);
        Book book = bookRepository.findById(bookId);

        if (user == null) {
            result.addErrorMessage("user not in db", ResultType.INVALID);
        }
        if (book == null) {
            result.addErrorMessage("book not in db", ResultType.NOT_FOUND);
        }
        if (!result.isSuccess()) {
            return result;
        }

        boolean success = repository.deleteById(user.getUserId(), bookId);
        if (!success) {
            result.addErrorMessage("could not delete", ResultType.INVALID);
        }
        return result;
    }
}
