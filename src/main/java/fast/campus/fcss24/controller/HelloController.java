package fast.campus.fcss24.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/api/v1/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/api/v2/hello")
    public String hello2() {
        return "Hello!2";
    }
}
