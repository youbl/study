package cn.beinet.deployment.admin;

import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan({"cn.beinet.**.dal"})
@EnableScheduling
public class AdminApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //System.out.println(SystemConst.getOuterIp());
    }
}
