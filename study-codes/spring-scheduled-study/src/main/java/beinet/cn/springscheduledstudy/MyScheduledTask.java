package beinet.cn.springscheduledstudy;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MyScheduledTask {
    @Scheduled(cron = "* * * * * *")
    void timed111() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        System.out.println(now + " Timed111 method is runing. " + Thread.currentThread().getId());
        long i = 0;
        while (i < 10000000000L) {
            i++;
        }
//        try {
//            Thread.sleep(5000);
//        } catch (Exception exp) {
//            Thread.currentThread().interrupt();
//        }
        System.out.println(now + " Timed111 method is runed. ");
    }

//    @Scheduled(cron = "*/2 * * * * *")
//    void timed222() {
//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
//        System.out.println(now + " Timed22222 method is runed. " + Thread.currentThread().getId());
//    }
//
//    @Scheduled(fixedDelay = 3000)
//    void timedFixed() {
//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
//        System.out.println(now + " timedFixed method is runed. " + Thread.currentThread().getId());
//    }
}
