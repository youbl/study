package cn.beinet.deployment.admin;

import cn.beinet.core.base.configs.SystemConst;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 运维后台主运行类
 * @author youbl
 * @since 2024/11/19 10:45
 */
@SpringBootApplication(scanBasePackages = "cn.beinet")
//@EnableFeignClients(basePackages = {"cn.beinet"})
@EnableAsync
//@MapperScan({"cn.beinet.**.dal"})
@EnableScheduling
public class MainApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(SystemConst.getOuterIp());
    }
}
