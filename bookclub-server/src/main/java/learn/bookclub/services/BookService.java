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
}
