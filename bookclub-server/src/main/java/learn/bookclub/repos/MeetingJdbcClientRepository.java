package learn.bookclub.repos;

import learn.bookclub.models.Meeting;
import learn.bookclub.repos.mappers.MeetingMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MeetingJdbcClientRepository implements MeetingRepository {

    private final JdbcClient jdbcClient;

    public MeetingJdbcClientRepository(JdbcClient jdbcClient) { this.jdbcClient = jdbcClient; }

    @Override
    public List<Meeting> findAll() {
        final String sql = """
                select meeting_id, reading_goal, meeting_date, meeting_notes, b.book_id, title, author, genre, when_read, link, img_link
                	from meetings m join books b
                	on m.book_id = b.book_id;""";

        return jdbcClient.sql(sql)
                .query(new MeetingMapper())
                .list();
    }

    @Override
    public Meeting findById(int meetingId) {
        final String sql = """
                select meeting_id, reading_goal, meeting_date, meeting_notes, b.book_id, title, author, genre, when_read, link, img_link
                	from meetings m join books b
                	on m.book_id = b.book_id
                	where meeting_id = ?;""";

        return jdbcClient.sql(sql)
                .param(meetingId)
                .query(new MeetingMapper())
                .optional().orElse(null);
    }

    //will be needed for the 'current session' page
    @Override
    public Meeting findCurrent() {
        final String sql = """
                select meeting_id, reading_goal, meeting_date, meeting_notes, b.book_id, title, author, genre, when_read, link, img_link
                	from meetings m join books b
                	on m.book_id = b.book_id
                	order by meeting_date desc
                	limit 1;""";

        return jdbcClient.sql(sql)
                .query(new MeetingMapper())
                .optional().orElse(null);
    }

    @Override
    public Meeting create(Meeting meeting) {
        final String sql = """
                insert into meetings (book_id, reading_goal, meeting_date, meeting_notes) values (
                :book_id,
                :reading_goal,
                :meeting_date,
                :meeting_notes
                );""";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("book_id", meeting.getBook().getBookId())
                .param("reading_goal", meeting.getReadingGoal())
                .param("meeting_date", meeting.getMeetingDate())
                .param("meeting_notes", meeting.getMeetingNotes())
                .update(keyHolder, "meeting_id");

        if (rowsAffected == 0) {
            return null;
        }

        meeting.setMeetingId(keyHolder.getKey().intValue());

        return meeting;
    }

    @Override
    public boolean update(Meeting meeting) {
        final String sql = """
                update meetings set 
                book_id = :book_id,
                reading_goal = :reading_goal,
                meeting_date = :meeting_date,
                meeting_notes = :meeting_notes
                where meeting_id = :meeting_id 
                ;""";

        int rowsUpdated = jdbcClient.sql(sql)
                .param("book_id", meeting.getBook().getBookId())
                .param("reading_goal", meeting.getReadingGoal())
                .param("meeting_date", meeting.getMeetingDate())
                .param("meeting_notes", meeting.getMeetingNotes())
                .param("meeting_id", meeting.getMeetingId())
                .update();

        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteById(int meetingId) {
        return false;
    }
}
