package beinet.cn.springconfigservergit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SpringConfigServerGitApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfigServerGitApplication.class, args);
    }

    @Autowired
    Environment env;

    @Override
    public void run(String... args) throws Exception {
        String appName = env.getProperty("spring.application.name");
        System.out.println(appName);

        System.out.println(env.getProperty("beinet.config"));
    }
}
