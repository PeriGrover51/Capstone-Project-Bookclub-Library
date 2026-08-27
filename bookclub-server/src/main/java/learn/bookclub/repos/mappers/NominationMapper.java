package learn.bookclub.repos.mappers;

import learn.bookclub.models.Nomination;
import learn.bookclub.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NominationMapper implements RowMapper<Nomination> {
    @Override
    public Nomination mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                null
        );

        return new Nomination(
                rs.getInt("nomination_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                user
        );
    }
}
