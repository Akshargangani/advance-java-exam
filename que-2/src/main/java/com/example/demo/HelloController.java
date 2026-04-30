package com.example.demo;
// added by akshar
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Spring Boot!";
    }
}
