package beinet.cn.springscheduledstudy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class SpringScheduledStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        log.info("程序启动");

        SpringApplication.run(SpringScheduledStudyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("程序初始化完成");
    }
}
