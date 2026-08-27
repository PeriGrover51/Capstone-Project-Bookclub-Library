package learn.bookclub.controllers;

import learn.bookclub.models.Book;
import learn.bookclub.models.Meeting;
import learn.bookclub.services.MeetingService;
import learn.bookclub.services.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin
public class MeetingController {
    @Autowired
    private MeetingService service;

    @GetMapping
    public List<Meeting> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Result<Meeting> result = service.findById(id);
        if(!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.OK);
    }

    //post mapping to create
    //user must be logged in, but specific user doesn't matter
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Meeting meeting) {
        Result<Meeting> result = service.create(meeting);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
    }


    //put mapping to update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Meeting meeting) {
        //check that url id == meeting.meetingId
        if (id != meeting.getMeetingId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); //409
        }

        Result<Meeting> result = service.update(meeting);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
    }
}
