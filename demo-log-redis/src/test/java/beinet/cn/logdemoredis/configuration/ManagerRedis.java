package beinet.cn.logdemoredis.configuration;

import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * 单元测试用的Redis服务器
 */
@Configuration
public class ManagerRedis {
    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        // https://github.com/kstyrc/embedded-redis/issues/51
        redisServer = RedisServer.builder()
                .port(6379)
                .setting("maxmemory 128M") //maxheap 128M
                .build();

        redisServer.start();
    }
    /**
     * 析构方法之后执行.
     */
    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
