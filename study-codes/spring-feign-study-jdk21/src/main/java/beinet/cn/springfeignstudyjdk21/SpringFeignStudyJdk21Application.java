package beinet.cn.springfeignstudyjdk21;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringFeignStudyJdk21Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringFeignStudyJdk21Application.class, args);
    }

}
