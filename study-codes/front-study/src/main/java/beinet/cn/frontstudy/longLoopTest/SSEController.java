package beinet.cn.frontstudy.longLoopTest;

import lombok.SneakyThrows;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于Server-Sent Events (SSE) 的Demo类
 * 注：ChatGPT使用的就是SSE协议
 *
 * @author youbl
 * @since 2024/6/27 10:36
 */
@RestController
public class SSEController {
    // 存放监听某个Id的SSE请求集合
    // 线程同步结构
    private static Map<String, SseEmitter> watchRequests = new ConcurrentHashMap<>();

    /**
     * SSE请求接口
     *
     * @param id 定义一个id，方便后端通过id找到对应的前端，进行数据响应
     * @return SSE结果类
     */
    @GetMapping("sse/watch")
    public SseEmitter watch(@RequestParam String id) {
        Assert.isTrue(StringUtils.hasLength(id), "id must not be empty");

        SseEmitter sseEmitter = new SseEmitter();
        sseEmitter.onCompletion(() -> complete(id, "ok"));
        sseEmitter.onTimeout(() -> complete(id, "ok"));
        watchRequests.put(id, sseEmitter);

        return sseEmitter;
    }

    private void complete(String id, String msg) {
        watchRequests.remove(id);// 移除
        System.out.println("watch complete:" + msg);
    }

    /**
     * 用于前端发请求测试：通过上面的sse接口，返回数据给前端
     *
     * @param id 给哪个sse任务返回数据
     * @return 测试结果
     */
    @SneakyThrows
    @GetMapping("sse/process")
    public String process(@RequestParam String id) {
        long now = System.currentTimeMillis();
        SseEmitter sseEmitter = watchRequests.get(id);
        if (sseEmitter != null) {
            String ret = id + " processed ok:" + now;
            SseEmitter.SseEventBuilder body = SseEmitter.event()
                    .id(String.valueOf(now))
                    .name("message") // 只能是message，不允许是其它值，否则前端onmessage方法不会触发
                    .data(ret)
                    .data("第2行数据")    // 多个data之前会以 \n 进行分隔
                    .data("第3行数据")
                    .comment("这是注释内容")     // 不会下发给客户端
                    .reconnectTime(5000);
            sseEmitter.send(body);
            return ret;
        }
        return id + "未找到任务";
    }
}
