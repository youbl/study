package beinet.cn.demounittestmockrabbitmq;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MockRabbitConfig
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/16 14:21
 */
@Configuration
public class MockRabbitConfig {
    /**
     * 启动一个mock用的RabbitMQ，参考：https://github.com/fridujo/rabbitmq-mock
     *
     * @return
     */
    @Bean
    ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(new MockConnectionFactory());
    }
}
