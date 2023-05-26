package beinet.cn.springconfigclientgit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class SpringConfigClientGitApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfigClientGitApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
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
