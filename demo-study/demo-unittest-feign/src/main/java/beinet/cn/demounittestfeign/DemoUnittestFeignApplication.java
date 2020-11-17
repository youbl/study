package beinet.cn.demounittestfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DemoUnittestFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoUnittestFeignApplication.class, args);
    }

}
