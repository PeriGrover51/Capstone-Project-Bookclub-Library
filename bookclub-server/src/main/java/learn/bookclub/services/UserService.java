package learn.bookclub.services;

import learn.bookclub.models.User;
import learn.bookclub.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final int HASH_STRENGTH = 12;

    @Autowired
    private UserRepository repository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(HASH_STRENGTH); //included in springsec

    public Result<User> register(User user) {
        //create new user
        Result<User> result = new Result<>();

        //validate user is valid
        if (user.getUsername().isBlank()) {
            result.addErrorMessage("Username cannot be blank", ResultType.INVALID);
        }

        if (user.getPassword().isBlank()) {
            result.addErrorMessage("Password cannot be blank", ResultType.INVALID);
        }

        if (repository.findByUsername(user.getUsername()) != null) {
            result.addErrorMessage("Username is already taken", ResultType.INVALID);
        }

        if (result.isSuccess()) {
            user.setPassword(encoder.encode(user.getPassword())); //encodes pw before saving to repo
            User registeredUser = repository.create(user);
            result.setpayload(registeredUser);
        }


        return result;
    }

    public Result<String> verify(User user) {
        Result<String> result = new Result<>();

        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())); //gives authentication obj

        //user is valid / in the db
        if (authentication.isAuthenticated()) {
            result.setpayload(jwtService.generateToken(user.getUsername())); //generates a jwt token upon login success
            //return jwtService.generateToken(user.getUsername());
        } else {
            result.addErrorMessage("Failed to login - not in db or wrong username/pw", ResultType.NOT_FOUND);
        }

        return result;
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Result<List<String>> findAll() {
        Result<List<String>> result = new Result<>();

        List<User> users = repository.findAll();
        if (users == null) {
            result.addErrorMessage("could not query the db", ResultType.INVALID);
            return result;
        }
        List<String> usernames = new ArrayList<>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }

        result.setpayload(usernames);
        return result;
    }
}
