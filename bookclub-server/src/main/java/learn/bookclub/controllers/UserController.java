package learn.bookclub.controllers;

import learn.bookclub.models.User;
import learn.bookclub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register (@RequestBody User user) { //TODO: change return type to ResponseEntity
        return userService.register(user);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login (@RequestBody )
}
