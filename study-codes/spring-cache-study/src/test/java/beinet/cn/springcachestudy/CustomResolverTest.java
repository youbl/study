package beinet.cn.springcachestudy;

import beinet.cn.springcachestudy.service.CustomResolverService;
import beinet.cn.springcachestudy.service.DtoDemo;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

/**
 * CustomResolverTest
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/13 14:28
 */
@SpringBootTest
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
public class CustomResolverTest {
    @Autowired
    CustomResolverService service;
    private String name = String.valueOf(System.nanoTime());

    @Test
    public void test() {
        int id = 111;
        // 先取有Birthday的
        DtoDemo dto1 = service.get1(id, name);
        Assert.assertEquals(dto1.getName(), name);
        Assert.assertNotNull(dto1.getBirthday());

        sleep(1000);
        DtoDemo dto2 = service.get1(id, name);
        Assert.assertEquals(dto1.toString(), dto2.toString());

        dto2 = service.get2(id, name);
        Assert.assertEquals(dto1.toString(), dto2.toString());

        id = 222;
        // 先取 Birthday为null的
        dto1 = service.get2(id, name);
        Assert.assertEquals(dto1.getName(), name);
        Assert.assertNull(dto1.getBirthday());

        sleep(1000);
        dto2 = service.get1(id, name);
        Assert.assertEquals(dto1.toString(), dto2.toString());

        dto2 = service.get2(id, name);
        Assert.assertEquals(dto1.toString(), dto2.toString());
    }

    private static RedisServer redisServer;


    /**
     * 启动Redis，并在6379端口监听
     */
    @BeforeClass
    public static void startRedis() {
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
    @AfterClass
    public static void stopRedis() {
        redisServer.stop();
    }

    void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception exp) {
        }
    }
}
