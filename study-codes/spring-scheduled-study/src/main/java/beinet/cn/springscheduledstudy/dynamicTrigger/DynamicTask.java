package beinet.cn.springscheduledstudy.dynamicTrigger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

/**
 * 自定义运行时长的计划任务类。
 * 注意：因为继承了SchedulingConfigurer，会导致配置spring.task.scheduling 失效
 * @author youbl
 * @since 2025/4/9 14:29
 */
@Component
@Slf4j
public class DynamicTask implements SchedulingConfigurer {
    @Value("${spring.task.scheduling.pool.size:5}")
    private int poolSize;
    @Value("${spring.task.scheduling.thread-name-prefix:beinet-}")
    private String threadNamePrefix;

    private long lastTime = System.currentTimeMillis();
    private long lastStartTime2 = System.currentTimeMillis();

    /**
     * 运行日志参考，修改不影响当前等待中的任务，只对任务在修改后的第2次启动生产：
     * 上次执行:1744182214667，等待时长:10001
     * 定时器间隔修改：10000 => 3000
     * 上次执行:1744182224668，等待时长:10000
     * 上次执行:1744182234668，等待时长:3001
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 初始化线程池（复用 yml 配置）
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix(threadNamePrefix);
        scheduler.initialize();
        taskRegistrar.setScheduler(scheduler);

        addFixedDelayTask(taskRegistrar);

        addCronTask(taskRegistrar);
    }

    private void addFixedDelayTask(ScheduledTaskRegistrar taskRegistrar) {
        // 根据 timerMillis 指定的时间间隔启动 dynamicTask1 任务.
        // 注意是按上次任务结束时间开始计时，等同于：fixedDelay
        taskRegistrar.addTriggerTask(
                this::dynamicTask1,
                triggerContext -> {
                    PeriodicTrigger periodicTrigger = new PeriodicTrigger(DynamicVars.getTimerMillis());
                    return periodicTrigger.nextExecutionTime(triggerContext);
                });
    }

    private void addCronTask(ScheduledTaskRegistrar taskRegistrar) {
        // 再加一个使用cron配置启动的计划任务
        taskRegistrar.addTriggerTask(
                this::dynamicTask2,
                triggerContext -> {
                    CronTrigger cronTrigger = new CronTrigger(DynamicVars.getCron());
                    return cronTrigger.nextExecutionTime(triggerContext);
                });
    }

    @SneakyThrows
    private void dynamicTask1() {
        long nowTime = System.currentTimeMillis();
        log.info("上次执行:{}，等待时长:{}", lastTime, nowTime - lastTime);
        lastTime = nowTime;
        Thread.sleep(3000L);
    }

    // 调用下面接口，可以改成每秒执行一次：
    // http://localhost:8801/updateCron?cron=*%20*%20*%20*%20*%20?
    @SneakyThrows
    private void dynamicTask2() {
        long nowTime = System.currentTimeMillis();
        log.info("task2上次执行:{}，等待时长:{}", lastStartTime2, nowTime - lastStartTime2);
        lastStartTime2 = nowTime;
    }
}
