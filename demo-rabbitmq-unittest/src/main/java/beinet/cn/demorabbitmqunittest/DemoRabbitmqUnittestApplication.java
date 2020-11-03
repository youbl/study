package beinet.cn.demorabbitmqunittest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class DemoRabbitmqUnittestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoRabbitmqUnittestApplication.class, args);
    }

    @Autowired
    RabbitOperator operator;

    @Override
    public void run(String... args) {
        System.out.println("启动");
        operator.publish("发布一条消息：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }
}
