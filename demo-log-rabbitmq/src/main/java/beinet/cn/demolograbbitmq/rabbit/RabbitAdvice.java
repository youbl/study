package beinet.cn.demolograbbitmq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;

/**
 * 打印所有RabbitMQ消费者日志的拦截器
 */
@Slf4j
public class RabbitAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!log.isDebugEnabled())
            return invocation.proceed();

        long start = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            long cost = System.currentTimeMillis() - start;
            StringBuilder sb = new StringBuilder("收到消息, 处理耗时:");
            sb.append(cost).append("ms\r\n");
            try {
                Object[] args = invocation.getArguments();
                for (Object obj : args) {
                    sb.append(obj).append("\r\n");
                    if (obj instanceof Message) {
                        sb.append("body: ").append(new String(((Message) obj).getBody())).append("\r\n");
                    }
                }
                log.debug(sb.toString());
            } catch (Exception exp) {
                sb.append("Exception: ").append(exp.getMessage()).append("\r\n");
                log.error(sb.toString());
            }
        }
    }
}