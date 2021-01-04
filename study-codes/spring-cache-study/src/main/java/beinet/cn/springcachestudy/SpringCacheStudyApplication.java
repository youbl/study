package beinet.cn.springcachestudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringCacheStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCacheStudyApplication.class, args);
    }

}
