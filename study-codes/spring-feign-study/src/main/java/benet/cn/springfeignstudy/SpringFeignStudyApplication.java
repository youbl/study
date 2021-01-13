package benet.cn.springfeignstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringFeignStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringFeignStudyApplication.class, args);
    }

    @Autowired
    FeignDemo feignDemo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(feignDemo.xxx());
    }
}
