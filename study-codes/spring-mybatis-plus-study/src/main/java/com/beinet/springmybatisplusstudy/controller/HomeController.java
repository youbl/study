package com.beinet.springmybatisplusstudy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beinet.springmybatisplusstudy.dal.entity.User;
import com.beinet.springmybatisplusstudy.dal.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2021/9/18 21:31
 */
@RestController
public class HomeController {
    private final UserMapper userMapper;

    public HomeController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                + " " + this.getClass().getName();
    }

    @GetMapping("user/{id}")
    public User getById(@PathVariable int id) {
        return userMapper.selectById(id);
    }

    /**
     * 返回全部用户
     *
     * @return
     */
    @GetMapping("user/all")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @GetMapping("user")
    public List<User> getByCond(@RequestParam int age, @RequestParam String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ge("age", age);
        wrapper.eq("name", name);
        return userMapper.selectList(wrapper);
    }


}
