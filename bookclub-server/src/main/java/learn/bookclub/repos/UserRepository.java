package learn.bookclub.repos;

import learn.bookclub.models.User;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserRepository {
    public User findByUsername(String username) throws DataAccessException;

    public User create(User user) throws DataAccessException;

    public List<User> findAll();
}