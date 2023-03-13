package beinet.cn.demolograbbitmq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
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

import java.util.Arrays;
import java.util.List;

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
                    SimpleRabbitListenerContainerFactory factory = (SimpleRabbitListenerContainerFactory) bean;
                    Advice myAdvice = new RabbitAdvice();
                    Advice[] adviceList = factory.getAdviceChain();
                    
                    int len = adviceList == null ? 0 : adviceList.length;
                    if (len <= 0) {
                        adviceList = new Advice[]{myAdvice};
                    } else {
                        adviceList = Arrays.copyOf(adviceList, len + 1);
                        adviceList[len] = myAdvice;
                    }
                    factory.setAdviceChain(adviceList);
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
