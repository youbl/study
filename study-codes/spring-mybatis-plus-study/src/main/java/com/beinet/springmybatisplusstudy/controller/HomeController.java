package com.beinet.springmybatisplusstudy.controller;

import com.beinet.springmybatisplusstudy.dal.entity.User;
import com.beinet.springmybatisplusstudy.dal.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public User index() {
        return userMapper.selectById(3);
    }
}
