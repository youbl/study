package beinet.cn.demologmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.logbook.DefaultHttpLogFormatter;
import org.zalando.logbook.HttpLogFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class BeinetController {
    @GetMapping("time")
    public String time() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }


    @Bean
    public HttpLogFormatter httpLogFormatter() {
        return new DefaultHttpLogFormatter();
    }
}
