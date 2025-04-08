package beinet.cn.springscheduledstudy;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FixedScheduledTask {

    /*
     * 注：默认只有一个Schedule线程，所以第1个方法也会被迫等7秒后执行,
     * 需要添加配置：spring.task.scheduling.pool.size=5
     *  */

    // 首次启动就执行，下次要等5秒后执行
    @Scheduled(fixedRate = 5000)
    @SneakyThrows
    void fixedRateTask() {
        log.info("fixedRateTask 5000开始");
    }

    // 首次启动就执行，因为有sleep，导致下次要等7秒后执行
    // Spring对同一个任务，默认不会并行执行，因此不会固定等5秒，而是在5秒到达且上次执行完成才启动第2次
    @Scheduled(fixedRate = 5000)
    @SneakyThrows
    void fixedRateTaskWithSleep() {
        log.info("fixedRateTaskWithSleep 5000开始");
        Thread.sleep(7000);
    }

    // 首次启动就执行，方法执行结束后，再等5秒后执行
    // 因为方法里休眠了7秒，所以第2次执行实际要等12秒
    //@Scheduled(fixedDelay = 5000)
    @SneakyThrows
    void fixedDelayTask() {
        log.info("fixedDelayTask 5000开始");
        Thread.sleep(7000);
    }
}
