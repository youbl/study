package beinet.cn.demounittestmockrabbitmq;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DbController {

    private final RabbitOperator operator;

    public DbController(RabbitOperator operator) {
        this.operator = operator;
    }

    /**
     * http://localhost:8801/publish 发布消息，监听的消费者定义，在RabbitOperator里
     * @return
     */
    @GetMapping("publish")
    public String publish() {
        String msg = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " " + this.getClass().getName();
        operator.publish(msg);
        return "OK: " + msg;
    }

    @GetMapping("index")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " " + this.getClass().getName();
    }
}
