package beinet.cn.springscheduledstudy;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FixedScheduledTask {

    /* 注：默认只有一个Schedule线程，所以第1个方法也会被迫等7秒后执行 */

    // 首次启动就执行，下次要等5秒后执行
    @Scheduled(fixedRate = 5000)
    @SneakyThrows
    void fixedRateTask() {
        log.info("fixedRateTask 5000开始");
    }

    // 首次启动就执行，因为有sleep，导致下次要等7秒后执行
    @Scheduled(fixedRate = 5000)
    @SneakyThrows
    void fixedRateTaskWithSleep() {
        log.info("fixedRateTaskWithSleep 5000开始");
        Thread.sleep(7000);
    }
}
