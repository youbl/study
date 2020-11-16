package beinet.cn.demounittestmockredis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@RestController
public class DbController {
    private final RedisTemplate redisTemplate;

    /**
     * stringRedisTemplate使用的是json序列化，
     * 如果用 redisTemplate，用的是二进制序列化
     *
     * @param redis
     */
    public DbController(@Qualifier("stringRedisTemplate") RedisTemplate redis) {
        this.redisTemplate = redis;
    }

    /**
     * http://localhost:8801/redis?key=add&val=asfddd
     * @param key
     * @param val
     * @return
     */
    @GetMapping("redis")
    public String getFromRedis(@RequestParam String key, @RequestParam(required = false) String val) {
        String ret = "Redis读取结果: ";
        ret += redisTemplate.opsForValue().get(key);

        if (val != null && !val.isEmpty()) {
            redisTemplate.opsForValue().set(key, val, 10, TimeUnit.SECONDS);
            ret += " | 写入成功";
        }
        return ret;
    }

    @GetMapping("index")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " " + this.getClass().getName();
    }
}
