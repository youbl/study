package beinet.cn.demoredisunittest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
//@RequestMapping("home")
public class DemoController {
    private final RedisTemplate redisTemplate;

    public DemoController(@Qualifier("stringRedisTemplate") RedisTemplate redis) {
        this.redisTemplate = redis;
    }

    @GetMapping("index")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @GetMapping("redis")
    public String getFromRedis(@RequestParam String key, @RequestParam(required = false) String val) {
        String ret = "Redis读取结果: ";
        ret += redisTemplate.opsForValue().get(key);

        if (val != null && !val.isEmpty()) {
            redisTemplate.opsForValue().set(key, val);
            ret += " | 写入成功";
        }
        return ret;
    }
}
