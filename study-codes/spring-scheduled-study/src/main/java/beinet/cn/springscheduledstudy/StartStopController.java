package beinet.cn.springscheduledstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.Task;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
public class StartStopController {
    private Set<Object> allScheduledBeans = new HashSet<>();

    private ScheduledAnnotationBeanPostProcessor postProcessor;

    private MyScheduledTask scheduledTask;

    public StartStopController(ScheduledAnnotationBeanPostProcessor postProcessor,
                               MyScheduledTask scheduledTask) {
        this.postProcessor = postProcessor;
        this.scheduledTask = scheduledTask;

        init();
    }

    private void init() {
        Set<ScheduledTask> tasks = postProcessor.getScheduledTasks();
        for (ScheduledTask task : tasks) {
            Task ttt = task.getTask();
            Runnable aaa = ttt.getRunnable();
            Object target = ((ScheduledMethodRunnable) aaa).getTarget();
            System.out.println(target);

            allScheduledBeans.add(target);
        }
    }

    @GetMapping("/start")
    public String start() {
        if (!allScheduledBeans.add(scheduledTask)) {
            return LocalDateTime.now() + " already started, didn't need start.";
        }
        postProcessor.postProcessAfterInitialization(scheduledTask, null);

        return LocalDateTime.now() + " START OK";
    }

    // 停止指定类的所有计划任务
    @GetMapping("/stop")
    public String stop() {
        if (!allScheduledBeans.remove(scheduledTask)) {
            return LocalDateTime.now() + " already stoped, didn't need stop.";
        }
        postProcessor.postProcessBeforeDestruction(scheduledTask, null);
        String str = LocalDateTime.now() + " STOP OK";
        System.out.println(str);
        return str;
    }
}
