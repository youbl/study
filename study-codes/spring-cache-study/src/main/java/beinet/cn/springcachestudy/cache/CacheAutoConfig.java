package beinet.cn.springcachestudy.cache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

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
@Slf4j
@Configuration
public class CacheAutoConfig {

    // 在Spring5.1（不含5.1）之前的版本，Bean没有autowireCandidate属性，要通过setAutowireCandidate处理
    // java.lang.IllegalStateException: No CacheResolver specified, and no unique bean of type CacheManager found. Mark one as primary (or give it the name 'cacheManager') or declare a specific CacheManager to use, that serves as the default one.
    // public CacheAutoConfig(DefaultListableBeanFactory factory) {
    //    factory.getBeanDefinition(BeinetCacheEnv.BEAN_NAME).setAutowireCandidate(false);
    // }

    /**
     * 定义当前项目使用Redis作为缓存
     * 注：缓存在Redis里的key，是[CacheName::实际key]
     *
     * @return 缓存管理器
     */
    @Bean(value = BeinetCacheEnv.BEAN_NAME, autowireCandidate = false)
    RedisCacheManager createRedisCacheManager(Environment env) { // 如果要使用默认的redis配置，这里参数可以是 RedisConnectionFactory redisConnectionFactory
        RedisConnectionFactory redisConnectionFactory = initConnection(env);

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        addCacheTimeConfig(redisCacheConfigurationMap);

        // redisCacheConfigurationMap中不存在的key，会采用这个配置
        RedisCacheConfiguration defaultConfig = redisCacheConfigurationMap.get(BeinetCacheTime.HALF_HOUT);

        return new RedisCacheManager(redisCacheWriter, defaultConfig, redisCacheConfigurationMap);
    }

    // 使用全局统一的Redis配置
    private RedisConnectionFactory initConnection(Environment env) {
        // 单实例redis
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        // 哨兵redis 用 new RedisSentinelConfiguration();
        // 集群redis 用 new RedisClusterConfiguration();

        String host = env.getProperty("beinet.sdk.redis.host", "");
        String pwd = env.getProperty("beinet.sdk.redis.password", "");
        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(pwd)) {
            host = "127.0.0.1";
            pwd = "";
            //throw new IllegalArgumentException("'beinet.sdk.redis' info can't be empty. ");
        }

        int port = env.getProperty("beinet.sdk.redis.port", Integer.class, 6379);
        int db = env.getProperty("beinet.sdk.redis.database", Integer.class, 0);
        int timeout = env.getProperty("beinet.sdk.redis.timeout", Integer.class, 1000);
        boolean useSsl = env.getProperty("beinet.sdk.redis.ssl", Boolean.class, false);

        redisConfig.setHostName(host);
        redisConfig.setPassword(RedisPassword.of(pwd));
        redisConfig.setPort(port);
        redisConfig.setDatabase(db);

//        int maxActive = env.getProperty("spring.redis.jedis.pool.max-active", Integer.class, 8);
//        int maxWait = env.getProperty("spring.redis.jedis.pool.max-wait", Integer.class, -1);
//        int maxIdel = env.getProperty("spring.redis.jedis.pool.max-idle", Integer.class, 8);
//        int minIdel = env.getProperty("spring.redis.jedis.pool.min-idle", Integer.class, 0);
//        org.apache.commons.pool2.impl.GenericObjectPoolConfig poolConfig = new org.apache.commons.pool2.impl.GenericObjectPoolConfig();
//        poolConfig.setMaxTotal(maxActive);
//        poolConfig.setMaxIdle(maxIdel);
//        poolConfig.setMaxWaitMillis(maxWait);
//        poolConfig.setMinIdle(minIdel);

        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration
                .builder()
                // .poolConfig(poolConfig)
                .commandTimeout(Duration.ofMillis(timeout));
        if (useSsl)
            builder.useSsl();
        LettuceClientConfiguration config = builder.build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, config);
        factory.afterPropertiesSet();
        log.info("GlobalBeinetCache inited to redis: " + host + ':' + port + " db:" + db);
        return factory;
//        RedisTemplate redis = new RedisTemplate();
//        redis.setConnectionFactory(factory);
//        redis.afterPropertiesSet();
//
//        return redis;
    }


    private void addCacheTimeConfig(Map<String, RedisCacheConfiguration> redisCacheConfigurationMap) {
        // key为Cacheable的value
        redisCacheConfigurationMap.put(BeinetCacheTime.MONTH, createCacheConfiguration(30 * 24 * 60 * 60));
        redisCacheConfigurationMap.put(BeinetCacheTime.DAY, createCacheConfiguration(24 * 60 * 60));
        redisCacheConfigurationMap.put(BeinetCacheTime.HOUR, createCacheConfiguration(60 * 60));
        redisCacheConfigurationMap.put(BeinetCacheTime.HALF_HOUT, createCacheConfiguration(30 * 60));
        redisCacheConfigurationMap.put(BeinetCacheTime.MINUTE, createCacheConfiguration(60));
        redisCacheConfigurationMap.put(BeinetCacheTime.TEN_SECOND, createCacheConfiguration(10));
    }

    private RedisCacheConfiguration createCacheConfiguration(int seconds) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(createRedisSerializer()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .entryTtl(Duration.ofSeconds(seconds))
                .computePrefixWith(cacheName -> "bn:" + cacheName + ":");// 拼接全局统一的缓存key前缀
    }


    private GenericJackson2JsonRedisSerializer createRedisSerializer() {
        ObjectMapper mapper = createObjectMapper(true);
        return new GenericJackson2JsonRedisSerializer(mapper);
    }

    /**
     * 创建序列化类
     *
     * @param enableTypeing 序列化结果是否包含类型
     * @return ObjectMapper
     */
    private ObjectMapper createObjectMapper(boolean enableTypeing) {
        JavaTimeModule module = new JavaTimeModule();
        // module.addDeserializer(LocalDateTime.class, new LocalDateTimeSerializerExt());

        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
                .modules(module, new SimpleModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)  // 禁用写时间戳，Could not read JSON: Cannot construct instance of `java.time.LocalDateTime`
                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES) // 反序列化时，忽略大小写
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)// 忽略未知属性
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                // .featuresToDisable(MapperFeature.USE_ANNOTATIONS)
                .featuresToEnable()
                .build();

        if (enableTypeing) {
            // 没有这一句，会抛异常：java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to xxx
            mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
        return mapper;
    }
}
