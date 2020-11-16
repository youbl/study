package beinet.cn.demounittestmockrabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class DemoUnittestMockrabbitmqApplicationTests {

    @Autowired
    DbController controller;

    /**
     * 注：只在内存中mock，无法跨项目，比如单元测试publish的消息，无法被其它项目收到
     */
    @Test
    void contextLoads() {
        controller.publish();
        Assert.isTrue(true, "控制台将会输出一条Listener输出的消息");
    }

}
