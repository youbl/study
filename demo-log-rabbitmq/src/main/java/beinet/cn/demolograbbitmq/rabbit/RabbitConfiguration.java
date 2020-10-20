package beinet.cn.demolograbbitmq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
public class RabbitConfiguration implements BeanPostProcessor {
    Environment env;

    public RabbitConfiguration(Environment environment) {
        this.env = environment;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        switch (beanName) {
            // (bean instanceof SimpleRabbitListenerContainerFactory) 如果项目未引用mq，这里会报错：NoClassDefFoundError
            case "rabbitListenerContainerFactory":
                // 修改原有Bean，避免new SimpleRabbitListenerContainerFactory 出现问题
                if (!StringUtils.isEmpty(env.getProperty("logging.level.beinet.cn.demolograbbitmq.rabbit.RabbitAdvice"))) {
                    ((SimpleRabbitListenerContainerFactory) bean).setAdviceChain(new RabbitAdvice());
                }

                ((SimpleRabbitListenerContainerFactory) bean).setMessageConverter(new Jackson2JsonMessageConverter());
                break;

            case "rabbitTemplate":
                // 默认的 SimpleMessageConverter only supports String, byte[] and Serializable payloads
                ((RabbitTemplate) bean).setMessageConverter(new Jackson2JsonMessageConverter());
                break;
        }
        return bean;
    }
}
