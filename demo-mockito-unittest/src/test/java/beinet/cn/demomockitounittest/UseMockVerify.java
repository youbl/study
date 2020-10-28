package beinet.cn.demomockitounittest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class UseMockVerify {

    /**
     * 方法调用次数校验
     */
    @Test
    void testMockVerifyTimes() {
        // 初始化
        BusinessService mockObj = Mockito.mock(BusinessService.class);
        HomeController controller = new HomeController(mockObj);

        // 执行业务逻辑 各1次
        controller.getSina();
        controller.getBaidu();

        // 断言，requestBaiduHtml方法 只调用了1次
        Mockito.verify(mockObj).requestBaiduHtml();// 等效于 verify(mockObj, Mockito.times(1))
        // 断言，requestSinaHtml方法 只调用了1次
        Mockito.verify(mockObj, Mockito.times(1)).requestSinaHtml();
        // 断言，requestSinaHtml方法 最少调用了1次（可以1次或n次）
        Mockito.verify(mockObj, Mockito.atLeastOnce()).requestSinaHtml();
        Mockito.verify(mockObj, Mockito.atLeast(1)).requestSinaHtml();
        // 断言，requestSinaHtml方法 最多只能调用5次（可以0次到5次）
        Mockito.verify(mockObj, Mockito.atMost(5)).requestSinaHtml();
        // 断言，requestByPara方法 没调用过
        Mockito.verify(mockObj, Mockito.never()).requestByPara("123a");

        // 执行业务逻辑
        controller.getWithPara("123");
        controller.getWithPara("123a");
        // 断言，requestByPara方法调用了2次
        Mockito.verify(mockObj, Mockito.times(2)).requestByPara(ArgumentMatchers.any());
        // 断言，requestByPara方法+参数123 只调用了1次
        Mockito.verify(mockObj).requestByPara("123");
        // 断言，requestByPara方法+参数123a 只调用了1次
        Mockito.verify(mockObj).requestByPara("123a");
        // 断言，requestByPara方法+参数123b 没调用了过
        Mockito.verify(mockObj, Mockito.never()).requestByPara("123b");
    }

    /**
     * 方法内部关系校验
     */
    @Test
    void testMockVerifyInteractions() {
        // 初始化
        BusinessService mockObj = Mockito.mock(BusinessService.class);
        HomeController controller = new HomeController(mockObj);

        // 执行业务逻辑1
        controller.getSina();
        // controller.getBaidu(); 取消这个注释就会断言失败

        // 断言，只调用了 requestSinaHtml方法，而且只调用一次
        Mockito.verify(mockObj, Mockito.only()).requestSinaHtml();
        // 等效于    verify(mock).requestSinaHtml(); + verifyNoMoreInteractions(mock);

        // 执行业务逻辑2
        controller.getBaidu();
        // 断言
        Mockito.verify(mockObj, Mockito.atLeastOnce()).requestBaiduHtml();
        // 断言，所有的计数器，都进行了断言，否则就失败
        Mockito.verifyNoMoreInteractions(mockObj);
    }

    /**
     * 异常方法也计数
     */
    @Test
    void testExp() {
        // 初始化
        BusinessService mockObj = Mockito.mock(BusinessService.class);
        HomeController controller = new HomeController(mockObj);

        // 调用原始方法
        Mockito.doCallRealMethod().when(mockObj).throwExp(ArgumentMatchers.any());

        // 执行业务逻辑
        try {
            controller.getExp();
        } catch (Exception exp) {
            System.out.println(exp);
        }
        // 改变异常
        Mockito.doThrow(RuntimeException.class).when(mockObj).throwExp(ArgumentMatchers.any());
        try {
            controller.getExp();
        } catch (Exception exp) {
            System.out.println(exp);
        }

        // 断言，没有调用过
        Mockito.verify(mockObj, Mockito.times(2)).throwExp(ArgumentMatchers.any());
    }


    @Test
    void testTimeout() {
        // 初始化
        BusinessService mockObj = Mockito.mock(BusinessService.class);
        HomeController controller = new HomeController(mockObj);
        long mainId = Thread.currentThread().getId();
        long startMills = System.currentTimeMillis();

        // 启动一个异步方法
        System.out.println("async begin test: " + costTime(startMills) + " 线程id:" + mainId);
        CompletableFuture<Void> asyncMethod = CompletableFuture.runAsync(() -> {
            long subId = Thread.currentThread().getId();
            System.out.println("async fn started: " + costTime(startMills) + " 线程id:" + subId);

            sleep(1000);
            controller.getBaidu();
            System.out.println("async fn called1: " + costTime(startMills) + " 线程id:" + subId);

            sleep(1000);
            controller.getBaidu();
            System.out.println("async fn called2: " + costTime(startMills) + " 线程id:" + subId);

            sleep(500);
            controller.getBaidu();
            System.out.println("async fn called3: " + costTime(startMills) + " 线程id:" + subId);
        });

        // 断言，在1秒内执行了1次
        Mockito.verify(mockObj, Mockito.timeout(1100)).requestBaiduHtml();
        System.out.println("async end test 1: " + costTime(startMills) + " 线程id:" + mainId);
        // 断言，在1.5秒内执行了2次
        Mockito.verify(mockObj, Mockito.timeout(3000).times(2)).requestBaiduHtml();
        System.out.println("async end test 2: " + costTime(startMills) + " 线程id:" + mainId);

        /* 注：
        timeout 是在指定时间内，只要达到要求的次数，就立即返回
        所以上面的异步方法，明明在 2.5 秒内执行了3次，但是 timeout(3000).times(2) 依然断言成功了
         */
        // 断言，在3秒后再断言，是否执行了3次
        Mockito.verify(mockObj, Mockito.after(3000).times(3)).requestBaiduHtml();
        System.out.println("async end test 3: " + costTime(startMills) + " 线程id:" + mainId);
    }

    /**
     * 计数器重置使用
     */
    @Test
    void testClear() {
        // 初始化
        BusinessService mockObj = Mockito.mock(BusinessService.class);
        HomeController controller = new HomeController(mockObj);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 执行业务逻辑
        controller.getSina();
        // 断言，没有调用过
        Mockito.verify(mockObj, Mockito.times(1)).requestSinaHtml();
        // 重置计数器，不推荐使用，内部new MockMethodInterceptor，建议自行新建一个mock对象
        Mockito.reset(mockObj);
        // 断言，没有调用过
        Mockito.verify(mockObj, Mockito.never()).requestSinaHtml();
        // 断言，没内部调用
        Mockito.verifyNoInteractions(mockObj);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 执行业务逻辑
        controller.getSina();
        // 断言，没有调用过
        Mockito.verify(mockObj, Mockito.times(1)).requestSinaHtml();
        // 清零计数器
        Mockito.clearInvocations(mockObj);
        // 断言，没内部调用
        Mockito.verifyNoInteractions(mockObj);
        // 断言，没有调用过
        Mockito.verify(mockObj, Mockito.times(0)).requestSinaHtml();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 执行业务逻辑
        controller.getSina();
        Object[] arr = Mockito.ignoreStubs(mockObj);
        Mockito.verify(mockObj, Mockito.times(1)).requestSinaHtml();
        Mockito.verifyNoMoreInteractions(arr);
        Mockito.verifyNoMoreInteractions(mockObj);
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(600); // TimeUnit.MILLISECONDS.sleep(600);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    static String costTime(long startMills) {
        long ret = (System.currentTimeMillis() - startMills);
        // 4表示4位长度，0表示不足4位补0
        return String.format("%04d", ret);
    }
}
