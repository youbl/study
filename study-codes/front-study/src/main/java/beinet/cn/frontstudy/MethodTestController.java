package beinet.cn.frontstudy;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 新类
 *
 * @author youbl
 * @date 2023/3/9 11:14
 */
@RestController
public class MethodTestController {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @GetMapping("methodTest")
    public String getAction() {
        return LocalDateTime.now().format(FORMATTER) + " GET请求";
    }

    @PostMapping("methodTest")
    public String postAction() {
        return LocalDateTime.now().format(FORMATTER) + " POST 请求";
    }

    @PutMapping("methodTest")
    public String putAction() {
        return LocalDateTime.now().format(FORMATTER) + " PUT 请求";
    }

    @DeleteMapping("methodTest")
    public String delAction() {
        return LocalDateTime.now().format(FORMATTER) + " DELETE 请求";
    }

}
