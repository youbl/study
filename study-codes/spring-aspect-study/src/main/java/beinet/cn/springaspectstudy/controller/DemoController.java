package beinet.cn.springaspectstudy.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DemoController {

    @RequestMapping("")
    public String index() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return String.format("我是RequestMapping的GET:%s %s", now, this.getClass().getName());
    }

    @GetMapping("isGet")
    public String isGet() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return String.format("我是GetMapping:%s %s", now, this.getClass().getName());
    }

    @RequestMapping(value = "isPost", method = RequestMethod.POST)
    public String isPost() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return String.format("我是RequestMapping的POST:%s %s", now, this.getClass().getName());
    }


    @PostMapping(value = "isPost2")
    public String isPost2() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return String.format("我是PostMapping:%s %s", now, this.getClass().getName());
    }
}
