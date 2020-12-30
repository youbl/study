package beinet.cn.springjpastudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringJpaStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaStudyApplication.class, args);
    }

}
