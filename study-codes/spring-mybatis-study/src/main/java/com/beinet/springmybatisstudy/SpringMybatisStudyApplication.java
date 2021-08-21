package com.beinet.springmybatisstudy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringMybatisStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisStudyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.printf("我是Mybatis演示");
    }
}
