package beinet.cn.springbeanstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class SpringBeanStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBeanStudyApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        Environment env = applicationContext.getEnvironment();

        // 获取yml配置里的 spring.profiles.active 的值，未配置时返回 String[0]
        System.out.println("profile:" + Arrays.toString(env.getActiveProfiles()));
        // 获取yml配置里的 spring.application.name 的值，未配置时返回 null
        System.out.println("envGetName:" + env.getProperty("spring.application.name"));
        // 返回 "" 空串
        System.out.println("contextName:" + applicationContext.getApplicationName());
        // 跟踪代码可知这个等于上面的值，类在 org\springframework\boot\context\ContextIdApplicationContextInitializer
        /* 未配置时返回 "application"
    private String getApplicationId(ConfigurableEnvironment environment) {
        String name = environment.getProperty("spring.application.name");
        return StringUtils.hasText(name) ? name : "application";
    }
        * */
        System.out.println("contextId:" + applicationContext.getId());
    }
}
