package beinet.cn.frontstudy.longLoopTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 新类
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
        DeferredResult<String> deferredResult = new DeferredResult<>(TIME_OUT);
        // 异步请求完成时移除 key，防止内存溢出
        deferredResult.onCompletion(() -> {
            watchRequests.remove(id, deferredResult);
        });
        // 注册长轮询请求
        watchRequests.put(id, deferredResult);
        return deferredResult;
    }

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
