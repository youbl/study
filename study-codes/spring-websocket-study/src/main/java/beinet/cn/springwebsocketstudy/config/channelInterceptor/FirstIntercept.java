package beinet.cn.springwebsocketstudy.config.channelInterceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * 新类
 * @author youbl
 * @since 2024/8/22 15:56
 */
@Component
@Slf4j
public class FirstIntercept implements MyChannelInterceptor {
    /*
    客户端发起连接后的3条日志
    preSend: GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT, stompCommand=CONNECT, nativeHeaders={accept-version=[1.2,1.1,1.0], heart-beat=[10000,10000]}, simpSessionAttributes={}, simpHeartbeat=[J@21f885c5, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel]
    postSend: GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT, stompCommand=CONNECT, nativeHeaders={accept-version=[1.2,1.1,1.0], heart-beat=[10000,10000]}, simpSessionAttributes={}, simpHeartbeat=[J@21f885c5, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel] true
    afterSendCompletion: GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT, stompCommand=CONNECT, nativeHeaders={accept-version=[1.2,1.1,1.0], heart-beat=[10000,10000]}, simpSessionAttributes={}, simpHeartbeat=[J@21f885c5, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel] true

    客户端发起订阅后的3条日志
    preSend: GenericMessage [payload=byte[0], headers={simpMessageType=SUBSCRIBE, stompCommand=SUBSCRIBE, nativeHeaders={id=[sub-0], destination=[/topic/mymsg?ak=bbbbb]}, simpSessionAttributes={}, simpHeartbeat=[J@2f69e9c1, simpSubscriptionId=sub-0, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81, simpDestination=/topic/mymsg?ak=bbbbb}] ExecutorSubscribableChannel[clientInboundChannel]
    postSend: GenericMessage [payload=byte[0], headers={simpMessageType=SUBSCRIBE, stompCommand=SUBSCRIBE, nativeHeaders={id=[sub-0], destination=[/topic/mymsg?ak=bbbbb]}, simpSessionAttributes={}, simpHeartbeat=[J@2f69e9c1, simpSubscriptionId=sub-0, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81, simpDestination=/topic/mymsg?ak=bbbbb}] ExecutorSubscribableChannel[clientInboundChannel] true
    afterSendCompletion: GenericMessage [payload=byte[0], headers={simpMessageType=SUBSCRIBE, stompCommand=SUBSCRIBE, nativeHeaders={id=[sub-0], destination=[/topic/mymsg?ak=bbbbb]}, simpSessionAttributes={}, simpHeartbeat=[J@2f69e9c1, simpSubscriptionId=sub-0, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81, simpDestination=/topic/mymsg?ak=bbbbb}] ExecutorSubscribableChannel[clientInboundChannel] true

    客户端发消息后的3条日志
    preSend: GenericMessage [payload=byte[100], headers={simpMessageType=MESSAGE, stompCommand=SEND, nativeHeaders={destination=[/app/wkmsg], content-length=[100]}, simpSessionAttributes={}, simpHeartbeat=[J@5ea4dd35, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81, simpDestination=/app/wkmsg}] ExecutorSubscribableChannel[clientInboundChannel]
    postSend: GenericMessage [payload=byte[100], headers={simpMessageType=MESSAGE, stompCommand=SEND, nativeHeaders={destination=[/app/wkmsg], content-length=[100]}, simpSessionAttributes={}, simpHeartbeat=[J@5ea4dd35, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81, simpDestination=/app/wkmsg}] ExecutorSubscribableChannel[clientInboundChannel] true
    afterSendCompletion: GenericMessage [payload=byte[100], headers={simpMessageType=MESSAGE, stompCommand=SEND, nativeHeaders={destination=[/app/wkmsg], content-length=[100]}, simpSessionAttributes={}, simpHeartbeat=[J@5ea4dd35, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81, simpDestination=/app/wkmsg}] ExecutorSubscribableChannel[clientInboundChannel] true

    客户端断开连接后的6条日志
    preSend: GenericMessage [payload=byte[0], headers={simpMessageType=DISCONNECT, stompCommand=DISCONNECT, nativeHeaders={receipt=[close-1]}, simpSessionAttributes={}, simpHeartbeat=[J@4705fd3f, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel]
    postSend: GenericMessage [payload=byte[0], headers={simpMessageType=DISCONNECT, stompCommand=DISCONNECT, nativeHeaders={receipt=[close-1]}, simpSessionAttributes={}, simpHeartbeat=[J@4705fd3f, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel] true
    afterSendCompletion: GenericMessage [payload=byte[0], headers={simpMessageType=DISCONNECT, stompCommand=DISCONNECT, nativeHeaders={receipt=[close-1]}, simpSessionAttributes={}, simpHeartbeat=[J@4705fd3f, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel] true
    preSend: GenericMessage [payload=byte[0], headers={simpMessageType=DISCONNECT, stompCommand=DISCONNECT, simpSessionAttributes={}, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel]
    postSend: GenericMessage [payload=byte[0], headers={simpMessageType=DISCONNECT, stompCommand=DISCONNECT, simpSessionAttributes={}, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel] true
    afterSendCompletion: GenericMessage [payload=byte[0], headers={simpMessageType=DISCONNECT, stompCommand=DISCONNECT, simpSessionAttributes={}, simpSessionId=3f431a5e-dbd6-4a89-b527-a9d7888e3c81}] ExecutorSubscribableChannel[clientInboundChannel] true
    WebSocketSession[0 current WS(0)-HttpStream(0)-HttpPoll(0), 1 total, 0 closed abnormally (0 connect failure, 0 send limit, 0 transport error)], stompSubProtocol[processed CONNECT(1)-CONNECTED(1)-DISCONNECT(1)], stompBrokerRelay[null], inboundChannel[pool size = 15, active threads = 0, queued tasks = 0, completed tasks = 15], outboundChannel[pool size = 3, active threads = 0, queued tasks = 0, completed tasks = 3], sockJsScheduler[pool size = 1, active threads = 1, queued tasks = 0, completed tasks = 0]

    * */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("preSend: {} {}", message, channel);
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("postSend: {} {} {}", message, channel, sent);
    }

    @Override
    public void afterSendCompletion(
            Message<?> message, MessageChannel channel, boolean sent, @Nullable Exception ex) {
        log.warn("afterSendCompletion: {} {} {}", message, channel, sent, ex);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        log.info("preReceive: {}", channel);
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        log.info("postReceive: {} {}", message, channel);
        return message;
    }

    @Override
    public void afterReceiveCompletion(@Nullable Message<?> message, MessageChannel channel,
                                       @Nullable Exception ex) {
        log.warn("afterReceiveCompletion: {} {}", message, channel, ex);
    }
}
