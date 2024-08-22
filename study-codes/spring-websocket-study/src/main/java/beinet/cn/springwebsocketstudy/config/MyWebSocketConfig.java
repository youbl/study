package beinet.cn.springwebsocketstudy.config;

import beinet.cn.springwebsocketstudy.config.channelInterceptor.MyChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

/**
 * 新类
 * @author youbl
 * @since 2024/8/22 11:20
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class MyWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final List<MyChannelInterceptor> channelInterceptorList;

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

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        if (channelInterceptorList != null && !channelInterceptorList.isEmpty()) {
            MyChannelInterceptor[] arr = channelInterceptorList.toArray(new MyChannelInterceptor[channelInterceptorList.size()]);

            registration.interceptors(arr);
        }
    }

    // 添加下面的代码，FirstIntercept会额外输出CONNECT_ACK和DISCONNECT_ACK日志，其它参数一致，因此没必要加
//    @Override
//    public void configureClientOutboundChannel(ChannelRegistration registration) {
//        if (channelInterceptorList != null && !channelInterceptorList.isEmpty()) {
//            MyChannelInterceptor[] arr = channelInterceptorList.toArray(new MyChannelInterceptor[channelInterceptorList.size()]);
//
//            registration.interceptors(arr);
//        }
//    }
}
