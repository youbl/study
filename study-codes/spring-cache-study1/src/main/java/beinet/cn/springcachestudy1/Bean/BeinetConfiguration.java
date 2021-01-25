package beinet.cn.springcachestudy1.Bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * BeinetConfiguration
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/23 16:18
 */
@Configuration
public class BeinetConfiguration {
    @Bean("name1")
    RedisCacheManager createRedisCacheManager1(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> configs = new HashMap<>();
        configs.put("aaaCacheName", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(100)));
        configs.put("bbbCacheName", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(200)));
        return new RedisCacheManager(redisCacheWriter, configuration, configs);
    }

    @Bean(value = "name2", autowireCandidate = false)
    RedisCacheManager createRedisCacheManager2(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        return new RedisCacheManager(redisCacheWriter, configuration);
    }
}
