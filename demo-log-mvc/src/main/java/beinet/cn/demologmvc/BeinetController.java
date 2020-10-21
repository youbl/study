package beinet.cn.demologmvc;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.logbook.DefaultHttpLogFormatter;
import org.zalando.logbook.HttpLogFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class BeinetController {
    @GetMapping("time")
    public String time() {
        return '"' + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + '"';
    }

    @PostMapping("add")
    public Dto add(@RequestBody Dto dto) {
        dto.setName("Hello, " + dto.getName());
        dto.setTime(LocalDateTime.now());
        return dto;
    }

    @Bean
    public HttpLogFormatter httpLogFormatter() {
        return new DefaultHttpLogFormatter();
    }

    @Data
    static class Dto {
        private int id;
        private String name;
        private LocalDateTime time;
    }
}
