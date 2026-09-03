package learn.bookclub.repos;

import learn.bookclub.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserJdbcClientRepository implements UserRepository {
    private final JdbcClient jdbcClient;

    public UserJdbcClientRepository(JdbcClient jdbcClient) { this.jdbcClient = jdbcClient; }

    @Override
    public User findByUsername(String username) throws DataAccessException {
        return jdbcClient.sql("select * from user where user.username = ?")
                .param(username)
                .query(User.class)
                .optional().orElse(null);
    }

    @Override
    public User create(User user) throws DataAccessException {
        final String sql = """
                insert into user (username, password)
                values (:username, :password);""";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .update(keyHolder, "user_id");

        if (rowsAffected == 0) {
            return null;
        }

        user.setUserId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public List<User> findAll() {
        final String sql = "select * from user;";

        return jdbcClient.sql(sql)
                .query(User.class)
                .list();
    }
}