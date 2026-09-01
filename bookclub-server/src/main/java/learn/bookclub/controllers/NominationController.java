package learn.bookclub.controllers;

import learn.bookclub.models.Nomination;
import learn.bookclub.services.NominationService;
import learn.bookclub.services.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nominations")
@CrossOrigin
public class NominationController {

    @Autowired
    NominationService service;

    @GetMapping
    public List<Nomination> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Result<Nomination> result = service.findById(id);
        if(!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.OK);
    }

    //create + edit: check that auth username == nomination.user.username
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Nomination nomination, Authentication authentication) {
        String requestingUsername = authentication.getName(); //comes from verified JWT, not the request body

        //this should check that the username from the jwt token and the username in the nom body match.
        if (!nomination.getUser().getUsername().equals(requestingUsername)) {
            return new ResponseEntity<>("Logged-in user and nomination body user mismatch", HttpStatus.FORBIDDEN);
        }

        Result<Nomination> result = service.create(nomination);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Nomination nomination, Authentication authentication) {
        String requestingUsername = authentication.getName(); //comes from verified JWT, not the request body

        //this should check that the username from the jwt token and the username in the nom body match.
        if (!nomination.getUser().getUsername().equals(requestingUsername)) {
            return new ResponseEntity<>("Logged-in user and nomination body user mismatch", HttpStatus.FORBIDDEN);
        }
        //the check that nom body username matches the to-update-nomination's existing username in the db is handled in the service.

        //check that url id == nom id
        if (id != nomination.getNominationId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); //409
        }

        Result<Nomination> result = service.update(nomination);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
    }

    //delete mapping: check that the jwt token username matches the username of nom they are deleting
    //call service.findById(), if nom == null say it don't exist, if nom.getUserName != jwt username throw error
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id, Authentication authentication) {
        String requestingUsername = authentication.getName(); //comes from verified JWT, not the request body
        Result<Nomination> existing = service.findById(id);

        if (existing.getpayload() == null) {
            return new ResponseEntity<>("nomination id does not exist", HttpStatus.NOT_FOUND);
        }

        //this checks that the existing nomination's user matches the jwt token (signed-in user) and therefore is allowed to delete this nomination
        if (!existing.getpayload().getUser().getUsername().equals(requestingUsername)) {
            return new ResponseEntity<>("Cannot delete a nomination you don't own", HttpStatus.FORBIDDEN);
        }

        Result<Nomination> result = service.deleteById(id);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() { //no need to get auth username, this should fail unless they pass auth anyway
        Result<Nomination> result = service.deleteAll();
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
