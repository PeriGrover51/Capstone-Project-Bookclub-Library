package learn.bookclub.services;

import learn.bookclub.models.User;
import learn.bookclub.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final int HASH_STRENGTH = 12;

    @Autowired
    private UserRepository repository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(HASH_STRENGTH); //included in springsec

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword())); //encodes pw before saving to repo
        return repository.create(user);
    }
}
