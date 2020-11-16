package beinet.cn.demounittestjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoUnittestJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoUnittestJpaApplication.class, args);
    }

}
