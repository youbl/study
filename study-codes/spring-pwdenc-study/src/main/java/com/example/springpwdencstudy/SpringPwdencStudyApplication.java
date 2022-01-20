package com.example.springpwdencstudy;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringPwdencStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        // 参考 https://github.com/ulisesbocchio/jasypt-spring-boot
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("123456");
        // 默认值
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        //要加密的数据（数据库的用户名或密码等）
        String username = "beinet";
        String password = "beinet.cnbeinet.cn";
        System.out.println("username:" + username + " 加密后: " + encryptor.encrypt(username));
        System.out.println("password:" + password + " 加密后: " + encryptor.encrypt(password));

        SpringApplication.run(SpringPwdencStudyApplication.class, args);
    }

    @Value("${pwdTest.ak}")
    private String ak;
    @Value("${pwdTest.sk}")
    private String sk;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(ak + ":" + sk);

        System.out.println(System.getenv());
    }
}
