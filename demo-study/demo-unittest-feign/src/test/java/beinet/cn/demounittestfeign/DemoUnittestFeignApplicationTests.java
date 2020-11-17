package beinet.cn.demounittestfeign;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 指定要测试的Feign类
@SpringBootTest(classes = FeignBaidu.class)
// 导入Feign的自动装配
@Import({FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
// 继承于待测试类的Feign类
@EnableFeignClients(clients = DemoUnittestFeignApplicationTests.MockFeignBaidu.class)
class DemoUnittestFeignApplicationTests {

    @FeignClient(name = "mock-feignbaidu", url = "https://www.163.com/")
    public interface MockFeignBaidu extends FeignBaidu {

        // 首页方法不重写

        // 用 https://www.163.com/ipad mock掉 https://www.baidu.com/s 的结果
        @GetMapping(value = "ipad")
        String search(@RequestParam String wd);
    }

    @Autowired
    MockFeignBaidu feignBaidu;

    @Test
    void contextLoads() {
        String html = feignBaidu.index();
        Assert.isTrue(html.indexOf("www.baidu.com") < 0, "返回的是163，没有百度");

        html = feignBaidu.search("beinet");
        Assert.isTrue(html.indexOf("www.baidu.com") < 0, "返回的是163，没有百度");
        Assert.isTrue(html.indexOf("/ipad/") >= 0, "返回的是163的IPAD页");
    }

}
