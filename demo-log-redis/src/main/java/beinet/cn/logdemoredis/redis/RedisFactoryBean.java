package beinet.cn.logdemoredis.redis;

import beinet.cn.logdemoredis.redis.util.ProxyUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Configuration
public class RedisFactoryBean implements BeanPostProcessor {

    private Environment env;

    public RedisFactoryBean(Environment environment) {
        this.env = environment;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (beanName.equals("redisConnectionFactory")) {
            // 使用日志代理，覆盖原Redis工厂类
            //if (!StringUtils.isEmpty(env.getProperty("logging.level." + RedisFactoryBean.class.getName()))) {
            return ProxyUtils.getProxy(bean, invocation -> new RedisAdvice().interceptorRedisFactory(invocation));
            //}
        } else if (bean instanceof RedisTemplate) {
            // 避免默认的序列化方式，导致进入Redis的字符串无法直接辨识
            initSerializer((RedisTemplate) bean);
        }
        return bean;
    }

    private static void initSerializer(RedisTemplate redis1) {
        // Key用StringRedisSerializer，避免写入Redis的Key和Value，前缀都会出现 \xAC\xED\x00\x05t\x00\x03
        RedisSerializer keySerializer = new StringRedisSerializer();
        redis1.setKeySerializer(keySerializer);
        redis1.setHashKeySerializer(keySerializer);

        BeinetJsonRedisSerializer valSerializer = new BeinetJsonRedisSerializer(Object.class);
        redis1.setValueSerializer(valSerializer);
        redis1.setHashValueSerializer(valSerializer);
    }

    @Slf4j
    private static class BeinetJsonRedisSerializer<T> implements RedisSerializer<T> {

        protected static ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

        private Class<T> clazz;

        public BeinetJsonRedisSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }

        @Override
        public byte[] serialize(T t) {
            try {
                return mapper.writeValueAsBytes(t);
            } catch (JsonProcessingException e) {
                log.error(e.toString());
                return null;
            }
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            try {
                return mapper.readValue(bytes, clazz);
            } catch (IOException e) {
                log.error(e.toString());
                return null;
            }
        }

    }
}
