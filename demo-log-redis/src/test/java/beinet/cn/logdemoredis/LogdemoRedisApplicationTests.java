package beinet.cn.logdemoredis;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@SpringBootTest
class LogdemoRedisApplicationTests {
    @Qualifier("redisTemplate")
    @Autowired
    RedisTemplate redis;

    @Before
    void doBefore() {

    }

    @Test
    void contextLoads() {
        redis.opsForValue().setIfAbsent("bbb", "ddd");
        redis.opsForValue().set("aaa", "bbb", Duration.ofMinutes(1));
        Object ret = redis.opsForValue().get("aaa");

        redis.delete("bbb");

        redis.opsForHash().get("abc", "def");
    }

}
