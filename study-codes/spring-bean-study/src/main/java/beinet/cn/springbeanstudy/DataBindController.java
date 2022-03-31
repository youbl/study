package beinet.cn.springbeanstudy;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/30 14:35
 */
@RestController
public class DataBindController {
    @PostMapping("bind")
    public User doBind(@RequestBody User para) {
        return para;
    }

    @Data
    public static class User {
        private int id;
        private String name;
        private LocalDateTime birthday;
        private long n1;
        private Long n2;
    }
}
