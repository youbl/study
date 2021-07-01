package beinet.cn.canalDemo;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
//启用canal
@EnableCanalClient
public class CanalDemoApplication {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
        SpringApplication.run(CanalDemoApplication.class, args);
    }
}
