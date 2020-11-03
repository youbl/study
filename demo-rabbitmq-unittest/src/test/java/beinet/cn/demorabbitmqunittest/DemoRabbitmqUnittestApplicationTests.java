package beinet.cn.demorabbitmqunittest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class DemoRabbitmqUnittestApplicationTests {

    @Autowired
    RabbitOperator operator;

    @Test
    void contextLoads() {
        operator.publish("发布一条消息：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Assert.isTrue(true, "控制台将会输出一条Listener输出的消息");
    }

}
