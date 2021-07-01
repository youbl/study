package beinet.cn.canalDemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HomeController {
    @GetMapping()
    public String index() {
        return this.getClass().getName() + " " + LocalDateTime.now();
    }

}