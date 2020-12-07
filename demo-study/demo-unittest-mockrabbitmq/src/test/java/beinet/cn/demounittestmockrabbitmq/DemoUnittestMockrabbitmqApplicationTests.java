package beinet.cn.demounittestmockrabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

@SpringBootTest
@ActiveProfiles("unittest")
class DemoUnittestMockrabbitmqApplicationTests {

    @Autowired
    DbController controller;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 注：只在内存中mock，无法跨项目，比如单元测试publish的消息，无法被其它项目收到
     */
    @Test
    void contextLoads() {
        String str = "我是消息主体";
        controller.publish(str);

        Message message = rabbitTemplate.receive(RabbitOperator.QUEUE);
        Assert.isTrue(new String(message.getBody()).equals("PUBLISH:" + str), "收发消息不匹配");
    }

}
