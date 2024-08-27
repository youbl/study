package beinet.cn.awss3demo.demos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 新类
 *
 * @author youbl
 * @since 2024/6/12 17:57
 */
@RestController
public class HomeController {
    @GetMapping("")
    public String home() {
        return this.getClass().getName() + " " + LocalDateTime.now();
    }
}
