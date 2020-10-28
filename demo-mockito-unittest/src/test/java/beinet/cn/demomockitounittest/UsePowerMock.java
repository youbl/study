package beinet.cn.demomockitounittest;

//import org.junit.jupiter.api.Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.util.Assert;

/**
 * 官方说明 https://github.com/powermock/powermock
 * 在mokito上进行增加，可以mock 静态方法、私有方法、final方法、enum、构造函数等等
 * <p>
 * 注意：要使用PowerMockito，必须使用org.junit.Test，且class和method都必须是public
 * 参考：https://github.com/powermock/powermock/issues/1030
 */
// 启用PwoerMockRunner
@RunWith(PowerMockRunner.class)
// 需要测试的类，都要列在 PrepareForTest 这里
@PrepareForTest({BusinessService.class, BusinessService.EnumDemo.class})
public class UsePowerMock {

    /**
     * 静态方法Mock测试
     */
    @Test
    public void testMockStatic() {
        String name = "王霸";
        Assert.isTrue(BusinessService.getHello1(name).equals("Hello1, 王霸"), "原生有问题？");

        PowerMockito.mockStatic(BusinessService.class);
        PowerMockito.when(BusinessService.getHello1(ArgumentMatchers.any())).thenReturn("你好");
        Assert.isTrue(BusinessService.getHello1("").equals("你好"), "mock有问题？");
    }

    /**
     * 构造函数Mock测试
     */
    @Test
    public void testMockConstructor() throws Exception {
        BusinessService mockObj = PowerMockito.mock(BusinessService.class);
        PowerMockito.when(mockObj.requestSinaHtml()).thenReturn("Mock新浪");

        // 如果创建 BusinessService对象时，传递了参数 abc，则返回mock对象
        PowerMockito.whenNew(BusinessService.class).withArguments("abc").thenReturn(mockObj);

        // 创建对象，不符合mock条件，创建会失败
        BusinessService noMock = new BusinessService("abcd");
        Assert.isTrue(noMock == null, "原生方法？");

        // 创建对象，符合mock条件
        BusinessService withMock = new BusinessService("abc");
        Assert.isTrue(withMock.requestSinaHtml().equals("Mock新浪"), "Mock方法？");
    }


    /**
     * 私有方法Mock测试
     */
    @Test
    public void testMockPrivate() throws Exception {
        BusinessService mockObj = PowerMockito.spy(new BusinessService());

        // 没mock前，调用原生方法
        String tmp = mockObj.getHello2("ab");
        Assert.isTrue(tmp.equals("Hello2, ab"), "原生有问题？");

        // mock私有方法
        PowerMockito.when(mockObj, "getHelloPrivate", Mockito.anyString()).thenReturn("abc");

        tmp = mockObj.getHello2("ab");
        Assert.isTrue(tmp.equals("abc"), "mock有问题？");
    }


    /**
     * final方法Mock测试
     */
    @Test
    public void testMockFinal() {
        BusinessService mockObj = PowerMockito.spy(new BusinessService());

        // 没mock前，调用原生方法
        String tmp = mockObj.getHelloFinal("ab");
        Assert.isTrue(tmp.equals("Hello, Final ab"), "原生有问题？");

        // mock final方法
        PowerMockito.when(mockObj.getHelloFinal(Mockito.anyString())).thenReturn("abc");

        tmp = mockObj.getHelloFinal("ab");
        Assert.isTrue(tmp.equals("abc"), "mock有问题？");
    }

    /**
     * enum Mock测试
     */
    @Test
    public void testMockEnum() {
        BusinessService.EnumDemo mockObj = PowerMockito.mock(BusinessService.EnumDemo.class);

        // 没mock前，调用原生方法
        String tmp = BusinessService.EnumDemo.enum2.getTitle();
        Assert.isTrue(tmp.equals("ENUM2"), "原生有问题？");

        // mock enum2 方法
        Whitebox.setInternalState(BusinessService.EnumDemo.class, "enum2", mockObj);
        PowerMockito.when(mockObj.getTitle()).thenReturn("abc");

        // 返回mock后的值
        tmp = BusinessService.EnumDemo.enum2.getTitle();
        Assert.isTrue(tmp.equals("abc"), "mock有问题？");

        // 未mock
        tmp = BusinessService.EnumDemo.enum3.getTitle();
        Assert.isTrue(tmp.equals("ENUM3"), "mock有问题？");
    }
}
