package com.beinet.springmybatisplusstudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.beinet.springmybatisplusstudy.dal.mapper"})
public class SpringMybatisPlusStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisPlusStudyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("系统启动");
    }
}
