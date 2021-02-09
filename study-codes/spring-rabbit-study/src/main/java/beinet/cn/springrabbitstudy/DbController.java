package beinet.cn.springrabbitstudy;

import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DbController {

    private final RabbitOperator operator;
    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    public DbController(RabbitOperator operator,
                        RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry) {
        this.operator = operator;
        this.rabbitListenerEndpointRegistry = rabbitListenerEndpointRegistry;
    }

    /**
     * http://localhost:8801/publish 发布消息，监听的消费者定义，在RabbitOperator里
     *
     * @return
     */
    @GetMapping("publish")
    public String publish(@RequestParam String msg) {
        operator.publish("PUBLISH:" + msg);
        return "OK: " + msg;
    }

    @GetMapping("start")
    public String start() {
        rabbitListenerEndpointRegistry.start();
        return "Started: " + getQueues();
    }

    @GetMapping("stop")
    public String stop() {
        rabbitListenerEndpointRegistry.stop();
        return "Stoped: " + getQueues();
    }

    private String getQueues() {
        StringBuilder sb = new StringBuilder();
        for (MessageListenerContainer container : rabbitListenerEndpointRegistry.getListenerContainers()) {
            if (container instanceof AbstractMessageListenerContainer) {
                for (String item : ((AbstractMessageListenerContainer) container).getQueueNames()) {
                    sb.append(item).append(',');
                }
            } else {
                sb.append(container.getClass().getName()).append(',');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
