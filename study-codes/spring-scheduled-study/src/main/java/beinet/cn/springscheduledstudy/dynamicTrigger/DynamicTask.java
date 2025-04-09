package beinet.cn.springscheduledstudy.dynamicTrigger;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

/**
 * 自定义运行时长的计划任务类。
 * 注意：因为继承了SchedulingConfigurer，会导致配置spring.task.scheduling 失效，不推荐
 * @author youbl
 * @since 2025/4/9 14:29
 */
@Component
@Slf4j
@Setter
@Getter
public class DynamicTask implements SchedulingConfigurer {
    private Long timerMillis = 10000L;

    private long lastTime = System.currentTimeMillis();

    /**
     * 运行日志参考，修改不影响当前等待中的任务，只对任务在修改后的第2次启动生产：
     * 上次执行:1744182214667，等待时长:10001
     * 定时器间隔修改：10000 => 3000
     * 上次执行:1744182224668，等待时长:10000
     * 上次执行:1744182234668，等待时长:3001
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 根据 timerMillis 指定的时间间隔启动 dynamicTask1 任务
        taskRegistrar.addTriggerTask(
                this::dynamicTask1,
                triggerContext -> {
                    PeriodicTrigger periodicTrigger = new PeriodicTrigger(timerMillis);
                    return periodicTrigger.nextExecutionTime(triggerContext);
                });
    }

    private void dynamicTask1() {
        long nowTime = System.currentTimeMillis();
        log.info("上次执行:{}，等待时长:{}", lastTime, nowTime - lastTime);
        lastTime = nowTime;
    }
}
