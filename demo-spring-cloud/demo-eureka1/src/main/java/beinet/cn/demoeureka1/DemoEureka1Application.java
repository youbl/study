package beinet.cn.demoeureka1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DemoEureka1Application {

    public static void main(String[] args) {
        SpringApplication.run(DemoEureka1Application.class, args);
    }

}
