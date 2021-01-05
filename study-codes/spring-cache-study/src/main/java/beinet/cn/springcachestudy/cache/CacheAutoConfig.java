package beinet.cn.springcachestudy.cache;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * CacheAutoConfig
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/5 15:38
 */
@Configuration
public class CacheAutoConfig {
    @Bean
    ObjectMapper createObjectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        // module.addDeserializer(LocalDateTime.class, new LocalDateTimeSerializerExt());

        return Jackson2ObjectMapperBuilder.json().modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)  // 禁用写时间戳
                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES) // 反序列化时，忽略大小写
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)// 忽略未知属性
                .build();
    }

    /**
     * 定义当前项目使用Redis作为缓存
     * 注：缓存在Redis里的key，是[CacheName::实际key]
     * @param redisConnectionFactory redis工厂
     * @param mapper 序列化器
     * @return 缓存管理器
     */
    @Bean
    RedisCacheManager createRedisCacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper mapper) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        // key为Cacheable的value
        redisCacheConfigurationMap.put("DayCache", createCacheConfiguration(24 * 60 * 60, mapper));
        redisCacheConfigurationMap.put("HourCache", createCacheConfiguration(60 * 60, mapper));
        redisCacheConfigurationMap.put("MinuteCache", createCacheConfiguration(60, mapper));
        redisCacheConfigurationMap.put("TenSecondCache", createCacheConfiguration(10, mapper));

        // redisCacheConfigurationMap中不存在的key，会采用这个配置
        RedisCacheConfiguration redisCacheConfiguration = createCacheConfiguration(1800, mapper);

        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration, redisCacheConfigurationMap);
    }

    private RedisCacheConfiguration createCacheConfiguration(int seconds, ObjectMapper mapper) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .entryTtl(Duration.ofSeconds(seconds));
    }
}
