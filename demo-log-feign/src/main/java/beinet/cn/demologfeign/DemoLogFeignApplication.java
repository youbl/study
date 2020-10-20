package beinet.cn.demologfeign;

import beinet.cn.demologfeign.feign.FeignBaidu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DemoLogFeignApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoLogFeignApplication.class, args);
    }

    @Autowired
    FeignBaidu feign;

    @Override
    public void run(String... args) throws Exception {
        feign.getHome();
    }
}
