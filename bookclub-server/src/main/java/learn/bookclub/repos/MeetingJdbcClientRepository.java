package learn.bookclub.repos;

import learn.bookclub.models.Meeting;
import learn.bookclub.repos.mappers.MeetingMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
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

    @Override
    public Meeting findCurrent() {
        return null;
    }

    @Override
    public Meeting create(Meeting meeting) {
        return null;
    }

    @Override
    public boolean update(Meeting meeting) {
        return false;
    }

    @Override
    public boolean deleteById(int meetingId) {
        return false;
    }
}
