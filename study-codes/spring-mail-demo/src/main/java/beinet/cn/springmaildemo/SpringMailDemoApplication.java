package beinet.cn.springmaildemo;

import beinet.cn.springmaildemo.mails.MailDto;
import beinet.cn.springmaildemo.mails.Netease163Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringMailDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringMailDemoApplication.class, args);
    }

    @Autowired
    Netease163Mail netease163;

    @Override
    public void run(String... args) throws Exception {

        String username = "xxx@163.com";
        // 使用163邮箱的授权码
        String password = "123456";

        // 读取邮件
        List<MailDto> mailList = netease163.getMail(username, password);
        // 成功，输出
        System.out.println(mailList);
    }
}
