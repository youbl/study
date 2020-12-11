package beinet.cn.demogitlaboperation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DemoGitlabOperationApplication {

    // 演示代码在test下
    public static void main(String[] args) {
        SpringApplication.run(DemoGitlabOperationApplication.class, args);
    }

}
