package beinet.cn.demolograbbitmq.rabbit;

import beinet.cn.demolograbbitmq.util.Dto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听消息的消费者定义
 */
@Component
public class Consumer {
    @RabbitListener(queues = Producer.QUEUE)
    void handler(@Payload Dto dto, @Headers Map<String, Object> headers) {
        System.out.println("我收到消息了:" + dto);
    }
}
