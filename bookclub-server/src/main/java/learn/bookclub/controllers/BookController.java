package learn.bookclub.controllers;

import learn.bookclub.models.Book;
import learn.bookclub.services.BookService;
import learn.bookclub.services.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping
    public List<Book> findAll() { //don't need token / auth or body
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Result<Book> result = service.findById(id);
        if(!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.OK);
    }

    //post mapping to create
    //user authentication should be sent in the form of a jwt token, which should be handled in the chain - no need to check for it here
    //no need to check user mismatch here either
    //should be straightforward?
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Book book) {
        Result<Book> result = service.create(book);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
    }

    //put mapping to update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Book book) {
        return null;
    }
}
