package beinet.cn.springjpastudy;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringJpaStudyApplication {

    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt
        textEncryptor.setPassword("123456");
        //要加密的数据（数据库的用户名或密码）
        String username = "beinet";
        String password = "beinet.cnbeinet.cn";
        System.out.println("username:" + username + " 加密后: " + textEncryptor.encrypt(username));
        System.out.println("password:" + password + " 加密后: " + textEncryptor.encrypt(password));

        SpringApplication.run(SpringJpaStudyApplication.class, args);
    }

}
