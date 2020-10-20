package beinet.cn.logdemoredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@SpringBootApplication
public class LogdemoRedisApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LogdemoRedisApplication.class, args);
    }

    @Qualifier("redisTemplate")
    @Autowired
    RedisTemplate redis;

    @Override
    public void run(String... args) throws Exception {
        redis.opsForValue().setIfAbsent("bbb", "ddd");
        redis.opsForValue().set("aaa", "bbb", Duration.ofMinutes(1));
        Object ret = redis.opsForValue().get("aaa");

        redis.delete("bbb");

        redis.opsForHash().get("abc", "def");
        //System.out.println("redis操作返回: " + ret);
    }
}
