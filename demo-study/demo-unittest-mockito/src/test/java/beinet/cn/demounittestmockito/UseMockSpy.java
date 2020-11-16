package beinet.cn.demounittestmockito;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

// 官方说明 https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Spy.html
//      或 https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#spy-T-
@SpringBootTest
@ActiveProfiles("unittest")
class UseMockSpy {

    // spy与mock的区别是，没有mock的情况下，会直接调用真实对象方法
    @Spy
    private BusinessService businessService;

    /**
     * 使用注解定义的mock对象
     */
    @Test
    void testMockBeanMethod() {
        DbController controller = new DbController(businessService);
        testBeanMethod(businessService, controller);
    }

    /**
     * 方法里直接生成mock代理对象
     */
    @Test
    void testMockDirectMethod() {
        BusinessService service = Mockito.spy(BusinessService.class);
        DbController controller = new DbController(service);
        testBeanMethod(service, controller);
    }

    static void testBeanMethod(BusinessService businessService, DbController controller) {
        // 对 requestBaiduHtml 方法进行mock
        Mockito.when(businessService.requestBaiduHtml()).thenReturn("我是Mock后的百度");

        // 测试 调用mock方法
        String ret = controller.getBaidu();
        Assert.isTrue(ret.equals("我是Mock后的百度"), "mock失败？");

        // 测试 调用没有mock的方法，会直接调用原始方法
        ret = controller.getSina();
        Assert.isTrue(ret.equals("我是新浪"), "啥情况？");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 对requestByPara，且参数为123进行mock
        Mockito.when(businessService.requestByPara("123")).thenReturn("mock参数123");
        // 测试 调用requestByPara("123")
        ret = controller.getWithPara("123");
        Assert.isTrue(ret.equals("mock参数123"), "啥情况？");
        // 测试 调用requestByPara 未mock的参数，调用原始方法
        ret = controller.getWithPara("456");
        Assert.isTrue(ret.equals("我收到参数:456"), "啥情况？");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 对requestByPara，任意字符串参数 进行mock
        Mockito.when(businessService.requestByPara(ArgumentMatchers.anyString())).thenAnswer(invocationOnMock -> {
            Object arg = invocationOnMock.getArgument(0);
            if (arg == null || arg.toString().isEmpty()) {
                return "Mock收到空值";
            }

            Pattern pattern = Pattern.compile("^\\d+$");
            if (pattern.matcher(arg.toString()).find()) {
                return "Mock掉数值调用:" + arg;
            }
            // 不是空，也不是数值，直接调用源方法
            return invocationOnMock.callRealMethod();
        });

        // 测试 null参数的mock, anyString()不支持null参数，会调用原始方法
        ret = controller.getWithPara(null);
        Assert.isTrue(ret.equals("我收到参数:isNULL"), "啥情况？");
        // 测试 empty参数的mock
        ret = controller.getWithPara("");
        Assert.isTrue(ret.equals("Mock收到空值"), "啥情况？");
        // 测试 Answer逻辑里的数值参数
        ret = controller.getWithPara("123");
        Assert.isTrue(ret.equals("Mock掉数值调用:123"), "啥情况？");
        // 测试 Answer逻辑里的非数值参数
        ret = controller.getWithPara("123a");
        Assert.isTrue(ret.equals("我收到参数:123a"), "啥情况？");
    }


}
