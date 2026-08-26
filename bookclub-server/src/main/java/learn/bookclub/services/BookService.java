package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.repos.BookJdbcRepository;
import learn.bookclub.repos.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Result<Book> findById(int id) {
        Result<Book> result = new Result<>();
        Book existing = repository.findById(id);
        if (existing == null) {
            result.addErrorMessage("Book not found", ResultType.NOT_FOUND);
        } else {
            result.setpayload(existing);
        }

        return result;
    }

    public Result<Book> update(Book book) {
        //update validation: id must be set
        //id must exist in db
        Result<Book> result = new Result<>();
        validate(book, result);

        if (!result.isSuccess()) {
            return result;
        }

        if (book.getBookId() <= 0) {
            result.addErrorMessage("bookId is required.", ResultType.INVALID);
            return result;
        }

        Book existing = repository.findById(book.getBookId());
        if (existing == null) {
            result.addErrorMessage("book not in db", ResultType.NOT_FOUND);
        }

        if (result.isSuccess()) {
            if (repository.update(book)) {
                result.setpayload(book);
            } else {
                result.addErrorMessage("book not found", ResultType.NOT_FOUND);
            }
        }

        return result;
    }

    public Result<Book> create(Book book) {
        //create validation: id must be 0
        Result<Book> result = new Result<>();
        validate(book, result);

        if (!result.isSuccess()) {
            return result;
        }

        if (book.getBookId() != 0) {
            result.addErrorMessage("bookId must not be set", ResultType.INVALID);
            return result;
        }

        Book created = repository.create(book);
        result.setpayload(created);

        return result;
    }



    private void validate(Book book, Result<Book> result) {
        //validation for create + update:book/ title / author / genre / whenRead cannot be null or blank
        //whenRead can be in the future, e.g. the book wins the nominations and is set to begin reading next week
        //link and img_link can be null
        if (book == null) {
            result.addErrorMessage("book cannot be null", ResultType.INVALID);
            return;
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            result.addErrorMessage("title cannot be empty", ResultType.INVALID);
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            result.addErrorMessage("author cannot be empty", ResultType.INVALID);
        }
        if (book.getGenre() == null || book.getGenre().isBlank()) {
            result.addErrorMessage("genre cannot be empty", ResultType.INVALID);
        }
        if (book.getWhenRead() == null) {
            result.addErrorMessage("whenRead cannot be empty", ResultType.INVALID);
        }
        //(title + author + genre + whenRead) must be unique combination and bookIds don't match (for update purposes)

        if (result.isSuccess()) {
            List<Book> existingBooks = repository.findAll();

            for (Book existingBook : existingBooks) {
                if (existingBook.getBookId() != book.getBookId() &&
                        existingBook.getTitle().equalsIgnoreCase(book.getTitle()) &&
                        existingBook.getAuthor().equalsIgnoreCase(book.getAuthor()) &&
                        existingBook.getGenre().equalsIgnoreCase(book.getGenre()) &&
                        existingBook.getWhenRead().isEqual(book.getWhenRead())) {
                    result.addErrorMessage("book cannot be duplicate", ResultType.INVALID);
                }
            }
        }
    }
}
