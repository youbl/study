package beinet.cn.lombokdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 11:52
 */
@RestController
public class HomeController {
    @GetMapping("")
    public String index() {
        return this.getClass().getName() + " " + LocalDateTime.now();
    }
}
