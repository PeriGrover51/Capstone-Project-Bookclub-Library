package learn.bookclub.controllers;

import learn.bookclub.models.User;
import learn.bookclub.services.Result;
import learn.bookclub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody User user) {
        Result<User> result = userService.register(user);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody User user) {
        Result<String> result = userService.verify(user);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }

        //sets user id so response holds correct userId, not just 0
        User userInDB = userService.findByUsername(user.getUsername());
        user.setUserId(userInDB.getUserId());

        return ResponseEntity.ok(Map.of(
               "token", result.getpayload(),
               "user", Map.of(
                       "id", user.getUserId(),
                        "username", user.getUsername()
                )
        ));
    }
}
