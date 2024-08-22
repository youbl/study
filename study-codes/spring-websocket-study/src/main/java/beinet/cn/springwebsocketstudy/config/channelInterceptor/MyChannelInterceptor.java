package beinet.cn.springwebsocketstudy.config.channelInterceptor;

import org.springframework.messaging.support.ChannelInterceptor;

/**
 * 定义自己的接口，方便在Config里注入这个接口，而不是注入ChannelInterceptor，导致其它问题
 *
 * @author youbl
 * @since 2024/8/22 16:01
 */
public interface MyChannelInterceptor extends ChannelInterceptor {
}
