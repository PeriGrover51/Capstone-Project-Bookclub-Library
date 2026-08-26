package learn.bookclub.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    //this controller exists solely for testing spring security + jwt tokens
    @GetMapping("/")
    public String greet() {
        return "Testing Spring in the project";
    }
}
