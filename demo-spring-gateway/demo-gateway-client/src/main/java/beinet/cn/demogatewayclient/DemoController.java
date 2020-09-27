package beinet.cn.demogatewayclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class DemoController {
    @GetMapping("/")
    public String index() {
        return "Hello beinet, " + LocalDateTime.now() + "\n";
    }
}
