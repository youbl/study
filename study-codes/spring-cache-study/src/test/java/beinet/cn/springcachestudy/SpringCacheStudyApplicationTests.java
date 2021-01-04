package beinet.cn.springcachestudy;

import beinet.cn.springcachestudy.service.BusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class SpringCacheStudyApplicationTests {
    @Autowired
    BusinessService businessService;

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

    void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception exp) {
        }
    }
}
