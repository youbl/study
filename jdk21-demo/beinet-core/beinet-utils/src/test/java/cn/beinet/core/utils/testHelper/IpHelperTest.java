package cn.beinet.core.utils.testHelper;

import cn.beinet.core.utils.UtilsTestApplication;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UtilsTestApplication.class)
public class IpHelperTest {
    @Test
    public void testDemo() {
        var ts = System.currentTimeMillis();
        Assert.assertTrue(ts > 1);
    }
}