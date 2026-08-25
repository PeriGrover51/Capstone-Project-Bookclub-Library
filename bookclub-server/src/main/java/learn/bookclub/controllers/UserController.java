package learn.bookclub.controllers;

import learn.bookclub.models.User;
import learn.bookclub.services.Result;
import learn.bookclub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody User user) {
        Result<User> result = userService.register(user);

        //TODO: change return type to ResponseEntity

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login (@RequestBody User user) {

        //TODO: change return type to ResponseEntity
        //TODO: make sure that this returns the jwt token and the user for frontend to use

        return userService.verify(user);
    }
}
