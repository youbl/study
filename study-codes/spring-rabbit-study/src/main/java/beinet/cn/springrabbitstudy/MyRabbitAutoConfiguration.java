package beinet.cn.springrabbitstudy;

import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
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
}
