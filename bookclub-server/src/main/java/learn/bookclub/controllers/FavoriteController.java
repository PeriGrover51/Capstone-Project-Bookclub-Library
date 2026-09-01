package learn.bookclub.controllers;

import learn.bookclub.models.Book;
import learn.bookclub.services.FavoriteService;
import learn.bookclub.services.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/favorites")
public class FavoriteController {
    @Autowired
    FavoriteService service;

    //my favorites = get username from authentication
    @GetMapping("/mine")
    public ResponseEntity<?> getMyFavorites(Authentication authentication) {
        String requestingUsername = authentication.getName();

        Result<List<Book>> result = service.findByUsername(requestingUsername);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addFavorite(@PathVariable("id") int id, Authentication authentication) {
        String requestingUsername = authentication.getName();

        Result<Book> result = service.create(requestingUsername, id);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavorite(@PathVariable("id") int id, Authentication authentication) {
        String requestingUsername = authentication.getName();

        Result<Book> result = service.deleteById(requestingUsername, id);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //getting favorites of other users will come later.
}
