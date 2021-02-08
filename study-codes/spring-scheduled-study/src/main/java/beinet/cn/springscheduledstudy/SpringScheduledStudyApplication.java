package beinet.cn.springscheduledstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringScheduledStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringScheduledStudyApplication.class, args);
    }

}
