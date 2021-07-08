package beinet.cn.springrabbitstudy;

import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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
        String beforeStatus = getQueues();
        rabbitListenerEndpointRegistry.start();
        return beforeStatus + "<br>\nStarted: <br>\n" + getQueues();
    }

    @GetMapping("stop")
    public String stop() {
        String beforeStatus = getQueues();
        rabbitListenerEndpointRegistry.stop();
        return beforeStatus + "<br>\nStoped: <br>\n" + getQueues();
    }

    private String getQueues() {
        StringBuilder sb = new StringBuilder();
        for (MessageListenerContainer container : rabbitListenerEndpointRegistry.getListenerContainers()) {
            if (container instanceof SimpleMessageListenerContainer) { // AbstractMessageListenerContainer
                SimpleMessageListenerContainer asbContainer = (SimpleMessageListenerContainer) container;
                for (String item : asbContainer.getQueueNames()) {
                    sb.append(item).append('、');
                }

                sb.append("[consumerCount:")
                        .append(asbContainer.getActiveConsumerCount())
                        .append(",running:")
                        .append(asbContainer.isRunning())
                        .append(",autoStartup:")
                        .append(asbContainer.isAutoStartup())
                        .append(",consumerBatchEnabled:")
                        .append(asbContainer.isConsumerBatchEnabled())
                        .append(",active:")
                        .append(asbContainer.isActive())
                        .append("],<br>\n");

            } else {
                sb.append(container.getClass().getName())
                        .append("[running:")
                        .append(container.isRunning())
                        .append(",autoStartup:")
                        .append(container.isAutoStartup())
                        .append(",consumerBatchEnabled:")
                        .append(container.isConsumerBatchEnabled())
                        .append("],<br>\n");
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
