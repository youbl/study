package beinet.cn.frontstudy.longLoopTest;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 前端长轮询调用接口，结合异常类：LongLoopRequestTimeoutHandler 和前端页面：longloop.html
 * 无数据超时时前端会得到304，有数据时会提前返回
 *
 * @author youbl
 * @since 2024/6/1 11:02
 */
@RestController
public class LongLoopController {
    private static long TIME_OUT = 10000; // 长轮询等待时长
    // 存放监听某个Id的长轮询集合
    // 线程同步结构
    private static Map<String, DeferredResult<String>> watchRequests = new ConcurrentHashMap<>();

    /**
     * 长轮询接口
     *
     * @param id 一些参数
     * @return 轮询结果
     */
    @GetMapping("longLoop/watch")
    public DeferredResult<String> watch(@RequestParam String id) {
        Assert.isTrue(StringUtils.hasLength(id), "id must not be empty");

        DeferredResult<String> deferredResult = new DeferredResult<>(TIME_OUT);
        // 异步请求完成时移除 key，防止内存溢出
        deferredResult.onCompletion(() -> {
            watchRequests.remove(id, deferredResult);
        });
        // 注册长轮询请求
        watchRequests.put(id, deferredResult);
        return deferredResult;
    }

    /**
     * 用于前端发请求测试：通过长轮询接口，返回数据给前端
     *
     * @param id 给哪个长轮询任务返回数据
     * @return 测试结果
     */
    @GetMapping("longLoop/process")
    public String process(@RequestParam String id) {
        DeferredResult<String> deferredResult = watchRequests.get(id);
        if (deferredResult != null) {
            String ret = id + " 数据已处理：" + LocalDateTime.now();
            deferredResult.setResult(ret);
            return ret;
        }
        return id + "无轮询";
    }
}
