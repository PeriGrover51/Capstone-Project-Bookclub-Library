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
        System.out.println(requestingUsername);

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

    //post / create mapping: pull the Authentication obj, get the username from that, and compare to the userid in the request body
    //Put / edit mapping: pull the Authentication obj, get the username from that, and compare to the userid in the request body
}
