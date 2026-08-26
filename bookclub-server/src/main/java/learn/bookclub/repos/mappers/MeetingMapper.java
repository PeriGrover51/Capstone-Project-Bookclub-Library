package learn.bookclub.repos.mappers;

import learn.bookclub.models.Book;
import learn.bookclub.models.Meeting;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MeetingMapper implements RowMapper<Meeting> {
    @Override
    public Meeting mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getDate("when_read"),
                rs.getString("link"),
                rs.getString("img_link")
        );
        return new Meeting(
                rs.getInt("meeting_id"),
                rs.getString("reading_goal"),
                rs.getDate("meeting_date"),
                rs.getString("meeting_notes"),
                book
        );
    }
}
