package beinet.cn.demoredisunittest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootTest
@ActiveProfiles("test") // 使用yml里的test配置
class DemoRedisUnittestApplicationTests {

    private RedisServer redisServer;

    /**
     * 启动Redis
     */
    @PostConstruct
    public void startRedis() {
        // https://github.com/kstyrc/embedded-redis/issues/51
        redisServer = RedisServer.builder()
                .port(6379)
                .setting("maxmemory 128M") //maxheap 128M
                .build();

        redisServer.start();
    }

    /**
     * 析构方法之后执行，停止Redis.
     */
    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }


    @Autowired
    DemoController controller;

    @Test
    void contextLoads() {
        String ret = controller.getFromRedis("aa", null);
        Assert.isTrue(ret.contains("null"), "测试失败");

        ret = controller.getFromRedis("aa", "abcdHHH");
        Assert.isTrue(ret.contains("null"), "测试失败");

        ret = controller.getFromRedis("aa", null);
        Assert.isTrue(ret.contains("abcdHHH"), "测试失败");
    }

}
