package beinet.cn.springrabbitstudy;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MsgHandler {
    /*
    下面2个方法，监听同一个队列，会轮流消费，而不是1个消息消费2次。
    比如：
    第1次访问：http://localhost:8801/publish?msg=abc 输出 46收到消息aaa:(Body:'PUBLISH:abc'
    第2次访问：http://localhost:8801/publish?msg=abc 输出 46收到消息xxx:(Body:'PUBLISH:abc'
     */
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = RabbitOperator.QUEUE, durable = "true"),
                    exchange = @Exchange(value = RabbitOperator.EXCHANGE, type = "direct", durable = "true", autoDelete = "false"),
                    key = RabbitOperator.ROUTEKEY,
                    declare = "true")
    })
    void xx1(Message message) {
        System.out.println(Thread.currentThread().getId() + "收到消息aaa:" + message);
    }

  //  @RabbitListener(queues = RabbitOperator.QUEUE)
    void xx2(Message message) {
        System.out.println(Thread.currentThread().getId() + "收到消息xxx:" + message);
    }
}
