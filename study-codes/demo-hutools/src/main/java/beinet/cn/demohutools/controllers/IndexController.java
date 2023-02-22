package beinet.cn.demohutools.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 新类
 *
 * @author youbl
 * @date 2023/2/22 16:09
 */
@RestController
public class IndexController {
    @GetMapping("")
    public String index(@RequestParam(required = false) String str) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        return now + " " + str;
    }
}
