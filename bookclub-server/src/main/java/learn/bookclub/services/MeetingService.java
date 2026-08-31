package learn.bookclub.services;

import learn.bookclub.models.Book;
import learn.bookclub.models.Meeting;
import learn.bookclub.repos.BookRepository;
import learn.bookclub.repos.MeetingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MeetingService {

    private MeetingRepository repository;
    private BookRepository bookRepository;

    public MeetingService(MeetingRepository repository, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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

    public Result<Meeting> findCurrent() {
        Result<Meeting> result = new Result<>();
        Meeting current = repository.findCurrent();

        if (current == null) {
            result.addErrorMessage("No meetings found", ResultType.NOT_FOUND);
            return result;
        }
        //check that the meeting date is in the future, else add error message "no future meetings"
        if (current.getMeetingDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("No future meetings", ResultType.NOT_FOUND);
        }

        if (result.isSuccess()) {
            result.setpayload(current);
        }

        return result;
    }

    public Result<Meeting> update(Meeting meeting) {
        Result<Meeting> result = new Result<>();
        validate(result, meeting);

        if (!result.isSuccess()) {
            return result;
        }

        //check meetingId is set
        if (meeting.getMeetingId() <= 0) {
            result.addErrorMessage("id must be set", ResultType.INVALID);
            return result;
        }

        //check meetingId exists
        Meeting existing = repository.findById(meeting.getMeetingId());
        if (existing == null) {
            result.addErrorMessage("meeting not in db", ResultType.NOT_FOUND);
        }

        if (result.isSuccess()) {
            if (repository.update(meeting)) {
                result.setpayload(meeting);
            } else {
                result.addErrorMessage("meeting not found", ResultType.NOT_FOUND);
            }
        }

        return result;
    }

    public Result<Meeting> create(Meeting meeting) {
        Result<Meeting> result = new Result<>();
        validate(result, meeting);

        if (!result.isSuccess()) {
            return result;
        }

        //check meetingId is unset
        if (meeting.getMeetingId() != 0) {
            result.addErrorMessage("meeting id must be unset", ResultType.INVALID);
            return result;
        }

        Meeting created = repository.create(meeting);
        result.setpayload(created);

        return result;
    }


    private void validate(Result<Meeting> result, Meeting meeting) {
        //create + update validation:
        if (meeting == null) {
            result.addErrorMessage("meeting cannot be null", ResultType.INVALID);
            return;
        }

        //book, readingGoal, meetingDate, and meetingNotes must not be null
        if (meeting.getReadingGoal() == null || meeting.getReadingGoal().isBlank()) {
            result.addErrorMessage("reading goal cannot be empty", ResultType.INVALID);
        }
        if (meeting.getMeetingDate() == null) {
            result.addErrorMessage("meeting date cannot be empty", ResultType.INVALID);
        }
        if (meeting.getMeetingNotes() == null || meeting.getMeetingNotes().isBlank()) {
            result.addErrorMessage("meeting notes cannot be empty", ResultType.INVALID);
        }
        if (meeting.getBook() == null) {
            result.addErrorMessage("book cannot be empty", ResultType.INVALID);
        }

        //dupe test: if meetingId != existingId && meetingDate is the same, then do not allow (cannot have mult meetings on same date)
        if (result.isSuccess()) {

            //book id must exist
            Book existing = bookRepository.findById(meeting.getBook().getBookId());
            if (existing == null) {
                result.addErrorMessage("book must exist in db", ResultType.INVALID);
            }

            List<Meeting> existingMeetings = repository.findAll();

            for (Meeting existingMeeting : existingMeetings) {
                if (existingMeeting.getMeetingId() != meeting.getMeetingId() &&
                        existingMeeting.getMeetingDate().isEqual(meeting.getMeetingDate())) {
                    result.addErrorMessage("meeting dates must be unique", ResultType.INVALID);
                }
            }
        }
    }
}
