package learn.bookclub.controllers;

import learn.bookclub.models.Vote;
import learn.bookclub.services.Result;
import learn.bookclub.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    VoteService service;


    @GetMapping("/nomination/{nominationId}") //returns all votes for a specific nomination - needed to count a nom's votes
    public List<Vote> getVotesForNomination(@PathVariable("nominationId") int nominationId) {
        return service.findByNominationId(nominationId);
    }

    @GetMapping("/nomination/{nominationId}/mine") //returns the vote of the user for a specific nomination - needed for prepopulating frontend data
    public ResponseEntity<?> getUserVoteForNomination(@PathVariable("nominationId") int nominationId, Authentication authentication) {
        //gets the user's info from the jwt, not the request body
        String requestingUsername = authentication.getName();

        Result<Vote> existingVote = service.findByUserAndNominationId(requestingUsername, nominationId);
        if (!existingVote.isSuccess()) { //problem with the user
            return ErrorResponse.build(existingVote);
        }
        if (existingVote.getpayload() == null) { //no vote for this nomination from this user - not an error
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(existingVote.getpayload(), HttpStatus.OK); //returns existing vote
    }

    @PostMapping("/nomination/{nominationId}") //create or update a vote
    public ResponseEntity<?> castVote(@PathVariable("nominationId") int nominationId, Authentication authentication, @RequestBody Map<String, Integer> requestBody) {
        String requestingUsername = authentication.getName();
        int score = requestBody.get("score"); //get score from the request body

        Result<Vote> result = service.saveVote(requestingUsername, nominationId, score);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        return new ResponseEntity<>(result.getpayload(), HttpStatus.OK);
    }
}
