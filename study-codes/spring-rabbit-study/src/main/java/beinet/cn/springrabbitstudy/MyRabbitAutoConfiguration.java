package beinet.cn.springrabbitstudy;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitAutoConfiguration {
    // 在 RabbitAutoConfiguration 里，定义了Bean: ConnectionFactory
    @Bean
    MyRabbitAdmin createMyRabbitAdmin(ConnectionFactory connectionFactory) {
        return new MyRabbitAdmin(connectionFactory);
    }
}
