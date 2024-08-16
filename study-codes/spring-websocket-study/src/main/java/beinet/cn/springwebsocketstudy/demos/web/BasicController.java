package beinet.cn.springwebsocketstudy.demos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class BasicController {

    @GetMapping("")
    public String index() {
        return LocalDateTime.now() + " " + getClass().getName();
    }
}
