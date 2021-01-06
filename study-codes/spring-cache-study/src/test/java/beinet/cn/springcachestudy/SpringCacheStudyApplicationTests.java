package beinet.cn.springcachestudy;

import beinet.cn.springcachestudy.service.BusinessService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootTest
@ActiveProfiles("unittest")
class SpringCacheStudyApplicationTests {
    @Autowired
    BusinessService businessService;

    private RedisServer redisServer;

    /**
     * 启动Redis，并在6379端口监听
     */
    @PostConstruct
    void startRedis() {
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
    void stopRedis() {
        redisServer.stop();
    }

    @Test
    void same_cache_name() {
        int id1 = 12345;
        int id2 = 54321;

        // 不同的方法，使用相同的Cacheable value，在相同的参数时，返回值相同
        String str1 = businessService.getById1(id1);
        String str2 = businessService.getById2(id1);
        Assert.isTrue(str1.equals(str2), "--");
        Assert.isTrue(str2.contains(" getById1 " + id1), "--");

        // 在不同的参数时，返回值也不同
        String str2_2 = businessService.getById2(id2);
        Assert.isTrue(!str1.equals(str2_2), "--");
        Assert.isTrue(str2_2.contains(" getById2 " + id2), "--");

        // 使用相同的Cacheable value，但是使用不同的key时，虽然参数相同，但是返回值不同
        String str3 = businessService.getById3(id1);
        Assert.isTrue(!str1.equals(str3), "--");
        Assert.isTrue(str3.contains(" getById3 " + id1), "--");

        sleep(1000);

        // 最后确认一下缓存有效
        Assert.isTrue(businessService.getById1(id1).equals(str1), "--");
        Assert.isTrue(businessService.getById2(id1).equals(str2), "--");
        Assert.isTrue(businessService.getById3(id1).equals(str3), "--");
    }

    @Test
    public void no_cache_test() {
        int id1 = 12345;

        String str2 = businessService.getByIdNoWrite(id1);
        String str1 = businessService.getById1(id1);
        Assertions.assertNotEquals(str1, str2);
        Assertions.assertTrue(str1.contains("getById1"));
        Assertions.assertTrue(str2.contains("getByIdNoWrite"));

        str2 = businessService.getByIdNoWrite(id1);
        Assertions.assertEquals(str1, str2);
    }

    @Test
    public void get_and_clear_cache() {
        int id1 = 12345;
        int id2 = 54321;

        String str1 = businessService.getByIdWithKeyGenerator(id1);
        String str2 = businessService.getByIdWithKeyGenerator(id2);

        sleep(1000);
        Assert.isTrue(businessService.getByIdWithKeyGenerator(id1).equals(str1), "--");
        Assert.isTrue(businessService.getByIdWithKeyGenerator(id2).equals(str2), "--");

        sleep(1000);
        // 清除缓存1
        businessService.clearCache(id1);
        // 缓存不相同
        Assert.isTrue(!businessService.getByIdWithKeyGenerator(id1).equals(str1), "--");
        // 未清除，还是相等
        Assert.isTrue(businessService.getByIdWithKeyGenerator(id2).equals(str2), "--");
    }

    @Test
    public void custom_cache_time() {
        int id1 = 12345;

        String str1 = businessService.getAndCache10s(id1);
        sleep(1000);
        Assertions.assertEquals(str1, businessService.getAndCache10s(id1));
        sleep(6000);
        Assertions.assertEquals(str1, businessService.getAndCache10s(id1));
        sleep(4000);
        Assertions.assertNotEquals(str1, businessService.getAndCache10s(id1));
    }

    void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception exp) {
        }
    }
}
