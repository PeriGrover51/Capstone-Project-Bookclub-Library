package learn.bookclub.repos;

import learn.bookclub.models.Meeting;

import java.util.List;

public interface MeetingRepository {
    List<Meeting> findAll();

    Meeting findById(int meetingId);

    Meeting findCurrent();

    Meeting create(Meeting meeting);

    boolean update(Meeting meeting);

    boolean deleteById(int meetingId);
}
