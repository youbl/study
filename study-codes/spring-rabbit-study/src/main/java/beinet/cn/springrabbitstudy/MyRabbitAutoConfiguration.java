package beinet.cn.springrabbitstudy;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MyRabbitAutoConfiguration {
    // 在 RabbitAutoConfiguration 里，定义了Bean: ConnectionFactory
//    @Bean
    MyRabbitAdmin createMyRabbitAdmin(ConnectionFactory connectionFactory) {
        return new MyRabbitAdmin(connectionFactory);
    }

    // 参考实现 https://github.com/spring-projects/spring-amqp/issues/984
    // 重写 RabbitListenerAnnotationBeanPostProcessor 类时，必须也实现Bean: RabbitListenerEndpointRegistry
    @Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Primary
    public RabbitListenerAnnotationBeanPostProcessor rabbitListenerAnnotationProcessor(ApplicationContext applicationContext) {
        return new MyRabbitListenerAnnotationBeanPostProcessor(applicationContext);
    }

    @Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    // 复制 org.springframework.boot.autoconfigure.amqp.RabbitAnnotationDrivenConfiguration里，
    // 初始化SimpleRabbitListenerContainerFactory的代码
    @Bean(name = "rabbitListenerContainerFactory")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = "type", havingValue = "simple",
            matchIfMissing = true)
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setConnectionFactory(connectionFactory);
//        factory.setConcurrentConsumers(3);
//        factory.setMaxConcurrentConsumers(10);
//        factory.setContainerCustomizer(container -> /* customize the container */);
        factory.setAfterReceivePostProcessors(new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().getHeaders().put("newHeader1", "newValue");
                return message;
            }
        });
        return factory;
    }
}
