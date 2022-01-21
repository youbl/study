package beinet.cn.springkafkastudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringKafkaStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaStudyApplication.class, args);
    }

}
