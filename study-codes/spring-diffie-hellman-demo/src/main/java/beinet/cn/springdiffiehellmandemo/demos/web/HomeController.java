package beinet.cn.springdiffiehellmandemo.demos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * HomeController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:43
 */
@RestController
public class HomeController {

    @GetMapping("")
    public String index() {
        return LocalDateTime.now() + " " + this.getClass().getName();
    }
}
