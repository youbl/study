package benet.cn.springfeignstudy.autoConfigs;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 启用异步，并配置Async的feign使用的默认线程池
 *
 * @author youbl
 * @since 2023/12/21 19:39
 */
@EnableAsync
@Configuration
public class AsyncPoolConfig implements AsyncConfigurer {
    public static final String ASYNC_POOL = "feignPoolExecutor";

    @Bean(ASYNC_POOL)
    public Executor getTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数，可以理解为最小线程数, 默认值1
        executor.setMaxPoolSize(10); // 允许创建的最大线程数，默认值Integer.MAX_VALUE.
        executor.setKeepAliveSeconds(60);// 线程空闲销毁时间，超过核心线程数的那些线程会被销毁，默认值60秒
        executor.setQueueCapacity(200);  // 无空闲线程时，存储待执行任务的队列最大容量，默认Integer.MAX_VALUE.
        // 如果任务队列满了，则不加入队列，由调用者线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        /*
注：RejectedExecutionHandler有4种策略：
- AbortPolicy 线程池满了，抛出异常，这是默认策略
- DiscardPolicy 线程池满了，直接丢弃任务，不抛出异常
- DiscardOldestPolicy 线程池满了，直接丢弃最老的任务，把新任务加入队列，不抛出异常
- CallerRunsPolicy 线程池满了，由主线程（调用线程）执行任务，不使用线程池
- 自定义策略，自己实现RejectedExecutionHandler接口就好
        * */

        executor.setThreadNamePrefix("feignPool-"); // 设置线程前缀名
        executor.initialize();
        return executor;
    }

    /**
     * 配置默认的Async异常处理器，即输出error日志
     *
     * @return 异常处理器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
