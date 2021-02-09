package beinet.cn.springrabbitstudy;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MsgHandler {
    @RabbitListener(queues = RabbitOperator.QUEUE)
    void xx1(Message message) {
        System.out.println(Thread.currentThread().getId() + "收到消息aaa:" + message);
    }


    @RabbitListener(queues = RabbitOperator.QUEUE)
    void xx2(Message message) {
        System.out.println(Thread.currentThread().getId() + "收到消息xxx:" + message);
    }
}
