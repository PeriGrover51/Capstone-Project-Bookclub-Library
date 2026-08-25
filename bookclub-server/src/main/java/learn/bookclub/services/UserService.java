package learn.bookclub.services;

import learn.bookclub.models.User;
import learn.bookclub.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final int HASH_STRENGTH = 12;

    @Autowired
    private UserRepository repository;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(HASH_STRENGTH); //included in springsec

    public User register(User user) {
        //TODO: change to Result<User>, add validation
        user.setPassword(encoder.encode(user.getPassword())); //encodes pw before saving to repo
        return repository.create(user);
    }

    public String verify(User user) {
        //TODO: change to Result<User>
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())); //gives authentication obj

        if (authentication.isAuthenticated()) {
            return "Success";
        }

        return "Fail";
    }
}
