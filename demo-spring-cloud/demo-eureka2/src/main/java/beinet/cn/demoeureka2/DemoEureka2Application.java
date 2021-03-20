package beinet.cn.demoeureka2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DemoEureka2Application {

    public static void main(String[] args) {
        SpringApplication.run(DemoEureka2Application.class, args);
    }

}
