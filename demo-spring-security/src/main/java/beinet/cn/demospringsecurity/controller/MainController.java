package beinet.cn.demospringsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class MainController {
    @GetMapping("/")
    public String index() {
        return LocalDateTime.now() + " hello Beinet.";
    }

    @GetMapping("/time")
    public String time() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @GetMapping("/news")
    public String news() {
        return LocalDateTime.now() + " I'm news.";
    }
}
