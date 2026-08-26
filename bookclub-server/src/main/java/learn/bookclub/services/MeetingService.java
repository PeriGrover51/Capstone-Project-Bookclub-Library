package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.models.Meeting;
import learn.bookclub.repos.MeetingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {

    private MeetingRepository repository;

    public MeetingService(MeetingRepository repository) {
        this.repository = repository;
    }

    public List<Meeting> findAll() {
        return repository.findAll();
    }

    public Result<Meeting> findById(int meetingId) {
        Result<Meeting> result = new Result<>();
        Meeting existing = repository.findById(meetingId);
        if (existing == null) {
            result.addErrorMessage("Meeting not found", ResultType.NOT_FOUND);
        } else {
            result.setpayload(existing);
        }

        return result;
    }
}
