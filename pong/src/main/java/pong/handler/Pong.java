package pong.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Pong {
    @GetMapping("/world")
    public String world() {
        return "World";
    }
}