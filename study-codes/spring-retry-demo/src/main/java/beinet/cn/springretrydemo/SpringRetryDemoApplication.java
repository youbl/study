package beinet.cn.springretrydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class SpringRetryDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRetryDemoApplication.class, args);
    }

}
