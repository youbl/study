package beinet.cn.demolograbbitmq.rabbit;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 发送消息的生产者定义
 */
@Component
public class Producer {
    static final String QUEUE = "001.ybl";
    private final RabbitTemplate template;

    public Producer(RabbitTemplate template) {
        this.template = template;
    }

    public void sendMsg(Object obj) {
        // 用这个变量设置消息的Header
        MessagePostProcessor processor = message -> {
            message.getMessageProperties().setHeader("aaa", "bbb");
            return message;
        };

        // exchange为空，则直接把消息发到queue名==routingKey的队列里
        template.convertAndSend("", QUEUE, obj, processor);
    }
}
