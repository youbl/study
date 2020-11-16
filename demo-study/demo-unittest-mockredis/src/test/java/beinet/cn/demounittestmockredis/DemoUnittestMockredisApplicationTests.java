package beinet.cn.demounittestmockredis;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import redis.embedded.RedisServer;

@SpringBootTest
@ActiveProfiles("unittest")
class DemoUnittestMockredisApplicationTests {
    private static RedisServer redisServer;

    /**
     * 启动Redis，并在6379端口监听
     */
    @BeforeAll
    static void startRedis() {
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
    @AfterAll
    static void stopRedis() {
        redisServer.stop();
    }

    @Autowired
    DbController controller;


    @Test
    void contextLoads() throws InterruptedException {
        // 读取Redis，新初始化的Redis肯定是返回空值
        String ret = controller.getFromRedis("aa", null);
        Assert.isTrue(ret.contains("null"), "测试失败");

        // 写入，并返回旧值，肯定是空值
        ret = controller.getFromRedis("aa", "abcdHHH");
        Assert.isTrue(ret.contains("null"), "测试失败");

        // 读取刚刚写入的值
        ret = controller.getFromRedis("aa", null);
        Assert.isTrue(ret.contains("abcdHHH"), "测试失败");

        Thread.sleep(10000);
        // 10秒后读取刚刚写入的值
        ret = controller.getFromRedis("aa", null);
        Assert.isTrue(ret.contains("null"), "测试失败");

    }

}
