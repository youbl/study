package com.beinet.springmybatisstudy;

import com.beinet.springmybatisstudy.entity.User;
import com.beinet.springmybatisstudy.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringMybatisStudyApplication implements CommandLineRunner {
    // 官网 http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/
    private final UserMapper userMapper;

    public SpringMybatisStudyApplication(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisStudyApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("我是Mybatis演示");

        List<User> userList = userMapper.findByAgeGt(20);
        System.out.println("大于20的人数:" + userList.size());
        userList.forEach(System.out::println);

        userList = userMapper.findByAgeLet(20);
        System.out.println("小于等于20的人数:" + userList.size());
        userList.forEach(System.out::println);

        User newUser = new User();
        newUser.setAge(56);
        newUser.setEmail("zhangsan@163.com");
        newUser.setName("张三");
        newUser.setExtension("不存数据库");
        int num = userMapper.add(newUser);
        System.out.println("插入影响行数:" + num);
        System.out.println(newUser);

        newUser.setName("张三改名");
        num = userMapper.update(newUser);
        System.out.println("更新影响行数:" + num);
        userList = userMapper.findByAgeGt(20);
        System.out.println("大于20的人数:" + userList.size());
        userList.forEach(System.out::println);

        num = userMapper.deleteById(newUser.getId());
        System.out.println("删除影响行数:" + num);
        userList = userMapper.findByAgeGt(20);
        System.out.println("大于20的人数:" + userList.size());
        userList.forEach(System.out::println);
    }
}
