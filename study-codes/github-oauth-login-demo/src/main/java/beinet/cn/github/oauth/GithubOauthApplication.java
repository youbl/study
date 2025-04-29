package beinet.cn.github.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 新类
 * @author youbl
 * @since 2025/4/29 16:40
 */
@SpringBootApplication
@EnableFeignClients
public class GithubOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubOauthApplication.class, args);
    }

}