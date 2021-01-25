package beinet.cn.springcachestudy1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableCaching
public class SpringCacheStudy1Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringCacheStudy1Application.class, args);
    }

    @Autowired
    Environment environment;

    @Override
    public void run(String... args) throws Exception {
        // 获取 ${environment} 的值
        String key = "spring.application.name";
        String property = environment.getProperty(key);
        System.out.println(String.format("Environment#getProperty(%s) = %s", key, property));

        // 解析占位符 ${environment}
        String placeholders = environment.resolvePlaceholders("aa:${" + key + "}:bb");
        System.out.println(String.format("%s", placeholders));
    }
}
