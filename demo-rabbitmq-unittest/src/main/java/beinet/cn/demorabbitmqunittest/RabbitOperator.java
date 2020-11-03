package beinet.cn.demorabbitmqunittest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 初始化Exchange和Queue的类
 */
@Component
@Slf4j
public class RabbitOperator {
    private static final String EXCHANGE = "beinet.cn.exg";
    private static final String QUEUE = "beinet.cn.queue";
    private static final String ROUTEKEY = "beinet.cn.route";

    private final RabbitTemplate template;

    public RabbitOperator(RabbitTemplate template) {
        this.template = template;
    }

    /**
     * 不加Bean注解，将不会自动创建Exchange
     * @return
     */
    @Bean("beinet.exg")
    public DirectExchange createExchange() {
        // 创建 持久化的，不自动删除的Exchange
        return new DirectExchange(EXCHANGE, true, false);
    }

    /**
     * 不加Bean注解，将不会自动创建Queue
     * @return
     */
    @Bean("beinet.queue")
    public Queue createQueue() {
        // 创建持久化的Queue
        return new Queue(QUEUE, true);
    }


    /**
     * 不加Bean注解，将不会自动创建Binding.
     * 注：虽然这里会调用 createQueue 和 createExchange方法，但是并不会触发创建
     * @return
     */
    @Bean("beinet.bind")
    public Binding doBind() {
        return BindingBuilder.bind(createQueue()).to(createExchange()).with(ROUTEKEY);
    }

    /**
     * 发布消息
     *
     * @param obj 消息
     */
    public void publish(String obj) {
        template.convertAndSend(EXCHANGE, ROUTEKEY, obj);
    }

    @RabbitListener(queues = QUEUE)
    public void handleMsg(Message msg) {
        log.info("收到消息： " + new String(msg.getBody()));
    }
}
