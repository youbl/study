package com.example.springpwdencstudy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/1/20 16:18
 */
@RestController
public class IndexController {
    @GetMapping("")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) +
                " " + this.getClass().getName();
    }
}
