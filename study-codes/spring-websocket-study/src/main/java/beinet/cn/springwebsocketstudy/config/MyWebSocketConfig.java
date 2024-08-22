package beinet.cn.springwebsocketstudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 新类
 * @author youbl
 * @since 2024/8/22 11:20
 */
@Configuration
@EnableWebSocketMessageBroker
public class MyWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 给客户端使用的 websocket broker 地址
        registry.addEndpoint("/my-websocket-endpoint")
                // 如果浏览器不支持WebSocket 时，withSockJS会自动切换到使用轮询（polling）或长轮询（long-polling）的方式进行通信。
                // .withSockJS()
                // 支持跨域
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/topic");
    }
}
