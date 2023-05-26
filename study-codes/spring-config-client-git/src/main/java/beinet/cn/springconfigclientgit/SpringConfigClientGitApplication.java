package beinet.cn.springconfigclientgit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class SpringConfigClientGitApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfigClientGitApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*
单个应用刷新
假设需要刷新配置的应用，spring.application.name=cb-admin
1、修改配置，并提交到git（注意提交到正确的分支）
2、调用配置中心API，通知刷新，调用方式：
curl -X POST http://localhost:8999/lastTime?appName=cb-admin
3、OK，1分钟左右，该应用就会完成配置刷新
所有应用刷新
如果希望所有应用同时刷新，需要调用另一个API：
curl -X POST http://localhost:8999/lastGlobalTime
        * */
        
        while (true) {
            AbstractEnvironment env = SpringUtils.getBean(AbstractEnvironment.class);
            System.out.println("===" + env.getProperty("test.config1") + "===");
            System.out.println("===" + env.getProperty("test.config2") + "===");
            System.out.println("===" + env.getProperty("test.config3") + "===");
            System.out.println("===" + env.getProperty("test.config4") + "===");
            Thread.sleep(10000);
        }
    }
}
