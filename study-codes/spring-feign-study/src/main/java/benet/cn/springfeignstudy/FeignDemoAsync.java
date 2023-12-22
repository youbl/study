package benet.cn.springfeignstudy;

import benet.cn.springfeignstudy.autoConfigs.AsyncPoolConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 基于FeignDemo的异步实现
 *
 * @author youbl
 * @since 2023/12/21 21:03
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FeignDemoAsync {
    private final FeignDemo feignDemo;

    @Async(AsyncPoolConfig.ASYNC_POOL)
    public String xxx() {
        log.info("异步启动: {}", Thread.currentThread().getName());
        String ret = feignDemo.xxx();
        log.info("调用结束:{} 接口返回:{}", Thread.currentThread().getName(), ret);
        return ret;
    }

    @Async(AsyncPoolConfig.ASYNC_POOL)
    public Future<String> xxx2() {
        log.info("异步启动: {}", Thread.currentThread().getName());
        String ret = feignDemo.xxx();
        log.info("调用结束:{} 接口返回:{}", Thread.currentThread().getName(), ret);

        // 返回future对象，方便外部可以进行阻塞获取结果
        return CompletableFuture.completedFuture(ret);
    }
}
